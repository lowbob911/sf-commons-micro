package eu.sportsfusion.common.audits.entity;

/**
 * Model object to describe actions for audits.
 * @author Created by Mikhail Lysenka on 30.03.16
 */
public class Action {
    private Long id;
    private String name;
    private String description;
    //private String scope;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
