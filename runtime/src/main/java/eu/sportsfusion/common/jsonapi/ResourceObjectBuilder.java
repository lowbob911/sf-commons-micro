package eu.sportsfusion.common.jsonapi;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * @author Anatoli Pranovich
 */
public class ResourceObjectBuilder {

    private String id;
    private String type;
    private JsonObject attributes;
    private JsonObject relationships;

    public ResourceObjectBuilder addId(String id) {
        this.id = id;
        return this;
    }

    public ResourceObjectBuilder addType(String type) {
        this.type = type;
        return this;
    }

    public ResourceObjectBuilder addAttributes(JsonObject attributes) {
        this.attributes = attributes;
        return this;
    }

    public ResourceObjectBuilder addAttributes(JsonObjectBuilder attributesBuilder) {
        this.attributes = attributesBuilder.build();
        return this;
    }

    public ResourceObjectBuilder addAttributes(AttributesObjectBuilder attributesBuilder) {
        this.attributes = attributesBuilder.build();
        return this;
    }

    public ResourceObjectBuilder addRelationships(JsonObject relationships) {
        this.relationships = relationships;
        return this;
    }

    public ResourceObjectBuilder addRelationships(RelationshipsObjectBuilder relationshipsBuilder) {
        this.relationships = relationshipsBuilder.build();
        return this;
    }

    public JsonObject build() {
        JsonObjectBuilder builder = Json.createObjectBuilder().add("type", type);

        if (id != null) {
            builder.add("id", id);
        }

        if (attributes != null) {
            builder.add("attributes", attributes);
        }

        if (relationships != null) {
            builder.add("relationships", relationships);
        }

        return builder.build();
    }


}
