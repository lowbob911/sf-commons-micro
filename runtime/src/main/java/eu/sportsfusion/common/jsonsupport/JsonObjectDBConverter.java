package eu.sportsfusion.common.jsonsupport;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.StringReader;

/**
 * @author Anatoli Pranovich
 */
@Converter(autoApply = true)
public class JsonObjectDBConverter implements AttributeConverter<JsonObject, String> {


    @Override
    public String convertToDatabaseColumn(JsonObject jsonObject) {
        return jsonObject == null ? null : jsonObject.toString();
    }

    @Override
    public JsonObject convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try (JsonReader reader = Json.createReader(new StringReader(dbData))) {
                return reader.readObject();
            }
        }
    }
}