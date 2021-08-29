package com.example.home_books_javafx_spring.dto.models;

import com.example.home_books_javafx_spring.validator.NoRepeatsAuthor;
import com.example.home_books_javafx_spring.validator.WrongChoiceString;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AuthorDto implements EntityDto {

    private Integer id;

    @NotEmpty(message = "Author needs Name")
    @Size(min = 3, max = 40, message = "First Name should be from 3 to 40 letters")
    @WrongChoiceString(message = "Prohibited name for Author")
    @NoRepeatsAuthor(message = "Author already exists")
    private String name;

    private Integer nBooks;
}
