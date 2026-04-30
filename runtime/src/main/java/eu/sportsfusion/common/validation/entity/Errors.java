package eu.sportsfusion.common.validation.entity;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import eu.sportsfusion.common.jsonapi.FieldsMapper;
import eu.sportsfusion.common.jsonsupport.CustomJsonObjectBuilder;

/**
 * @author Anatoli Pranovich
 */
public class Errors {

    private Set<Error> errors = new HashSet<>();
    private FieldsMapper mapper = field -> null;

    public Errors() {
    }

    public Errors(FieldsMapper mapper) {
        this.mapper = mapper;
    }

    public void addError(String source, String code, String title) {
        Error error = new Error();
        error.setCode(code);
        error.setSource(source);
        error.setTitle(title);
        errors.add(error);
    }

    public JsonObject getErrorsAsJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Error error : errors) {

            CustomJsonObjectBuilder builder = new CustomJsonObjectBuilder();
            builder.add("code", error.getCode());

            String jsonPointer = mapper.getJsonPointer(error.getSource());

            builder.add("source", Json.createObjectBuilder().add("pointer",
                    jsonPointer == null ? mapper.getAttributesPrefix() + error.getSource() : jsonPointer));

            builder.add("title", error.getTitle());

            arrayBuilder.add(builder);
        }

        return Json.createObjectBuilder().add("errors", arrayBuilder).build();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void addConstraintViolations(Set<? extends ConstraintViolation<?>> violations) {
        addConstraintViolations(null, violations);
    }

    public void addConstraintViolations(String sourcePrefix, Set<? extends ConstraintViolation<?>> violations) {
        sourcePrefix = sourcePrefix == null ? "" : sourcePrefix + "/";

        for (ConstraintViolation<?> violation : violations) {
            String code = "ERR_UNKNOWN";
            String title = violation.getMessage();

            if (violation.getMessage().startsWith("ERR_")) {
                code = title.substring(0, title.indexOf(":"));
                title = title.substring(title.indexOf(":") + 1).trim();
            }

            addError(sourcePrefix + violation.getPropertyPath().toString(), code, title);
        }
    }
}
