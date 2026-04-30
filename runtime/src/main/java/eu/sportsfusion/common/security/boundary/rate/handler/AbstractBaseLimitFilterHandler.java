package eu.sportsfusion.common.security.boundary.rate.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import eu.sportsfusion.common.configuration.control.Config;
import eu.sportsfusion.common.security.boundary.rate.RateLimitConfiguration;
import eu.sportsfusion.common.security.boundary.rate.RateLimitService;

public abstract class AbstractBaseLimitFilterHandler implements LimitFilterHandler {
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected final Config config;

    protected final RateLimitService rateLimitService;

    private final Map<String,RateLimitConfiguration> configs = new ConcurrentHashMap<>();

    public AbstractBaseLimitFilterHandler(Config config, RateLimitService rateLimitService) {
        this.config = config;
        this.rateLimitService = rateLimitService;
    }

    protected RateLimitConfiguration getConfigFor(String name, Config config) {
        return RateLimitConfiguration.defaultFor(name, config);
    }

    protected RateLimitConfiguration getConfig(String name, Config config) {
        RateLimitConfiguration rlc = configs.get(name);
        if (rlc == null) {
            rlc = getConfigFor(name, config);
            configs.put(name, rlc);
            logger.info(name + ": " + rlc);
        }
        return rlc;
    }

}
