package eu.sportsfusion.common.security.boundary;

import java.security.Principal;

import eu.sportsfusion.common.security.entity.UserDetails;

/**
 * @author Anatoli Pranovich
 * @author Igor Vashyst
 */
public class SecurityContext implements jakarta.ws.rs.core.SecurityContext {

    private UserDetails principal;

    public SecurityContext(UserDetails userDetails) {
        this.principal = userDetails;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return principal != null && principal.hasRole(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
