package eu.sportsfusion.common;

import eu.sportsfusion.common.validation.entity.Errors;
import jakarta.json.JsonObject;
import jakarta.validation.ValidationException;

/**
 * @author Anatoli Pranovich
 */
public class CustomValidationException extends ValidationException {

    private Errors errors;

    // just to rethrow
    private JsonObject errorsJson;

    public CustomValidationException(Errors errors) {
        this.errors = errors;
    }

    public CustomValidationException(JsonObject errorsJson) {
        this.errorsJson = errorsJson;
    }

    public Errors getErrors() {
        return errors;
    }

    public JsonObject getErrorsJson() {
        return errorsJson;
    }
}
