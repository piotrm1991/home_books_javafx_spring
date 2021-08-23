package com.example.home_books_javafx_spring.database.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @Column(name = "first_name")
    @NotEmpty
    @Size(min = 2, max = 40)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Size(min = 2, max = 40)
    private String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
