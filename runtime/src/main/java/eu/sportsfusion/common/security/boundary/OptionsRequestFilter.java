package eu.sportsfusion.common.security.boundary;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

import eu.sportsfusion.common.configuration.control.Config;

/**
 * @author Anatoli Pranovich
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION - 90)
public class OptionsRequestFilter implements ContainerRequestFilter {

    @Inject
    private Config config;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!config.isCORSEnabled()) {
            return;
        }

        if (!HttpMethod.OPTIONS.equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        // Only handle actual browser CORS preflight. Let other OPTIONS requests proceed normally.
        String origin = requestContext.getHeaderString("Origin");
        String requestedMethod = requestContext.getHeaderString("Access-Control-Request-Method");
        if (origin == null || requestedMethod == null) {
            return;
        }

        String requestedHeaders = requestContext.getHeaderString("Access-Control-Request-Headers");
        String allowedHeaders = (requestedHeaders == null || requestedHeaders.isBlank())
                ? CorsResponseFilter.DEFAULT_ALLOWED_HEADERS
                : requestedHeaders;

        requestContext.abortWith(Response.ok()
                .header("Access-Control-Allow-Origin", origin)
                .header("Vary", "Origin")
                .header("Vary", "Access-Control-Request-Method")
                .header("Vary", "Access-Control-Request-Headers")
                .header("Access-Control-Allow-Methods", CorsResponseFilter.ALLOWED_METHODS)
                .header("Access-Control-Allow-Headers", allowedHeaders)
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Max-Age", CorsResponseFilter.MAX_AGE)
                .header("x-responded-by", "options-request-filter")
                .build());
    }
}
