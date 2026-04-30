package eu.sportsfusion.common.validation.constraints.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static eu.sportsfusion.common.ReflectionUtils.findField;

import java.lang.reflect.Field;
import java.time.Instant;

import eu.sportsfusion.common.validation.constraints.DateRange;

/**
 * @author Anatoli Pranovich
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startDateField;
    private String endDateField;

    public void initialize(DateRange constraintAnnotation) {
        startDateField = constraintAnnotation.startDateField();
        endDateField = constraintAnnotation.endDateField();
    }

    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(constraintContext.getDefaultConstraintMessageTemplate()).
                addPropertyNode(startDateField).addConstraintViolation();
        try {

            Instant startDate = (Instant) getFieldValue(object, startDateField);
            Instant endDate = (Instant) getFieldValue(object, endDateField);

            return startDate == null || endDate == null || startDate.isBefore(endDate);

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
