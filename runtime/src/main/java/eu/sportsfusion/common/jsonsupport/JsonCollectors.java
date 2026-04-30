package eu.sportsfusion.common.jsonsupport;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import java.util.stream.Collector;

/**
 * @author Anatoli Pranovich
 */
public class JsonCollectors {

    public static Collector<JsonObject, ?, JsonArrayBuilder> toJsonObjectArray() {
        return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add,
                (left, right) -> {
                    left.add(right);
                    return left;
                });
    }

    public static  Collector<String, ?, JsonArrayBuilder> toJsonStringArray() {
        return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add,
                (left, right) -> {
                    left.add(right);
                    return left;
                });
    }
}
