package eu.sportsfusion.common.jsonapi;

/**
 * @author Anatoli Pranovich
 */
public interface FieldsMapper {

    String getJsonPointer(String field);

    default String getAttributesPrefix() {
        return "/data/attributes/";
    }
}
