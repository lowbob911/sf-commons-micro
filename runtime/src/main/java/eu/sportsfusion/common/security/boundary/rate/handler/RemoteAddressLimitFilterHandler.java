package eu.sportsfusion.common.security.boundary.rate.handler;

import jakarta.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

import eu.sportsfusion.common.configuration.control.Config;
import eu.sportsfusion.common.security.boundary.rate.RateLimit;
import eu.sportsfusion.common.security.boundary.rate.RateLimitConfiguration;
import eu.sportsfusion.common.security.boundary.rate.RateLimitService;

public class RemoteAddressLimitFilterHandler extends AbstractBaseLimitFilterHandler {

    public RemoteAddressLimitFilterHandler(Config config, RateLimitService rateLimitService) {
        super(config, rateLimitService);
    }

    @Override
    public void handleRateLimit(ContainerRequestContext requestContext, String clientIpAddress, RateLimit limit) throws IOException {
        // Apply rate limiting by source IP address
        rateLimitService.checkExceeded(limit.name(), clientIpAddress, getConfig(limit.name(), config));
    }

    @Override
    protected RateLimitConfiguration getConfigFor(String name, Config config) {
        return RateLimitConfiguration.forRemoteAddress(name, config);
    }

}