package eu.sportsfusion.common.jaxrssupport;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Anatoli Pranovich
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION - 100)
public class HttpMethodOverrideEnabler implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String override = requestContext.getHeaders().getFirst("X-HTTP-Method-Override");

        if (override != null) {
            requestContext.setMethod(override);
        }
    }
}
