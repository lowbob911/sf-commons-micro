package eu.sportsfusion.common.security.boundary.rate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import eu.sportsfusion.common.RequestUtils;
import eu.sportsfusion.common.configuration.control.Config;
import eu.sportsfusion.common.security.boundary.rate.handler.JsonParameterLimitFilterHandler;
import eu.sportsfusion.common.security.boundary.rate.handler.LimitFilterHandler;
import eu.sportsfusion.common.security.boundary.rate.handler.RemoteAddressLimitFilterHandler;

import static jakarta.ws.rs.core.Response.Status.Family.CLIENT_ERROR;

/**
 * Filter implementation to apply rate limiting to methods using RateLimitService.
 *
 * Bind to target methods with (repeatable) RateLimit annotations, for example:
 *  [@]RateLimit(name = "remoteip", type = RateLimitType.RemoteAddress)
 *  [@]RateLimit(name = "username", type = RateLimitType.JsonParameter, parameter = "username")
 *  public Response login(...) {
 *      ...
 *  }
 *
 * Refer to RateLimitConfiguration class for configuration details.
 *
 * @author John Girvin
 */
@Provider
@Priority(Priorities.AUTHENTICATION + 1)
@RateLimited
public class RateLimitFilter implements ContainerRequestFilter {

    /**
     * StatusType indiciating HTTP 429 Rate exceeded
     * Not included in predefined Response.Status enum
     */
    public static final Response.StatusType RATE_EXCEEDED = new Response.StatusType(){
        @Override
        public int getStatusCode() {
            return 429;
        }

        @Override
        public Response.Status.Family getFamily() {
            return CLIENT_ERROR;
        }

        @Override
        public String getReasonPhrase() {
            return "Too Many Requests";
        }
    };

    // --------------------------------------------------------------------------------
    // DEPENDENCIES

    @Inject
    Config config;

    @Inject
    RateLimitService rateLimitService;

    @Context
    ResourceInfo resourceInfo;

    // Map RateLimitType to a class to handle rate limiting requests by that type
    private final Map<RateLimitType,LimitFilterHandler> handlerDispatcher = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        handlerDispatcher.put(RateLimitType.RemoteAddress, new RemoteAddressLimitFilterHandler(config, rateLimitService));
        handlerDispatcher.put(RateLimitType.JsonParameter, new JsonParameterLimitFilterHandler(config, rateLimitService));
    }

    // --------------------------------------------------------------------------------
    // ContainerRequestFilter

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Find @RateLimit annotations on call site, grouped by @RateLimited
        Method method = resourceInfo.getResourceMethod();
        if (method == null) {
            return;
        }

        RateLimited limits = method.getAnnotation(RateLimited.class);
        if (limits == null) {
            return;
        }

        // Iterate over discovered RateLimit annotations
        boolean isExceeded = false;
        for (RateLimit limit: limits.value()) {
            try {
                // Apply matching LimitFilterHandler to the current request
                LimitFilterHandler handler = handlerDispatcher.get(limit.type());
                if (handler == null) {
                    throw new IOException("Unknown RateLimitType " + limit.type());
                }

                handler.handleRateLimit(requestContext, RequestUtils.getClientIpAddress(requestContext), limit);
            } catch (RateLimitException rle) {
                // We want to process all annotations so just remember if any flagged as exceeded and handle after the loop
                isExceeded = true;
            }
        }

        // Abort request if rate limit has been exceeded
        if (isExceeded) {
            requestContext.abortWith(Response.status(RATE_EXCEEDED).build());
        }
    }

}
