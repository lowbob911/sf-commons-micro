package eu.sportsfusion.common.configuration.control;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import eu.sportsfusion.common.configuration.entity.ConfigurationProperty;

/**
 * @author Anatoli Pranovich
 */
@ApplicationScoped
@Named("config")
public class Config implements Serializable {

    @Inject
    Instance<ConfigurationService> configurationService;

    ConfigurationService configurationServiceOverride;

    private Map<String, String> properties = null;

    @PostConstruct
    void createConfig() {
        properties = new TreeMap<>();

        ConfigurationService service = configurationServiceOverride;
        if (service == null && !configurationService.isUnsatisfied()) {
            service = configurationService.get();
        }

        if (service == null) {
            return;
        }

        List<ConfigurationProperty> props = service.getConfigurationProperties();

        for (ConfigurationProperty prop : props) {
            properties.put(prop.getPropertyName(), prop.getPropertyValue());
        }
    }

    public boolean isDevelopment() {
        return Boolean.valueOf(getProperty("development"));
    }

    public boolean isCORSEnabled() {
        return Boolean.valueOf(getProperty("cors"));
    }

    public String getCdnUrl() {
        return getProperty("aws.cloudfront.url");
    }

    public String getS3Bucket() {
        return getProperty("aws.s3.bucket");
    }

    /**
     * Returns the <code>String</code> value of the named property, or <code>null</code> if the value is not set.
     *
     * @param name the name of the property
     *
     * @return the <code>String</code> value of the named property, or <code>null</code> if the value is not set
     */
    public String getProperty(String name) {
        return properties.get(name);
    }

    /**
     * Returns the <code>String</code> value of the named property, or <code>defaultValue</code> if the value is not set.
     *
     * @param name the name of the property
     * @param defaultValue the default value
     *
     * @return the <code>String</code> value of the named property, or <code>defaultValue</code> if the value is not set
     */
    public String getProperty(String name, String defaultValue) {
        String value = getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
        ConfigurationService service = configurationServiceOverride;
        if (service == null && !configurationService.isUnsatisfied()) {
            service = configurationService.get();
        }
        if (service != null) {
            service.setConfigurationProperty(name, value);
        }
    }

    /**
     * Returns either the <code>Long</code> value of the named property, or <code>null</code> if the property value is not set.
     *
     * @param name the name of the property
     *
     * @return the <code>Long</code> value of the named property, or <code>null</code> if the value is not set
     */
    public Long getLongProperty(String name) {
        return getLongProperty(name, null);
    }

    /**
     * Returns either the <code>Long</code> value of the named property, or <code>defaultValue</code> if the property
     * value is not set.
     *
     * @param name the name of the property
     * @param defaultValue the default value
     *
     * @return the <code>Long</code> value of the named property, or <code>defaultValue</code> if the property value
     * is not set
     */
    public Long getLongProperty(String name, Long defaultValue) {
        String value = getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Long.valueOf(value);
    }

    /**
     * Returns either the <code>boolean</code> value of the named property, or <code>false</code> if the property value is not set.
     *
     * @param name the name of the property
     *
     * @return the <code>boolean</code> value of the named property, or <code>false</code> if the value is not set
     */
    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    /**
     * Returns either the <code>boolean</code> value of the named property, or <code>defaultValue</code> if the property
     * value is not set.
     *
     * @param name the name of the property
     * @param defaultValue the default value
     *
     * @return the <code>boolean</code> value of the named property, or <code>defaultValue</code> if the property value
     * is not set
     */
    public boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Boolean.valueOf(value);
    }

    public void reloadConfig() {
        createConfig();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
