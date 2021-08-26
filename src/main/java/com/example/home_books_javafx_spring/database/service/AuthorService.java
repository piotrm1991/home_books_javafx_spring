package com.example.home_books_javafx_spring.database.service;


import com.example.home_books_javafx_spring.config.FieldsConfig;
import com.example.home_books_javafx_spring.database.entities.Author;
import com.example.home_books_javafx_spring.database.entities.Publisher;
import com.example.home_books_javafx_spring.database.repository.AuthorRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.home_books_javafx_spring.config.FieldsConfig.*;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    DtoMapper dtoMapper;

    private Integer keyHolder;

//    @Autowired
//    BookRepository bookRepository;

    @Transactional
    public void addAuthor(AuthorDto authorDto) {
        Author author = this.dtoMapper.fromAuthorDto(authorDto);
        Author save = this.authorRepository.save(author);
        this.keyHolder = save.getId();
    }

    public List<Author> getAllAuthors() {
        return this.authorRepository.findAll();
    }

    public Optional<Author> findById(Integer id) {
        return this.authorRepository.findById(id);
    }

    public List<AuthorDto> getAllAuthorsDto() {
        List<Author> authors = this.authorRepository.findAll();
        List<AuthorDto> authorsDto = new ArrayList<>();
        authors.stream().forEach(author -> authorsDto.add(this.dtoMapper.fromAuthor(author)));
        return authorsDto;
    }

    @Transactional
    public void deleteAuthor(Integer id) {
        Author toDelete = this.authorRepository.findById(id).get();
        boolean flag = toDelete.getName() != FieldsConfig.DEFAULT_AUTHOR_NAME;
        if (flag) {
//            Collection<Book> bookOfAuthorToDelete = this.bookRepository.findByAuthorId(id);
//            Author defaultAuthor = this.getDefaultAuthor();
//            bookOfAuthorToDelete.stream().forEach(book -> {
//                book.setAuthor(defaultAuthor);
//                this.bookRepository.save(book);
//            });
            this.authorRepository.delete(toDelete);
        }
    }

    private Author getDefaultAuthor() {
        return this.authorRepository.findByName(FieldsConfig.DEFAULT_AUTHOR_NAME).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultAuthor() {
        Optional<Author> first = this.authorRepository.findByName(DEFAULT_AUTHOR_NAME).stream().findFirst();
        if (first.isEmpty()) {
            this.authorRepository.save(Author.builder().name(DEFAULT_AUTHOR_NAME).build());
        }
    }

    public Integer getKeyHolder() {
        return keyHolder;
    }

    public AuthorDto getLastUpdatedAuthorDto() {
        Author author = this.authorRepository.findById(this.keyHolder).get();
        return this.dtoMapper.fromAuthor(author);
    }
}
