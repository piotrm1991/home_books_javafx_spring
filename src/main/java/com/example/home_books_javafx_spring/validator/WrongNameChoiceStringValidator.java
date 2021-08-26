package com.example.home_books_javafx_spring.validator;

import com.example.home_books_javafx_spring.config.FieldsConfig;
import com.example.home_books_javafx_spring.dto.models.EntityDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class WrongNameChoiceStringValidator implements ConstraintValidator<WrongNameChoiceString, EntityDto> {

    List<String> notAcceptedStrings = new ArrayList<>();

    @Override
    public void initialize(WrongNameChoiceString wrongNameChoiceString) {
        this.notAcceptedStrings = FieldsConfig.STRING_FOR_CHOOSING;
    }

    @Override
    public boolean isValid(EntityDto value, ConstraintValidatorContext context) {
        return this.notAcceptedStrings.stream().filter(s -> s.equals(value.getName())).findFirst().isEmpty();
    }
}
