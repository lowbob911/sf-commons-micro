package eu.sportsfusion.common.jsonapi;

import jakarta.json.*;

/**
 * @author Anatoli Pranovich
 */
public class JsonapiDocumentBuilder {

    private JsonStructure primaryDataJson;
    private JsonObject metaDataJson;
    private JsonObject linksDataJson;
    private JsonArray includedData;
    private JsonArray errorsDataJson;

    public JsonapiDocumentBuilder addPrimaryData(JsonStructure primaryDataJson) {
        this.primaryDataJson = primaryDataJson;
        return this;
    }

    public JsonapiDocumentBuilder addMetaData(JsonObject metaDataJson) {
        this.metaDataJson = metaDataJson;
        return this;
    }

    public JsonapiDocumentBuilder addLinksData(JsonObject linksDataJson) {
        this.linksDataJson = linksDataJson;
        return this;
    }

    public JsonapiDocumentBuilder addIncludedData(JsonArray includedData) {
        this.includedData = includedData;
        return this;
    }

    public JsonapiDocumentBuilder addErrorsData(JsonArray errorsData) {
        this.errorsDataJson = errorsDataJson;
        return this;
    }

    public JsonObject build() {
        if (metaDataJson == null && primaryDataJson == null && errorsDataJson == null) {
            throw new IllegalStateException("A document MUST contain at least one of the following top-level members:\n" +
                    "\n" +
                    "data: the document’s “primary data”\n" +
                    "errors: an array of error objects\n" +
                    "meta: a meta object that contains non-standard meta-information.");
        }

        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("jsonapi", Json.createObjectBuilder().add("version", "1.0"));

        if (metaDataJson != null) {
            builder.add("meta", metaDataJson);
        }

        if (primaryDataJson != null) {
            builder.add("data", primaryDataJson);
        }

        if (includedData != null) {
            builder.add("included", includedData);
        }

        if (linksDataJson != null) {
            builder.add("links", linksDataJson);
        }

        return builder.build();
    }

}
