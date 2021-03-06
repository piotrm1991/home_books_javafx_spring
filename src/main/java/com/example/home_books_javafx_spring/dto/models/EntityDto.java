package com.example.home_books_javafx_spring.dto.models;

public interface EntityDto {
    Integer getId();
    String getName();
    PublisherDto getPublisherDto();

    AuthorDto getAuthorDto();

    ShelfDto getShelfDto();

    StatusDto getStatusDto();
}
