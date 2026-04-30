package eu.sportsfusion.common.jsonsupport;

import jakarta.json.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import eu.sportsfusion.common.Money;

import static jakarta.json.JsonValue.ValueType.*;

/**
 * @author Anatoli Pranovich
 */
public class JsonUtils {

    private static JsonNumber getJsonNumber(JsonObject jsonObject, String name) {
        if (!jsonObject.containsKey(name) || jsonObject.isNull(name)) {
            return null;
        }

        try {
            return jsonObject.getJsonNumber(name);
        } catch (ClassCastException e) {
            throw new BadJsonException();
        }
    }

    public static Integer getInteger(JsonObject jsonObject, String name) {
        JsonNumber number = getJsonNumber(jsonObject, name);

        if (number != null) {
            return number.intValue();
        }

        return null;
    }

    public static BigDecimal getDecimal(JsonObject jsonObject, String name) {
        JsonNumber number = getJsonNumber(jsonObject, name);

        if (number != null) {
            return number.bigDecimalValue();
        }

        return null;
    }

    public static Instant getDateTime(JsonObject jsonObject, String name) {
        return getTime(jsonObject, name, Instant::parse);
    }

    public static LocalDate getDate(JsonObject jsonObject, String name) {
        return getTime(jsonObject, name, LocalDate::parse);
    }

    public static LocalTime getTime(JsonObject jsonObject, String name) {
        return getTime(jsonObject, name, LocalTime::parse);
    }

    private static <T extends Temporal> T getTime(JsonObject jsonObject, String name, Function<String, T> parser) {
        String string = jsonObject.getString(name, null);

        if (string != null) {
            try {
                return parser.apply(string);
            } catch (DateTimeParseException e) {
                throw new BadJsonException();
            }
        }
        return null;
    }

    public static Money getMoney(JsonObject jsonObject, String name) {
        if (!jsonObject.containsKey(name) || jsonObject.isNull(name)) {
            return null;
        }

        JsonValue val = jsonObject.get(name);

        if (val instanceof JsonNumber) {

            JsonNumber number = getJsonNumber(jsonObject, name);

            if (number != null) {
                return new Money(number.bigDecimalValue().toString());
            }
        }

        if (val instanceof JsonString) {
            return new Money(jsonObject.getString(name));
        }
        return null;
    }

    public static UUID getUUID(JsonObject jsonObject, String name) {
        return getUUID(jsonObject.getString(name, null));
    }

    public static UUID getUUID(String string) {
        if (string != null) {
            try {
                return UUID.fromString(string);
            } catch (IllegalArgumentException e) {
                throw new BadJsonException();
            }
        }
        return null;
    }

    public static <T> JsonArray asJsonArray(Collection<? extends T> list, Function<? super T, JsonObject> mapper) {
        return list.stream().map(mapper).collect(JsonCollectors.toJsonObjectArray()).build();
    }

    public static CustomJsonObjectBuilder convertToBuilder(JsonObject jsonObject) {
        CustomJsonObjectBuilder builder = new CustomJsonObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        return builder;
    }

    public static String jsonStringAsString(JsonValue jsonValue) {
        return ((JsonString) jsonValue).getString();
    }

    public static Map<String, Object> convertToMap(JsonObject jsonObject) {
        Map<String, Object> result = new HashMap<>();

        jsonObject.forEach((key, value) -> result.put(key, convertJsonValue(value)));

        return result;
    }

    private static Object convertJsonValue(JsonValue value) {
        JsonValue.ValueType type = value.getValueType();

        if (type == OBJECT) {
            return convertToMap((JsonObject) value);
        } else if (type == ARRAY) {
            return ((JsonArray) value).stream()
                    .map(JsonUtils::convertJsonValue)
                    .collect(Collectors.toList());
        } else if (type == NUMBER) {
            return ((JsonNumber) value).bigDecimalValue();
        } else if (type == STRING) {
            return ((JsonString) value).getString();
        } else if (type == TRUE || type == FALSE) {
            return type == TRUE;
        }

        return null;
    }
}
