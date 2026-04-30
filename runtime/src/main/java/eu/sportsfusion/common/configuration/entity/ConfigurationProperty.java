package eu.sportsfusion.common.configuration.entity;

import jakarta.persistence.*;

/**
 * @author Anatoli Pranovich
 */
@Entity
@Table(name = "configuration_properties")
@NamedQueries({
        @NamedQuery(name = ConfigurationProperty.findAll, query = "SELECT cp FROM ConfigurationProperty cp"),
        @NamedQuery(name = ConfigurationProperty.findByName, query = "SELECT cp FROM ConfigurationProperty cp where cp.propertyName=:name"),
})
public class ConfigurationProperty {

    private static final String PREFIX = "eu.sportsfusion.common.configuration.entity.ConfigurationProperty.";
    public static final String findAll = PREFIX + "all";
    public static final String findByName = PREFIX + "byName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyName;

    private String propertyValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ConfigurationProperty))
            return false;

        ConfigurationProperty that = (ConfigurationProperty) obj;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return propertyName != null ? propertyName.hashCode() : 0;
    }
}
