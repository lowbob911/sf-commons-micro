package eu.sportsfusion.common.security.boundary.rate;

import eu.sportsfusion.common.configuration.control.Config;

public class RateLimitConfiguration {
    private static final String CONFIG_PREFIX = "sf.rate.";

    public static final boolean DEFAULT_ENFORCING = true;
    public static final Long DEFAULT_LIMIT = 10L;
    public static final Long DEFAULT_PERIOD = 300L;
    public static final Long REMOTEADDRESS_DEFAULT_LIMIT = 100L;
    public static final Long REMOTEADDRESS_DEFAULT_PERIOD = 300L;

    private final boolean enforcing;
    private final long limit;
    private final long period;

    public boolean isEnforcing() {
        return enforcing;
    }

    public long getLimit() {
        return limit;
    }

    public long getPeriod() {
        return period;
    }

    public RateLimitConfiguration(boolean enforcing, long limit, long period) {
        this.enforcing = enforcing;
        this.limit = limit;
        this.period = period;
    }

    private static RateLimitConfiguration defaultFor(String limit, Config config, Long defaultRequestLimit, Long defaultRequestPeriod) {
        final String prefix = CONFIG_PREFIX + limit + ".";
        return new RateLimitConfiguration(
                config.getBooleanProperty(prefix + "enforcing", config.getBooleanProperty(CONFIG_PREFIX + "global.enforcing", DEFAULT_ENFORCING)),
                config.getLongProperty(prefix + "request.limit", defaultRequestLimit),
                config.getLongProperty(prefix + "request.period", defaultRequestPeriod)
        );
    }

    public static RateLimitConfiguration defaultFor(String limit, Config config) {
        return defaultFor(limit, config, DEFAULT_LIMIT, DEFAULT_PERIOD);
    }

    public static RateLimitConfiguration forRemoteAddress(String limit, Config config) {
        return defaultFor(limit, config, REMOTEADDRESS_DEFAULT_LIMIT, REMOTEADDRESS_DEFAULT_PERIOD);
    }

    @Override
    public String toString() {
        return "RateLimitConfiguration{" +
                "enforcing=" + enforcing +
                ", limit=" + limit +
                ", period=" + period +
                '}';
    }
}
