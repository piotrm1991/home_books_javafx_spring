package com.example.home_books_javafx_spring.database.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name = "Author")
@Table(name = "AUTHOR")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_author")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 3, max = 40)
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
