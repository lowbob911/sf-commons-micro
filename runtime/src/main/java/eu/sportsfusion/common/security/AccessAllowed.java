package eu.sportsfusion.common.security;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface AccessAllowed {
    String[] value() default {};
}
