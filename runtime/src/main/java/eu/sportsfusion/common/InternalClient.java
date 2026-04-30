package eu.sportsfusion.common;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import java.net.URI;

import eu.sportsfusion.common.jsonapi.MediaType;
import eu.sportsfusion.common.security.boundary.TokenManager;

/**
 * @author Igor Vashyst
 */
@ApplicationScoped
@Deprecated
public class InternalClient {

    @Inject
    TokenManager tokenManager;

    public Response get(URI uri) {
        return get(uri, tokenManager.createInternalToken());
    }

    public Response get(URI uri, String token) {
        Response response =  ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_VND_API_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .get();

        handleResponse(response);

        return response;
    }

    public Response post(URI uri, JsonObject jsonObject) {
        return post(uri, jsonObject, tokenManager.createInternalToken());
    }

    public Response post(URI uri, JsonObject jsonObject, String token) {
        Response response =  ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_VND_API_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .post(Entity.entity(jsonObject, MediaType.APPLICATION_VND_API_JSON));

        handleResponse(response);

        return response;
    }

    public Response patch(URI uri, JsonObject jsonObject) {
        return patch(uri, jsonObject, tokenManager.createInternalToken());
    }

    public Response patch(URI uri, JsonObject jsonObject, String token) {
        Response response =  ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_VND_API_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header("X-HTTP-Method-Override", "PATCH")
                .post(Entity.entity(jsonObject, MediaType.APPLICATION_VND_API_JSON));

        handleResponse(response);

        return response;
    }

    public Response delete(URI uri) {
        return delete(uri, tokenManager.createInternalToken());
    }

    public Response delete(URI uri, String token) {
        Response response =  ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_VND_API_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .delete();

        handleResponse(response);

        return response;
    }

    private void handleResponse(Response response) {
        if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            throw new NotAuthorizedException();
        } if (response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            throw new ForbiddenException();
        } else if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new RuntimeException(response.getHeaderString("X-Reason"));
        }
    }
}
