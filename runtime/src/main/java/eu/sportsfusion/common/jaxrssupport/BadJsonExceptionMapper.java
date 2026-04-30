package eu.sportsfusion.common.jaxrssupport;

import eu.sportsfusion.common.jsonsupport.BadJsonException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Anatoli Pranovich
 */
@Provider
public class BadJsonExceptionMapper implements ExceptionMapper<BadJsonException> {

    @Override
    public Response toResponse(BadJsonException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
