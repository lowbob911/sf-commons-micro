package eu.sportsfusion.common.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anatoli Pranovich
 */
@ReportAsSingleViolation
@Pattern(regexp = "^([a-z0-9]+(-[a-z0-9]+)*)$")
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DomainNamePart {

    String message() default "{eu.sportsfusion.validation.error.DomainNamePart.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
