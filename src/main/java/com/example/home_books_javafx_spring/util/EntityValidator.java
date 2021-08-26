package com.example.home_books_javafx_spring.util;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class EntityValidator {

    ValidatorFactory factory;
    Validator validator;

    public EntityValidator(ApplicationContext applicationContext) {
        this.factory =
                Validation.byProvider(HibernateValidator.class).configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(applicationContext.getAutowireCapableBeanFactory())).buildValidatorFactory();
        this.validator = factory.getValidator();
    }

    public Set<ConstraintViolation<EntityDto>> validateEntity(EntityDto entityDto) {
        System.out.println(entityDto.getClass());
        return validator.validate(entityDto);
    }
}
