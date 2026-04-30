package eu.sportsfusion.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Igor Vashyst
 */
public class ReflectionUtilsTest {

    private static final String FIELD_NAME = "id";

    @Test
    public void shouldFindFieldInCurrentClass() {
        class Child {
            private Long id;
        }

        Field field = ReflectionUtils.findField(Child.class, FIELD_NAME);

        assertNotNull(field);
        assertThat(field.getName(), is(FIELD_NAME));
    }

    @Test
    public void shouldFindFieldInParentClass() {
        class Parent {
            private Long id;
        }
        class Child extends Parent {}

        Field field = ReflectionUtils.findField(Child.class, FIELD_NAME);

        assertNotNull(field);
        assertThat(field.getName(), is(FIELD_NAME));
    }

    @Test
    public void shouldReturnNullWhenNotFound() {
        class Child {}

        Field field = ReflectionUtils.findField(Child.class, FIELD_NAME);

        assertNull(field);
    }
}