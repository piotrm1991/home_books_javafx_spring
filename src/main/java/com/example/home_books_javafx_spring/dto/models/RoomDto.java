package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoomDto implements EntityDto {

    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 40, message = "Room Name should be from 2 to 40 letters")
    private String name;

    private Integer nBooks;

    private Integer nShelves;
}