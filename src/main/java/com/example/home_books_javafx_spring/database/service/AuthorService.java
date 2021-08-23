package com.example.home_books_javafx_spring.database.service;


import com.example.home_books_javafx_spring.config.MainConfig;
import com.example.home_books_javafx_spring.database.entities.Author;
import com.example.home_books_javafx_spring.database.repository.AuthorRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    DtoMapper dtoMapper;

//    @Autowired
//    BookRepository bookRepository;

    @Transactional
    public void addAuthor(AuthorDto authorDto) {
        Author author = this.dtoMapper.fromAuthorDto(authorDto);
        this.authorRepository.save(author);
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
        boolean flag = toDelete.getFirstName() != MainConfig.DEFAULT_AUTHOR_FIRST_NAME && toDelete.getLastName() != MainConfig.DEFAULT_AUTHOR_LAST_NAME;
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
        return this.authorRepository.findByFirstNameAndLastName(MainConfig.DEFAULT_AUTHOR_FIRST_NAME, MainConfig.DEFAULT_AUTHOR_LAST_NAME).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultAuthor() {
        this.authorRepository
                .findByFirstNameAndLastName(MainConfig.DEFAULT_AUTHOR_FIRST_NAME, MainConfig.DEFAULT_AUTHOR_LAST_NAME)
                .stream()
                .findAny()
                .orElse(this.authorRepository.save(Author.builder()
                        .firstName(MainConfig.DEFAULT_AUTHOR_FIRST_NAME)
                        .lastName(MainConfig.DEFAULT_AUTHOR_LAST_NAME)
                        .build()));
    }
}
