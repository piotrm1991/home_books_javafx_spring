package com.example.home_books_javafx_spring.database.repository;

import com.example.home_books_javafx_spring.database.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}
