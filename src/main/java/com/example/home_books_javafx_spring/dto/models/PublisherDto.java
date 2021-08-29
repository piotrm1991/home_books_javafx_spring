package com.example.home_books_javafx_spring.dto.models;

import com.example.home_books_javafx_spring.validator.NoRepeatsPublisher;
import com.example.home_books_javafx_spring.validator.WrongChoiceString;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PublisherDto implements EntityDto {

    private Integer id;

    @NotEmpty
    @Size(min = 3, max = 40, message = "Publisher name have be from 3 to 40 letters")
    @WrongChoiceString(message = "Prohibited name for Publisher")
    @NoRepeatsPublisher(message = "Publisher already exists")
    private String name;

    private Integer nBooks;

    @Override
    public PublisherDto getPublisherDto() {
        return null;
    }

    @Override
    public AuthorDto getAuthorDto() {
        return null;
    }

    @Override
    public ShelfDto getShelfDto() {
        return null;
    }

    @Override
    public StatusDto getStatusDto() {
        return null;
    }
}
