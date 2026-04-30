package eu.sportsfusion.common.validation.constraints.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;

import eu.sportsfusion.common.validation.constraints.CountryCode;

/**
 * @author Anatoli Pranovich
 */
public class CountryCodeValidator implements ConstraintValidator<CountryCode, Object> {

    @Override
    public void initialize(CountryCode constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String[] codes = Locale.getISOCountries();

        for (String code : codes) {
            if (code.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
