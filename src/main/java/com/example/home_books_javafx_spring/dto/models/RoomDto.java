package com.example.home_books_javafx_spring.dto.models;

import com.example.home_books_javafx_spring.validator.WrongChoiceString;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RoomDto implements EntityDto {

    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 40, message = "Room Name should be from 2 to 40 letters")
    @WrongChoiceString(message = "Prohibited name for Room")
    private String name;

    private Integer nBooks;

    private Integer nShelves;
}