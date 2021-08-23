//package com.example.home_books_javafx_spring.database.service;
//
//import com.example.home_books_javafx_spring.database.entities.Book;
//import com.example.home_books_javafx_spring.database.repository.BookRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//@Service
//public class BookService {
//
//    @Autowired
//    BookRepository bookRepository;
//
//    @Autowired
//    EntityManager entityManager;
//
//    @Transactional
//    public void addBook(Book book) {
//        System.out.println(book);
//        this.entityManager.merge(book);
//    }
//
//    public List<Book> getAllBooks() {
//        return this.bookRepository.findAll();
//    }
//
//    public List<Book> getBooksByAuthorId(Integer id) {
//        return (List<Book>) this.bookRepository.findByAuthorId(id);
//    }
//
//    public List<Book> getBooksByPublisherId(Integer id) {
//        return (List<Book>) this.bookRepository.findByPublisherId(id);
//    }
//
//    public List<Book> getBooksByRoomId(Integer id) {
//        return (List<Book>) this.bookRepository.findByShelfRoomId(id);
//    }
//
//    public List<Book> getBooksByShelfId(Integer id) {
//        return (List<Book>) this.bookRepository.findByShelfId(id);
//    }
//
//    public void deleteBook(Integer id) {
//        this.bookRepository.deleteById(id);
//    }
//
//    public Book getBookById(Integer id) {
//        return this.bookRepository.findById(id).get();
//    }
//}
