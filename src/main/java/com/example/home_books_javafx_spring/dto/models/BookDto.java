package com.example.home_books_javafx_spring.dto.models;

import com.example.home_books_javafx_spring.validator.WrongNameChoiceString;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookDto implements EntityDto {

    private Integer id;

    @NotEmpty(message = "Title can't be empty")
    @Size(min = 2, max = 40, message = "Title should have from 2 to 40 letters")
    private String name;

    @NotNull(message = "You have to choose Author")
    @WrongNameChoiceString(message = "You have to choose Author")
    private AuthorDto authorDto;

    @NotNull(message = "You have to choose Publisher")
    @WrongNameChoiceString(message = "You have to choose Publisher")
    private PublisherDto publisherDto;

    @NotNull(message = "Book need Status")
    private StatusDto statusDto;

    @NotNull(message = "You have to choose Shelf")
    private ShelfDto shelfDto;
}
