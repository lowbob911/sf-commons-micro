package eu.sportsfusion.common.jaxrssupport;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Anatoli Pranovich
 */
@Provider
public class CommonExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("X-Reason", e.getMessage()).build();
    }
}
