package com.example.home_books_javafx_spring.validator;

import com.example.home_books_javafx_spring.database.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class NoRepeatsAuthorValidator implements ConstraintValidator<NoRepeatsAuthor, String> {

    @Autowired
    AuthorService authorService;

    @Override
    public void initialize(NoRepeatsAuthor noRepeatsAuthor) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.authorService.getAllAuthorNames().stream().filter(a -> a.equals(value)).findFirst().isEmpty();
    }
}