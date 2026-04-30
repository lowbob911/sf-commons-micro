package eu.sportsfusion.common.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import eu.sportsfusion.common.validation.constraints.impl.UniquePropertyValidator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@Constraint(validatedBy = UniquePropertyValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface UniqueProperty {

    String message() default "{eu.sportsfusion.validation.error.Unique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}
