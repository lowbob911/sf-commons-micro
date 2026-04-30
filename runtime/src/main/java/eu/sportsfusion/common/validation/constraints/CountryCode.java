package eu.sportsfusion.common.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import eu.sportsfusion.common.validation.constraints.impl.CountryCodeValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface CountryCode {

    String message() default "{eu.sportsfusion.validation.error.CountryCode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
