package com.example.home_books_javafx_spring.database.entities;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name = "Shelf")
@Table(name = "SHELF")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shelf")
    private Integer id;

    @Column(name = "letter")
    @NotEmpty
    @Size(min = 1, max = 3, message = "From 1 To 3")
    private String letter;

    @Column(name = "number")
    @Range(min = 1, max = 40, message = "From 1 to 40")
    private Integer number;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_room")
    private Room room;

    public Shelf(String letter, Integer number) {
        this.letter = letter;
        this.number = number;
    }
}
