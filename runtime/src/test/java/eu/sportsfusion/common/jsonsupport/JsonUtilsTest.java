package eu.sportsfusion.common.jsonsupport;

import eu.sportsfusion.common.Money;
import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Igor Vashyst
 */
public class JsonUtilsTest {

    @Test
    public void shouldConvertJsonObjectToMap() {
        JsonObject object = Json.createObjectBuilder()
                .addNull("null")
                .add("number", 7)
                .add("string", "hello")
                .add("true", true)
                .add("false", false)
                .add("array", Json.createArrayBuilder()
                        .add(1)
                        .add("string"))
                .add("object", Json.createObjectBuilder()
                        .add("string1", "string1")
                        .add("number1", 5)
                        .add("array1", Json.createArrayBuilder()
                                .add(9)
                                .add(3)))
                .build();

        Map<String, Object> map = JsonUtils.convertToMap(object);

        assertEquals(null, map.get("null"));
        assertEquals(BigDecimal.valueOf(7), map.get("number"));
        assertEquals("hello", map.get("string"));
        assertEquals(true, map.get("true"));
        assertEquals(false, map.get("false"));
        assertArrayEquals(new Object[]{BigDecimal.valueOf(1), "string"}, ((List) map.get("array")).toArray());

        Map<String, Object> o = (Map<String, Object>) map.get("object");
        assertEquals("string1", o.get("string1"));
        assertEquals(BigDecimal.valueOf(5), o.get("number1"));
        assertArrayEquals(new Object[]{BigDecimal.valueOf(9), BigDecimal.valueOf(3)}, ((List) o.get("array1")).toArray());
    }

    @Test
    public void getMoneyTest() {
        // no value
        assertNull(JsonUtils.getMoney(Json.createObjectBuilder().build(), "amount"));

        // null value
        assertNull(JsonUtils.getMoney(Json.createObjectBuilder().add("amount", JsonValue.NULL).build(), "amount"));


        // String value
        assertEquals(new Money("10.99"), JsonUtils.getMoney(Json.createObjectBuilder().add("amount", "10.99").build(), "amount"));

        // String value
        assertEquals(new Money("10"), JsonUtils.getMoney(Json.createObjectBuilder().add("amount", "10").build(), "amount"));

        // Number value
        assertEquals(new Money("10.99"), JsonUtils.getMoney(Json.createObjectBuilder().add("amount", 10.99).build(), "amount"));

        // Number value
        assertEquals(new Money("10"), JsonUtils.getMoney(Json.createObjectBuilder().add("amount", 10).build(), "amount"));

    }
}
