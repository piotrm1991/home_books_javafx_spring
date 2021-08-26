package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.database.entities.Book;
import com.example.home_books_javafx_spring.database.repository.BookRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    DtoMapper dtoMapper;

    @Transactional
    public void addBook(Book book) {
        this.entityManager.merge(book);
    }

    @Transactional
    public void addBook(BookDto bookDto) {
        Book book = this.dtoMapper.fromBookDto(bookDto);
        this.entityManager.merge(book);
    }

    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    public List<Book> getBooksByAuthorId(Integer id) {
        return (List<Book>) this.bookRepository.findByAuthorId(id);
    }

    public List<BookDto> getBooksDtoByAuthorId(Integer id) {
        List<Book> books = this.bookRepository.findByAuthorId(id);
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public List<Book> getBooksByPublisherId(Integer id) {
        return (List<Book>) this.bookRepository.findByPublisherId(id);
    }

    public List<Book> getBooksByRoomId(Integer id) {
        return (List<Book>) this.bookRepository.findByShelfRoomId(id);
    }

    public List<Book> getBooksByShelfId(Integer id) {
        return (List<Book>) this.bookRepository.findByShelfId(id);
    }

    public void deleteBook(Integer id) {
        this.bookRepository.deleteById(id);
    }

    public Book getBookById(Integer id) {
        return this.bookRepository.findById(id).get();
    }

    public BookDto getBookDtoById(Integer id) {
        Book book = this.bookRepository.findById(id).get();
        BookDto bookDto = this.dtoMapper.fromBook(book);
        return bookDto;
    }

    public List<BookDto> getBooksDtoByStatusTypeId(Integer id) {
        List<Book> books = this.bookRepository.findByStatusStatusTypeId(id);
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public List<BookDto> getBooksDtoByRoomId(Integer id) {
        List<Book> books = this.bookRepository.findByShelfRoomId(id);
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public List<BookDto> getBooksDtoByShelfId(Integer id) {
        List<Book> books = this.bookRepository.findByShelfId(id);
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public List<BookDto> getBooksDtoByPublisherId(Integer id) {
        List<Book> books = this.bookRepository.findByPublisherId(id);
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public List<BookDto> getAllBooksDto() {
        List<Book> books = this.bookRepository.findAll();
        List<BookDto> booksDto = new ArrayList<>();
        books.stream().forEach(book -> booksDto.add(this.dtoMapper.fromBook(book)));
        return booksDto;
    }

    public Integer countBooksByRoomId(Integer id) {
        return this.bookRepository.findByShelfRoomId(id).size();
    }

    public Integer countBooksByStatusTypeId(Integer id) {
        return this.bookRepository.findByStatusStatusTypeId(id).size();
    }

    public Integer countBooksByShelfId(Integer id) {
        return this.bookRepository.findByShelfId(id).size();
    }

    public Integer countBooksByPublisherId(Integer id) {
        return this.bookRepository.findByPublisherId(id).size();
    }

    public Integer countBooksByAuthorId(Integer id) {
        return this.bookRepository.findByAuthorId(id).size();
    }
}
