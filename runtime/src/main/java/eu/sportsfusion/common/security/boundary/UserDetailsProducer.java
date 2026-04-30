package eu.sportsfusion.common.security.boundary;

import eu.sportsfusion.common.security.entity.UserDetails;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;

/**
 * @author Anatoli Pranovich
 */
@RequestScoped
public class UserDetailsProducer {
    private UserDetails userDetails;

    public void handleAuthenticationEvent(@Observes UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Produces
    @RequestScoped
    public UserDetails produceUserDetails() {
        return userDetails;
    }
}
