package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AuthorDto implements EntityDto {

    private Integer id;

    @NotEmpty(message = "Author needs First Name")
    @Size(min = 2, max = 40, message = "First Name should be from 2 to 40 letters")
    private String firstName;

    @NotEmpty(message = "Author needs Last Name")
    @Size(min = 2, max = 40, message = "Last Name should be from 2 to 40 letters")
    private String lastName;

    private Integer nBooks;

    @Override
    public boolean equals(Object o) {
        if (this
            == o) {
            return true;
        }
        if (o
            == null
            || getClass()
               != o.getClass()) {
            return false;
        }
        AuthorDto authorDto = (AuthorDto) o;
        return Objects.equals(id, authorDto.id)
               && Objects.equals(firstName, authorDto.firstName)
               && Objects.equals(lastName, authorDto.lastName)
               && Objects.equals(nBooks, authorDto.nBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, nBooks);
    }
}
