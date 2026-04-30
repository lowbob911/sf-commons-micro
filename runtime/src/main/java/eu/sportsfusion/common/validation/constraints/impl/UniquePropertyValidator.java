package eu.sportsfusion.common.validation.constraints.impl;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static eu.sportsfusion.common.ReflectionUtils.findField;

import java.lang.reflect.Field;

import eu.sportsfusion.common.validation.constraints.UniqueProperty;

/**
 * @author Anatoli Pranovich
 */
@Vetoed
@Dependent
public class UniquePropertyValidator implements ConstraintValidator<UniqueProperty, Object> {

    @PersistenceContext
    EntityManager em;

    private String fieldName;

    public void initialize(UniqueProperty constraintAnnotation) {
        fieldName = constraintAnnotation.value();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(constraintContext.getDefaultConstraintMessageTemplate()).
                addPropertyNode(fieldName).addConstraintViolation();

        try {
            Object id = getFieldValue(object, "id");
            boolean hasDeleted = findField(object.getClass(), "deleted") != null;

            String jql = "select e from " + object.getClass().getSimpleName() + " e where e." + fieldName + "= :p";

            if (id != null) {
                jql += " and e.id != :id";
            }

            if (hasDeleted) {
                jql += " and e.deleted is null";
            }

            Query query = em.createQuery(jql).setParameter("p", getFieldValue(object, fieldName));

            if (id != null) {
                query = query.setParameter("id", id);
            }

            return query.getResultList().isEmpty();
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
