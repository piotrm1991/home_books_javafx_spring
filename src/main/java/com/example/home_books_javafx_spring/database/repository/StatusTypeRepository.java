package com.example.home_books_javafx_spring.database.repository;

import com.example.home_books_javafx_spring.database.entities.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StatusTypeRepository extends JpaRepository<StatusType, Integer> {
    Collection<StatusType> findStatusTypeByName(String name);
}
