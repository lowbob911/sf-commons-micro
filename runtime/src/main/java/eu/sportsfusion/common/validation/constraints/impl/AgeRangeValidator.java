package eu.sportsfusion.common.validation.constraints.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static eu.sportsfusion.common.ReflectionUtils.findField;

import java.lang.reflect.Field;

import eu.sportsfusion.common.validation.constraints.AgeRange;

/**
 * @author Anatoli Pranovich
 */
public class AgeRangeValidator implements ConstraintValidator<AgeRange, Object> {
    private String ageFromField;
    private String ageToField;

    public void initialize(AgeRange constraintAnnotation) {
        ageFromField = constraintAnnotation.ageFromField();
        ageToField = constraintAnnotation.ageToField();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(constraintContext.getDefaultConstraintMessageTemplate()).
                addPropertyNode(ageFromField).addConstraintViolation();
        try {

            Integer ageFrom = (Integer) getFieldValue(object, ageFromField);
            Integer ageTo = (Integer) getFieldValue(object, ageToField);

            return ageFrom == null || ageTo == null || ageFrom <= ageTo;

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValue(Object object, String field) throws IllegalAccessException, NoSuchFieldException {
        Field valueField = findField(object.getClass(), field);
        if (valueField == null) {
            throw new NoSuchFieldException(field);
        }

        valueField.setAccessible(true);
        return valueField.get(object);
    }
}
