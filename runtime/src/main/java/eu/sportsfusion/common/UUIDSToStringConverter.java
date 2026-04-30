package eu.sportsfusion.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author Anatoli Pranovich
 */
@Converter
public class UUIDSToStringConverter implements AttributeConverter<List<UUID>, String> {

    @Override
    public String convertToDatabaseColumn(List<UUID> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(",");

        for (UUID uuid : attribute) {
            joiner.add(uuid.toString());
        }

        return joiner.toString();
    }

    @Override
    public List<UUID> convertToEntityAttribute(String dbData) {
        List<UUID> result = new ArrayList<>();

        if (StringUtils.isBlank(dbData)) {
            return result;
        }

        String[] data = dbData.split(",");

        for (String s : data) {
            result.add(UUID.fromString(s));
        }
        return result;
    }
}

