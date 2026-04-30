package eu.sportsfusion.common.jsonapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import eu.sportsfusion.common.ParamsMap;
import eu.sportsfusion.common.SortField;
import eu.sportsfusion.common.SortOrder;
import eu.sportsfusion.common.StringUtils;

/**
 * @author Anatoli Pranovich
 */
public class JsonapiParamsReader {

    private ParamsMap filterParams;
    private ParamsMap pageParams;
    private ParamsMap fieldsParams;
    private List<SortField> sortFields;
    private List<String> inclusions;

    public JsonapiParamsReader(Map<String, List<String>> params) {
        filterParams = getBracketedParams("filter", params);
        pageParams = getBracketedParams("page", params);
        fieldsParams = getBracketedParams("fields", params);

        String rawIncludeParam = getFirstParam("include", params);
        inclusions = new ArrayList<>();

        if (StringUtils.isNotBlank(rawIncludeParam)) {
            inclusions.addAll(Arrays.asList(rawIncludeParam.split(",")));
        }

        String rawSortParam = getFirstParam("sort", params);
        sortFields = new ArrayList<>();

        if (StringUtils.isNotBlank(rawSortParam)) {
            String[] sortParams = rawSortParam.split(",");

            for (String sortParam : sortParams) {
                String fieldName = (sortParam.startsWith("+") || sortParam.startsWith("-"))
                        ? sortParam.substring(1, sortParam.length()) : sortParam;

                sortFields.add(new SortField(fieldName, sortParam.startsWith("-") ? SortOrder.DESC : SortOrder.ASC));
            }
        }
    }

    private ParamsMap getBracketedParams(String baseName, Map<String, List<String>> params) {
        ParamsMap bp = new ParamsMap();

        params.forEach((k, v) -> {
            if (k.startsWith(baseName + "[") && k.endsWith("]")) {
                bp.put(k.substring(k.indexOf("[") + 1, k.indexOf("]")), v.get(0));
            }
        });

        return bp;
    }

    private String getFirstParam(String key, Map<String, List<String>> params) {
        List<String> values = params.get(key);
        if (values != null && values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    public ParamsMap getFilterParams() {
        return filterParams;
    }

    public ParamsMap getPageParams() {
        return pageParams;
    }

    public ParamsMap getFieldsParams() {
        return fieldsParams;
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public List<String> getInclusions() {
        return inclusions;
    }
}
