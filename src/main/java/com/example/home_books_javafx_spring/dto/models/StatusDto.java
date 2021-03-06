package com.example.home_books_javafx_spring.dto.models;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StatusDto implements EntityDto {

    private Integer id;

    @NotNull
    private StatusTypeDto statusTypeDto;

    private Date dateUp;

    private String comment;

    @Override
    public String getName() {
        /* Nothing to do here */
        return null;
    }

    @Override
    public PublisherDto getPublisherDto() {
        return null;
    }

    @Override
    public AuthorDto getAuthorDto() {
        return null;
    }

    @Override
    public ShelfDto getShelfDto() {
        return null;
    }

    @Override
    public StatusDto getStatusDto() {
        return null;
    }
}
