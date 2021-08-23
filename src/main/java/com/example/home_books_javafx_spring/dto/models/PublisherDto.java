package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PublisherDto implements EntityDto {

    private Integer id;

    @NotEmpty
    @Size(min = 2, max = 40, message = "Publisher name should be from 2 to 40 letters")
    private String name;

    private Integer nBooks;
}
