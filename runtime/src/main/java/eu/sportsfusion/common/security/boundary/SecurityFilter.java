package eu.sportsfusion.common.security.boundary;

import jakarta.annotation.Priority;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import eu.sportsfusion.common.StringUtils;
import eu.sportsfusion.common.security.AccessAllowed;
import eu.sportsfusion.common.security.entity.UserDetails;

/**
 * @author Anatoli Pranovich
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    TokenManager tokenManager;

    @Inject
    Event<UserDetails> userDetailsEvent;

    @Context
    ResourceInfo resourceInfo;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info(requestContext.getMethod() + " " + requestContext.getUriInfo().getRequestUri());

        String authToken = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        UserDetails userDetails = null;

        if (StringUtils.isNotBlank(authToken) && authToken.startsWith("Bearer ")) {

            userDetails = this.tokenManager.parseToken(authToken.substring("Bearer ".length()));

            if (userDetails == null) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            } else {
                requestContext.setSecurityContext(new SecurityContext(userDetails));
                userDetailsEvent.fire(userDetails);
            }
        }

        // Authorization

        List<String> roles = getRoles(resourceInfo.getResourceMethod());

        // method is not annotated
        if (roles.isEmpty()) {

            // check class then
            roles = getRoles(resourceInfo.getResourceClass());
        }

        if (!roles.isEmpty()) {

            if (userDetails == null) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            } else {

                for (String role : roles) {
                    if (userDetails.hasRole(role)) {
                        return;
                    }
                }
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        }
    }

    private List<String> getRoles(AnnotatedElement annotatedElement) {

        if (annotatedElement != null) {
            AccessAllowed accessAllowed = annotatedElement.getAnnotation(AccessAllowed.class);

            if (accessAllowed != null) {
                return Arrays.asList(accessAllowed.value());
            }
        }

        return Collections.emptyList();
    }
}
