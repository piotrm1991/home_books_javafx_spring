package com.example.home_books_javafx_spring.validator;

import com.example.home_books_javafx_spring.config.FieldsConfig;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class WrongChoiceStringValidator implements ConstraintValidator<WrongChoiceString, String> {

    List<String> notAcceptedStrings = new ArrayList<>();

    @Override
    public void initialize(WrongChoiceString WrongChoiceString) {
        this.notAcceptedStrings = FieldsConfig.STRING_FOR_CHOOSING;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.notAcceptedStrings.stream().filter(s -> s.equals(value)).findFirst().isEmpty();
    }
}
