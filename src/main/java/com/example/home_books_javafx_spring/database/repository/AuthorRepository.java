package com.example.home_books_javafx_spring.database.repository;


import com.example.home_books_javafx_spring.database.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Collection<Author> findAuthorsByFirstName(String firstName);

    Collection<Author> findAuthorsByLastName(String lastName);

    Collection<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
