package eu.sportsfusion.common.validation.constraints;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface UniqueProperties {
    UniqueProperty[] value ();
}
