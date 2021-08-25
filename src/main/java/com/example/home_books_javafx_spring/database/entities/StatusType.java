package com.example.home_books_javafx_spring.database.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name = "StatusType")
@Table(name = "STATUS_TYPE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_type")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 40, message = "Status Type Name should be from 2 to 40 letters")
    private String name;

    public StatusType(String name) {
        this.name = name;
    }
}