package eu.sportsfusion.common.jaxrssupport;

import eu.sportsfusion.common.CustomValidationException;
import eu.sportsfusion.common.validation.entity.Errors;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Anatoli Pranovich
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException e) {

        if (e instanceof CustomValidationException) {
            CustomValidationException cve = (CustomValidationException) e;

            Errors errors = cve.getErrors();

            if (errors != null && errors.hasErrors()) {
                return Response.status(422).entity(errors.getErrorsAsJson()).build();
            }

            if (cve.getErrorsJson() != null) {
                return Response.status(422).entity(cve.getErrorsJson()).build();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("X-Reason", e.getMessage()).build();
    }
}
