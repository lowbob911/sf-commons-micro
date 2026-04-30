package eu.sportsfusion.common.security.entity;

import java.security.Principal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author Igor Vashyst
 */
public class UserDetails implements Principal {

    public static final String ROLE_SUPERADMIN = "superadmin";
    public static final String ROLE_CUSTOMER = "customer";

    private UUID id;
    private UUID clientId;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private String token;
    private UUID partnerId;
    private String avatarUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role) {
        return isSuperadmin() || (roles != null && roles.contains(role));
    }

    public boolean isSuperadmin() {
        return roles != null && roles.contains(ROLE_SUPERADMIN);
    }

    public boolean isCustomer() {
        return roles != null && roles.contains(ROLE_CUSTOMER);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(UUID partnerId) {
        this.partnerId = partnerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getName() {

        if (id != null) {
            return id.toString();
        }

        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetails)) return false;
        UserDetails that = (UserDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(roles, that.roles) &&
                Objects.equals(partnerId, that.partnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, firstName, lastName, roles, partnerId);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
