package eu.sportsfusion.common.configuration.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

import eu.sportsfusion.common.configuration.entity.ConfigurationProperty;

/**
 * @author Anatoli Pranovich
 */
@Vetoed
@ApplicationScoped
public class ConfigurationService {

    @PersistenceContext
    private EntityManager em;

    public List<ConfigurationProperty> getConfigurationProperties(){
        return em.createNamedQuery(ConfigurationProperty.findAll, ConfigurationProperty.class).getResultList();
    }

    public ConfigurationProperty getConfigurationProperty(String propertyName){
        List<ConfigurationProperty> props =
                em.createNamedQuery(ConfigurationProperty.findByName, ConfigurationProperty.class).
                        setParameter("name", propertyName).getResultList();

        if (props.isEmpty()) {
            return null;
        }
        return props.iterator().next();
    }

    @Transactional
    public void setConfigurationProperty(String propertyName, String propertyValue) {
        ConfigurationProperty configurationProperty = getConfigurationProperty(propertyName);

        if (configurationProperty == null) {
            configurationProperty = new ConfigurationProperty();
            configurationProperty.setPropertyName(propertyName);
        }

        configurationProperty.setPropertyValue(propertyValue);
        em.merge(configurationProperty);
    }
}
