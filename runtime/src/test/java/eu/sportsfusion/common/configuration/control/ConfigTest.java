package eu.sportsfusion.common.configuration.control;

import eu.sportsfusion.common.configuration.entity.ConfigurationProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Igor Vashyst
 */
public class ConfigTest {

    private Config cut;

    @BeforeEach
    public void init() {
        List<ConfigurationProperty> properties = new ArrayList<>();
        properties.add(createProperty("string", "value"));
        properties.add(createProperty("long", "17"));
        properties.add(createProperty("boolean", "true"));

        ConfigurationService service = mock(ConfigurationService.class);
        when(service.getConfigurationProperties()).thenReturn(properties);

        cut = new Config();
        cut.configurationServiceOverride = service;
        cut.createConfig();
    }

    @Test
    public void getProperty_missingProperty_returnNull() {
        String value = cut.getProperty("missing");

        assertThat(value, is(nullValue()));
    }

    @Test
    public void getProperty_missingPropertyWithDefaultValue_returnDefaultValue() {
        String value = cut.getProperty("missing", "default");

        assertThat(value, is("default"));
    }

    @Test
    public void getProperty_presentProperty_returnValue() {
        String value = cut.getProperty("string");

        assertThat(value, is("value"));
    }

    @Test
    public void getLongProperty_missingProperty_returnNull() {
        Long value = cut.getLongProperty("missing");

        assertThat(value, is(nullValue()));
    }

    @Test
    public void getLongProperty_missingPropertyWithDefaultValue_returnDefaultValue() {
        Long value = cut.getLongProperty("missing", 7L);

        assertThat(value, is(7L));
    }

    @Test
    public void getLongProperty_presentProperty_returnValue() {
        Long value = cut.getLongProperty("long");

        assertThat(value, is(17L));
    }

    @Test
    public void getBooleanProperty_missingProperty_returnFalse() {
        Boolean value = cut.getBooleanProperty("missing");

        assertThat(value, is(false));
    }

    @Test
    public void getBooleanProperty_missingPropertyWithDefaultValue_returnDefaultValue() {
        Boolean value = cut.getBooleanProperty("missing", true);

        assertThat(value, is(true));
    }

    @Test
    public void getBooleanProperty_presentProperty_returnValue() {
        Boolean value = cut.getBooleanProperty("boolean");

        assertThat(value, is(true));
    }

    private ConfigurationProperty createProperty(String name, String value) {
        ConfigurationProperty result = new ConfigurationProperty();
        result.setPropertyName(name);
        result.setPropertyValue(value);
        return result;
    }
}