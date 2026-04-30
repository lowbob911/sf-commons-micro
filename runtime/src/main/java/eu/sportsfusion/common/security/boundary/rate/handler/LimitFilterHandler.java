package eu.sportsfusion.common.security.boundary.rate.handler;

import jakarta.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

import eu.sportsfusion.common.security.boundary.rate.RateLimit;

public interface LimitFilterHandler {
    void handleRateLimit(ContainerRequestContext requestContext, String clientIpAddress, RateLimit limit) throws IOException;
}
