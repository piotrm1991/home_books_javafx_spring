package com.example.home_books_javafx_spring.database.repository;


import com.example.home_books_javafx_spring.database.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Collection<Room> findRoomByName(String name);

    Room findRoomById(Integer id);
}
