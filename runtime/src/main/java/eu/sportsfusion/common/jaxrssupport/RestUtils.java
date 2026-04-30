package eu.sportsfusion.common.jaxrssupport;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

import eu.sportsfusion.common.ParamsMap;
import eu.sportsfusion.common.jsonsupport.CustomJsonObjectBuilder;
import eu.sportsfusion.common.jsonsupport.JsonUtils;

/**
 * @author Anatoli Pranovich
 */
public class RestUtils {

    public static Response createdResponse(JsonObject createdJson, UriInfo info) {
        URI uri = info.getAbsolutePathBuilder().path("/" + createdJson.getJsonObject("data").getString("id")).build();
        return Response.created(uri).entity(createdJson).build();
    }

    public static ParamsMap getBracketedParams(String baseName, UriInfo info) {
        ParamsMap bp = new ParamsMap();

        info.getQueryParameters().forEach((k, v) -> {
            if (k.startsWith(baseName + "[") && k.endsWith("]")) {
                bp.put(k.substring(k.indexOf("[") + 1, k.indexOf("]")), v.get(0));
            }
        });

        return bp;
    }

    public static JsonObject addNextPrevLinks(JsonObject jsonObject, UriInfo info) {
        JsonObject resultJson = jsonObject;

        JsonObject pageJson = jsonObject.getJsonObject("meta").getJsonObject("page");
        boolean isFirst = pageJson.getBoolean("first");
        boolean isLast = pageJson.getBoolean("last");
        int pageNumber = pageJson.getInt("number");

        if (!(isFirst && isLast)) {
            UriBuilder uriBuilder = info.getRequestUriBuilder();
            JsonObjectBuilder linksBuilder = Json.createObjectBuilder();

            if (!isFirst) {
                linksBuilder.add("prev", uriBuilder.replaceQueryParam("page[number]", pageNumber - 1).build().toString());
            }

            if (!isLast) {
                linksBuilder.add("next", uriBuilder.replaceQueryParam("page[number]", pageNumber + 1).build().toString());
            }

            CustomJsonObjectBuilder pageBuilder = JsonUtils.convertToBuilder(jsonObject);
            resultJson = pageBuilder.add("links", linksBuilder.build()).build();
        }

        return resultJson;
    }
}
