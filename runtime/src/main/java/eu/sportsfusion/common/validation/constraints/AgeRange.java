package eu.sportsfusion.common.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import eu.sportsfusion.common.validation.constraints.impl.AgeRangeValidator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface AgeRange {

    String message() default "{eu.sportsfusion.validation.error.AgeRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String ageFromField();
    String ageToField();
}

