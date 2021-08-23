package com.example.home_books_javafx_spring.database.repository;

import com.example.home_books_javafx_spring.database.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Collection<Publisher> findPublisherByName(String name);
}
