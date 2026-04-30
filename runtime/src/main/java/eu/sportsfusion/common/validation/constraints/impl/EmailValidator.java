package eu.sportsfusion.common.validation.constraints.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.sportsfusion.common.StringUtils;
import eu.sportsfusion.common.validation.constraints.Email;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * @author Anatoli Pranovich
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

    private Pattern pattern;

    public void initialize(Email annotation) {
        pattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
                + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", CASE_INSENSITIVE);
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
