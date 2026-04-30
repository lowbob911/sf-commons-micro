package eu.sportsfusion.common.validation.constraints.impl;

import eu.sportsfusion.common.validation.constraints.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Anatoli Pranovich
 */
public class GenderValidator implements ConstraintValidator<Gender, Object> {

    @Override
    public void initialize(Gender constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || "F".equals(value) || "M".equals(value);

    }
}
