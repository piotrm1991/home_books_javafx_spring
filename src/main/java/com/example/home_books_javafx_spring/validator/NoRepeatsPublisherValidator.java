package com.example.home_books_javafx_spring.validator;

import com.example.home_books_javafx_spring.database.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class NoRepeatsPublisherValidator implements ConstraintValidator<NoRepeatsPublisher, String> {

    @Autowired
    PublisherService publisherService;

    @Override
    public void initialize(NoRepeatsPublisher noRepeatsPublisher) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.publisherService.getAllPublisherNames().stream().filter(a -> a.equals(value)).findFirst().isEmpty();
    }
}