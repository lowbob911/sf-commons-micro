package eu.sportsfusion.common.validation.constraints.impl;

import eu.sportsfusion.common.validation.constraints.UniqueProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Igor Vashyst
 */
public class UniquePropertyValidatorTest {

    private UniquePropertyValidator validator;

    @BeforeEach
    public void init() {
        validator = new UniquePropertyValidator();
        validator.em = mock(EntityManager.class);

        Query mockedQuery = mock(Query.class);
        when(validator.em.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
    }

    @Test
    public void shouldLookupSuperclassFields() {
        //
        // Given
        //
        class Parent {
            private Long id;
            private String email;
        }

        @UniqueProperty("email")
        class Child extends Parent {}


        //
        // When
        //
        validator.initialize(Child.class.getAnnotation(UniqueProperty.class));

        boolean isValid = validator.isValid(new Child(), mockConstraintValidatorContext());


        //
        // Then
        //
        assertTrue(isValid);
    }

    private ConstraintValidatorContext mockConstraintValidatorContext() {
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(violationBuilder.addPropertyNode(any())).thenReturn(nodeBuilder);

        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        when(validatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

        return validatorContext;
    }
}