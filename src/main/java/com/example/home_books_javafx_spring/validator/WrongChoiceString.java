package com.example.home_books_javafx_spring.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = WrongChoiceStringValidator.class)
public @interface WrongChoiceString {

    String message() default "You have to choose";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

