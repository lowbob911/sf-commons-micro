package eu.sportsfusion.common.jsonapi;

import eu.sportsfusion.common.jsonsupport.BadJsonException;
import jakarta.json.JsonObject;

/**
 * @author Anatoli Pranovich
 */
public class ResourceIdentifierObjectReader {

    private String id;
    private String type;

    public ResourceIdentifierObjectReader(JsonObject jsonObject) {
        type = jsonObject.getString("type", null);

        if (type == null) {
            throw new BadJsonException();
        }

        id = jsonObject.getString("id", null);

        if (id == null) {
            throw new BadJsonException();
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
