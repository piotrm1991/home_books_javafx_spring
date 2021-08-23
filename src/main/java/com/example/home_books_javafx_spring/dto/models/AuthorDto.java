package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AuthorDto implements EntityDto {

    private Integer id;

    @NotEmpty(message = "Author needs First Name")
    @Size(min = 2, max = 40, message = "First Name should be from 2 to 40 letters")
    private String firstName;

    @NotEmpty(message = "Author needs Last Name")
    @Size(min = 2, max = 40, message = "Last Name should be from 2 to 40 letters")
    private String lastName;

    private Integer nBooks;
}
