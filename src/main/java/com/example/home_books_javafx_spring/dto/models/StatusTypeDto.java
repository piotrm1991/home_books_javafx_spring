package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StatusTypeDto implements EntityDto {

    private Integer id;

    @NotEmpty
    @Size(min = 2, max = 40, message = "Status Type Name should be from 2 to 40 letters")
    private String name;

    private Integer nBooks;
}
