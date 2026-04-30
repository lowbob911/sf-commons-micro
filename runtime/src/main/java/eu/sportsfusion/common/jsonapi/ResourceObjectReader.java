package eu.sportsfusion.common.jsonapi;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

import eu.sportsfusion.common.StringUtils;
import eu.sportsfusion.common.jsonsupport.BadJsonException;

/**
 * @author Anatoli Pranovich
 */
public class ResourceObjectReader {

    private String id;
    private String type;
    private JsonObject attributesJson;
    private JsonObject relationshipsJson;

    public ResourceObjectReader(JsonObject dataJson) {
        type = dataJson.getString("type", null);

        if (StringUtils.isBlank(type)) {
            throw new BadJsonException();
        }
        id = dataJson.getString("id", null);
        attributesJson = dataJson.getJsonObject("attributes");
        relationshipsJson = dataJson.getJsonObject("relationships");
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public JsonObject getAttributes() {
        return attributesJson;
    }

    public JsonObject getRelationships() {
        return relationshipsJson;
    }

    public List<String> getRelationshipIds(String name) {
        if (containsRelationship(name)) {
            JsonObject relationshipJson = relationshipsJson.getJsonObject(name);

            if (!relationshipJson.containsKey("data")) {
                throw new BadJsonException();
            }

            List<String> ids = new ArrayList<>();

            JsonArray relationshipDataJson = relationshipJson.getJsonArray("data");

            for (int i = 0; i < relationshipDataJson.size(); i++) {
                String id = new ResourceIdentifierObjectReader(relationshipDataJson.getJsonObject(i)).getId();

                if (!ids.contains(id)) {
                    ids.add(id);
                }
            }

            return ids;
        }
        return null;
    }

    public String getRelationshipId(String name) {
        if (containsRelationship(name)) {
            JsonObject relationshipJson = relationshipsJson.getJsonObject(name);

            if (!relationshipJson.containsKey("data")) {
                throw new BadJsonException();
            }

            if (!relationshipJson.isNull("data")) {
                return new ResourceIdentifierObjectReader(relationshipJson.getJsonObject("data")).getId();
            }
        }
        return null;
    }

    public boolean containsRelationship(String name) {
        return relationshipsJson != null && relationshipsJson.containsKey(name);
    }

    public ResourceIdentifierObjectReader getRelationship(String name) {
        if (containsRelationship(name)) {
            JsonObject relationshipJson = relationshipsJson.getJsonObject(name);

            if (!relationshipJson.containsKey("data")) {
                throw new BadJsonException();
            }

            if (!relationshipJson.isNull("data")) {
                return new ResourceIdentifierObjectReader(relationshipJson.getJsonObject("data"));
            }
        }
        return null;
    }
}
