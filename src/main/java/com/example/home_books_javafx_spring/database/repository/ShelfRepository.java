package com.example.home_books_javafx_spring.database.repository;

import com.example.home_books_javafx_spring.database.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Integer> {
    List<Shelf> findByRoomId(Integer idRoom);

    Collection<Shelf> findShelfByLetter(String letter);

    Collection<Shelf> findShelfByNumber(Integer number);
}
