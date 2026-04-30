package eu.sportsfusion.common.security.boundary;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

import eu.sportsfusion.common.configuration.control.Config;

/**
 * @author Anatoli Pranovich
 */
@Provider
public class CorsResponseFilter implements ContainerResponseFilter {

    @Inject
    private Config config;

    public static final String ALLOWED_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD";
    public final static int MAX_AGE = 42 * 60 * 60;
    public final static String DEFAULT_ALLOWED_HEADERS = "origin,accept,content-type,Authorization,X-User-Locale";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (config.isCORSEnabled()) {
            String origin = requestContext.getHeaderString("Origin");
            final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
            if (origin != null && !origin.isBlank()) {
                // With credentials, wildcard origin is not allowed by browsers; reflect request origin.
                headers.putSingle("Access-Control-Allow-Origin", origin);
                headers.add("Vary", "Origin");
                headers.putSingle("Access-Control-Allow-Credentials", "true");
            } else {
                headers.putSingle("Access-Control-Allow-Origin", "*");
            }

            headers.putSingle("Access-Control-Allow-Headers", getRequestedHeaders(requestContext));
            headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.add("Access-Control-Max-Age", MAX_AGE);
            headers.add("x-responded-by", "cors-response-filter");
        }
    }

    String getRequestedHeaders(ContainerRequestContext requestContext) {
        String requestedHeaders = requestContext.getHeaderString("Access-Control-Request-Headers");
        if (requestedHeaders == null || requestedHeaders.isBlank()) {
            return DEFAULT_ALLOWED_HEADERS;
        }
        return requestedHeaders;
    }

}
