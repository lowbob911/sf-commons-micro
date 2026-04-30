package eu.sportsfusion.common.security.boundary.rate;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import eu.sportsfusion.common.StringUtils;
import eu.sportsfusion.common.configuration.control.Config;

/**
 * Track and process rate limits based on keys within named limits.
 *
 * The rate of requests is limited per key, and defined by passed in
 * RateLimitConfiguration objects.
 *
 * @author John Girvin
 */
@ApplicationScoped
public class RateLimitService {
    // -----------------------------------------------------------------------
    // DEPENDENCIES

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    Config config;

    // -----------------------------------------------------------------------
    // INSTANCE VARIABLES

    // map rate limit names and keys to Bucket instances
    // each Bucket tracks the limiting for the limit+key combination
    private final Map<String,Map<String,BucketEntry>> buckets = new ConcurrentHashMap<>();

    // track configured period (seconds) per limit name for eviction calculations
    private final Map<String,Long> limitPeriods = new ConcurrentHashMap<>();

    private ScheduledExecutorService cleanupExecutor;

    // -----------------------------------------------------------------------
    // LIFECYCLE

    @PostConstruct
    void startCleanup() {
        cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "rate-limit-cleanup");
            t.setDaemon(true);
            return t;
        });
        cleanupExecutor.scheduleWithFixedDelay(this::evictStaleEntries, 1, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    void stopCleanup() {
        if (cleanupExecutor != null) {
            cleanupExecutor.shutdown();
        }
    }

    // -----------------------------------------------------------------------
    // RateLimitService

    /**
     * Container class to hold the outcome of a rate limiting check.
     */
    public static class Result {
        boolean isExceeded;
        long remaining;
        long retrytime;

        public Result() {
            this(false, 0, 0);
        }

        public Result(boolean isExceeded, long remaining, long retrytime) {
            this.isExceeded = isExceeded;
            this.remaining = remaining;
            this.retrytime = retrytime;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "isExceeded=" + isExceeded +
                    ", remaining=" + remaining +
                    ", retrytime=" + retrytime +
                    '}';
        }
    }

    /**
     * Consume one request from the specified limit/key rate limit.
     * Throw RateLimitException if the limit has been exceeded
     *
     * @param limit - the limit name
     * @param key - the key within the limit name
     * @param rlc - limit configuration
     *
     * @throws RateLimitException
     */
    public void checkExceeded(final String limit, final String key, final RateLimitConfiguration rlc) throws RateLimitException {
        if (rlc.isEnforcing()) {
            Result result = isExceeded(limit, key, rlc);
            if (result.isExceeded) {
                logger.warning(limit + ": [" + key + "] rate exceeded " + result);
                throw new RateLimitException();
            }
        }
    }

    /**
     * Consume one request from the specified limit/key rate limit and return the result.
     * result.isExceeded will indicate if the rate limit was exceeded by this operation or not.
     *
     * @param limit - the limit name
     * @param key - the key within the limit name
     * @param rlc - limit configuration
     *
     * @return result
     */
    public Result isExceeded(final String limit, final String key, final RateLimitConfiguration rlc) {
        // sanity check
        if (StringUtils.isBlank(limit) || StringUtils.isBlank(key)) {
            logger.warning("RateLimitService.isExceeded invoked with blank limit/key [" + limit + "/" + key + "]");
            return new Result();
        }

        limitPeriods.putIfAbsent(limit, rlc.getPeriod());

        // get or create the key limit bucket container for this limit
        Map<String,BucketEntry> limitKeyBuckets = buckets.get(limit);
        if (limitKeyBuckets == null) {
            limitKeyBuckets = new ConcurrentHashMap<>();
            buckets.put(limit, limitKeyBuckets);
        }

        // get or create the individual limit bucket for this key
        BucketEntry entry = limitKeyBuckets.get(key);
        if (entry == null) {
            entry = new BucketEntry(createLimitBucket(rlc));
            limitKeyBuckets.put(key, entry);
        }
        entry.lastAccessed = System.currentTimeMillis();

        // check parameter bucket has enough tokens to permit this request
        ConsumptionProbe probe = entry.bucket.tryConsumeAndReturnRemaining(1L);

        Result result = new Result(
                !probe.isConsumed(),
                probe.getRemainingTokens(),
                TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())
        );

        if (config.isDevelopment()) {
            logger.info(limit + ": [" + key + "] " + result);
        }

        return result;
    }

    // -----------------------------------------------------------------------
    // HELPER METHODS

    protected Bucket createLimitBucket(final RateLimitConfiguration rlc) {
        Bandwidth bandwidth = Bandwidth.simple(
            rlc.getLimit(),
            Duration.ofSeconds(rlc.getPeriod())
        );

        Bucket bucket = Bucket4j.builder().addLimit(bandwidth).build();
        return bucket;
    }

    // -----------------------------------------------------------------------
    // CLEANUP

    void evictStaleEntries() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String,Map<String,BucketEntry>>> limitIter = buckets.entrySet().iterator();
        while (limitIter.hasNext()) {
            Map.Entry<String,Map<String,BucketEntry>> limitEntry = limitIter.next();
            String limit = limitEntry.getKey();
            Map<String,BucketEntry> keyMap = limitEntry.getValue();
            Long periodSeconds = limitPeriods.get(limit);
            if (periodSeconds == null) {
                periodSeconds = RateLimitConfiguration.DEFAULT_PERIOD;
            }
            long evictionThresholdMs = periodSeconds * 2 * 1000;

            Iterator<Map.Entry<String,BucketEntry>> keyIter = keyMap.entrySet().iterator();
            while (keyIter.hasNext()) {
                Map.Entry<String,BucketEntry> keyEntry = keyIter.next();
                if (now - keyEntry.getValue().lastAccessed > evictionThresholdMs) {
                    keyIter.remove();
                }
            }
            if (keyMap.isEmpty()) {
                limitIter.remove();
                limitPeriods.remove(limit);
            }
        }
    }

    private static class BucketEntry {
        final Bucket bucket;
        volatile long lastAccessed;

        BucketEntry(Bucket bucket) {
            this.bucket = bucket;
            this.lastAccessed = System.currentTimeMillis();
        }
    }

}
