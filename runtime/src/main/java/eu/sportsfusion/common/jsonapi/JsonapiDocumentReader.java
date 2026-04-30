package eu.sportsfusion.common.jsonapi;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.util.List;

import eu.sportsfusion.common.jsonsupport.BadJsonException;

/**
 * @author Anatoli Pranovich
 */
public class JsonapiDocumentReader {

    private ResourceObjectReader resourceObjectReader;
    private JsonObject documentJson;


    public JsonapiDocumentReader(JsonObject jsonObject) {
        documentJson = jsonObject;
        JsonObject dataJson = documentJson.getJsonObject("data");

        if (dataJson == null) {
            throw new BadJsonException();
        }

        resourceObjectReader = new ResourceObjectReader(dataJson);
    }

    public String getId() {
        return resourceObjectReader.getId();
    }

    public JsonObject getAttributes() {
        return resourceObjectReader.getAttributes();
    }

    public JsonObject getRelationships() {
        return resourceObjectReader.getRelationships();
    }

    public List<String> getRelationshipIds(String name) {
        return resourceObjectReader.getRelationshipIds(name);
    }

    public String getRelationshipId(String name) {
        return resourceObjectReader.getRelationshipId(name);
    }

    public boolean containsRelationship(String name) {
        return resourceObjectReader.containsRelationship(name);
    }

    public JsonArray getIncluded() {
        return documentJson.getJsonArray("included");
    }

    public JsonObject getMeta() {
        return documentJson.getJsonObject("meta");
    }

    public ResourceObjectReader getIncludedResource(String id, String type) {
        JsonArray included = getIncluded();

        if (included != null) {

            for (int i = 0; i < included.size(); i++) {
                ResourceObjectReader includedReader = new ResourceObjectReader(included.getJsonObject(i));

                if (includedReader.getId().equals(id) && includedReader.getType().equals(type)) {
                    return includedReader;
                }
            }
        }
        return null;
    }

    public ResourceObjectReader getIncludedResource(String relationshipName) {
        ResourceIdentifierObjectReader reader = resourceObjectReader.getRelationship(relationshipName);

        if (reader != null) {
            return getIncludedResource(reader.getId(), reader.getType());
        }
        return null;
    }

}
