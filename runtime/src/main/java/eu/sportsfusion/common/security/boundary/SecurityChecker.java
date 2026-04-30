package eu.sportsfusion.common.security.boundary;

import eu.sportsfusion.common.ForbiddenException;
import eu.sportsfusion.common.NotAuthorizedException;
import eu.sportsfusion.common.security.entity.UserDetails;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Anatoli Pranovich
 */
@Provider
@Deprecated
public class SecurityChecker {

    @Context
    jakarta.ws.rs.core.SecurityContext securityContext;


    @Deprecated
    // use @AccessAllowed annotation
    public void validateRequiredRole(String role) {
        if (!securityContext.isUserInRole(role)) {
            if (securityContext.getUserPrincipal() == null) {
                throw new NotAuthorizedException();
            } else {
                throw new ForbiddenException();
            }
        };
    }

    @Deprecated
    // just @Inject UserDetails userDetails;
    public UserDetails getUser() {
        if (securityContext.getUserPrincipal() != null) {
            return (UserDetails) securityContext.getUserPrincipal();
        }
        return null;
    }
}
