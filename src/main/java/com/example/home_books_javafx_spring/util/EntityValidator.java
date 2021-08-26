package com.example.home_books_javafx_spring.util;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class EntityValidator {
    ValidatorFactory factory;
    Validator validator;

    public EntityValidator() {
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public Set<ConstraintViolation<EntityDto>> validateEntity(EntityDto entityDto) {
        return validator.validate(entityDto);
    }
}
