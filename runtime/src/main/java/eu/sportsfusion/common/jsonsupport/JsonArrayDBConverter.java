package eu.sportsfusion.common.jsonsupport;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.StringReader;

/**
 * @author Anatoli Pranovich
 */
@Converter(autoApply = true)
public class JsonArrayDBConverter implements AttributeConverter<JsonArray, String> {


    @Override
    public String convertToDatabaseColumn(JsonArray jsonArray) {
        return jsonArray == null ? null : jsonArray.toString();
    }

    @Override
    public JsonArray convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try (JsonReader reader = Json.createReader(new StringReader(dbData))) {
                return reader.readArray();
            }
        }
    }
}