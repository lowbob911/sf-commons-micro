package eu.sportsfusion.common.jsonapi;

import org.junit.jupiter.api.Test;

import jakarta.json.JsonObject;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Anatoli Pranovich
 */
public class RelationshipsObjectBuilderTest {

    @Test
    public void build() {
        RelationshipsObjectBuilder relationshipsObjectBuilder = new RelationshipsObjectBuilder();

        relationshipsObjectBuilder.addRelationship("person", "persons", "Jeremy");
        relationshipsObjectBuilder.addRelationship("company", "companies", "SF");

        JsonObject jsonObject = relationshipsObjectBuilder.build();

        assertNotNull(jsonObject.getJsonObject("person"));
        assertNotNull(jsonObject.getJsonObject("company"));

        relationshipsObjectBuilder = new RelationshipsObjectBuilder();

        relationshipsObjectBuilder.withSparseFields(Collections.singleton("person"));

        relationshipsObjectBuilder.addRelationship("person", "persons", "Jeremy");
        relationshipsObjectBuilder.addRelationship("company", "companies", "SF");
        jsonObject = relationshipsObjectBuilder.build();

        assertNotNull(jsonObject.getJsonObject("person"));
        assertNull(jsonObject.getJsonObject("company"));
    }
}