package eu.sportsfusion.common.jsonsupport;

import eu.sportsfusion.common.Money;
import org.junit.jupiter.api.Test;

import jakarta.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Anatoli Pranovich
 */
public class CustomJsonObjectBuilderTest {

    @Test
    public void build() {

        JsonObject jsonObject = new CustomJsonObjectBuilder().
                add("string", "string").
                add("money", new Money("25")).
                add("int", 3).build();

        assertEquals("string", jsonObject.getString("string"));
        assertEquals(new Money("25"), JsonUtils.getMoney(jsonObject,"money"));
        assertEquals(3, jsonObject.getInt("int"));
    }
}