package eu.sportsfusion.common.jsonapi;

import org.junit.jupiter.api.Test;

import jakarta.json.JsonObject;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Anatoli Pranovich
 */
public class AttributesObjectBuilderTest {

    @Test
    public void build() {

        AttributesObjectBuilder attributesObjectBuilder = new AttributesObjectBuilder();

        attributesObjectBuilder.add("firstName", "Jeremy");
        attributesObjectBuilder.add("lastName", "Kerner");

        JsonObject jsonObject = attributesObjectBuilder.build();

        assertEquals("Jeremy", jsonObject.getString("firstName", null));
        assertEquals("Kerner", jsonObject.getString("lastName", null));

        attributesObjectBuilder = new AttributesObjectBuilder();

        attributesObjectBuilder.withSparseFields(Collections.singleton("firstName"));

        attributesObjectBuilder.add("firstName", "Jeremy");
        attributesObjectBuilder.add("lastName", "Kerner");

        jsonObject = attributesObjectBuilder.build();

        assertEquals("Jeremy", jsonObject.getString("firstName", null));
        assertNull(jsonObject.getString("lastName", null));
    }
}