package com.example.home_books_javafx_spring.database.repository;

import com.example.home_books_javafx_spring.database.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByAuthorId(Integer idAuthor);

    List<Book> findByPublisherId(Integer idPublisher);

    List<Book> findByShelfRoomId(Integer idRoom);

    List<Book> findByShelfId(Integer idShelf);

    Optional<Book> findByStatusId(Integer idStatus);

    List<Book> findByStatusStatusTypeId(Integer idStatusType);

    Collection<Book> findBookByName(String name);
}
