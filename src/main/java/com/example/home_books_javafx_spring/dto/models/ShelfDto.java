package com.example.home_books_javafx_spring.dto.models;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ShelfDto implements EntityDto {

    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 3, message = "From 1 To 3")
    private String letter;

    @Range(min = 1, max = 40, message = "From 1 to 40")
    private Integer number;

    @NotNull(message = "You have to choose Room")
    private RoomDto roomDto;

    private Integer nBooks;

    @Override
    public String getName() {
        /* Nothing to do here */
        return null;
    }

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
