package com.example.home_books_javafx_spring.dto.models;

import com.example.home_books_javafx_spring.validator.NoRepeatsPublisher;
import com.example.home_books_javafx_spring.validator.WrongChoiceString;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PublisherDto implements EntityDto {

    private Integer id;

    @NotEmpty
    @Size(min = 3, max = 40, message = "Publisher name have be from 3 to 40 letters")
    @WrongChoiceString(message = "Prohibited name for Publisher")
    @NoRepeatsPublisher(message = "Publisher already exists")
    private String name;

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
        PublisherDto that = (PublisherDto) o;
        return Objects.equals(id, that.id)
               && Objects.equals(name, that.name)
               && Objects.equals(nBooks, that.nBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nBooks);
    }
}
