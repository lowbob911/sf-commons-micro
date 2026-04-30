package eu.sportsfusion.common.jsonapi;

import jakarta.json.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anatoli Pranovich
 */
public class RelationshipsObjectBuilder {

    private Map<String, JsonValue> map = new HashMap<>();
    private Collection<String> sparseFields;

    public RelationshipsObjectBuilder withSparseFields(Collection<String> sparseFields) {
        this.sparseFields = sparseFields;
        return this;
    }

    private boolean isSparse(String name) {
        return sparseFields == null || sparseFields.contains(name);
    }

    public RelationshipsObjectBuilder addRelationship(String name, String type, Collection<String> ids) {
        return addRelationship(name, type, ids.toArray(new String[ids.size()]));
    }

    public RelationshipsObjectBuilder addRelationship(String name, String type, String... ids) {

        if (isSparse(name)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (String id : ids) {
                arrayBuilder.add(new ResourceObjectBuilder().addId(id).addType(type).build());
            }

            map.put(name, arrayBuilder.build());
        }
        return this;
    }

    public RelationshipsObjectBuilder addRelationship(String name, String type, String id) {
        if (isSparse(name)) {
            map.put(name, new ResourceObjectBuilder().addId(id).addType(type).build());
        }
        return this;
    }

    public RelationshipsObjectBuilder addNullRelationship(String name) {
        if (isSparse(name)) {
            map.put(name, JsonValue.NULL);
        }
        return this;
    }

    public JsonObject build() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
            builder.add(entry.getKey(), Json.createObjectBuilder().add("data", entry.getValue()));
        }
        return builder.build();
    }
}
