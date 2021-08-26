package com.example.home_books_javafx_spring.validator;

import com.example.home_books_javafx_spring.config.FieldsConfig;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class NotThisStringValidator implements ConstraintValidator<NotThisString, String> {

    List<String> notAcceptedStrings = new ArrayList<>();

    @Override
    public void initialize(NotThisString NotThisString) {
        this.notAcceptedStrings = FieldsConfig.STRING_FOR_CHOOSING;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.notAcceptedStrings.stream().filter(s -> s.equals(value)).findFirst().isEmpty();
    }
}
