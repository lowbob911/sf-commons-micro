package eu.sportsfusion.common;

/**
 * @author Anatoli Pranovich
 */
public class SortField {

    private String name;
    private SortOrder order;

    public SortField(String name, SortOrder order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public SortOrder getOrder() {
        return order;
    }
}
