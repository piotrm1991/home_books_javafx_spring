package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.config.MainConfig;
import com.example.home_books_javafx_spring.database.entities.Publisher;
import com.example.home_books_javafx_spring.database.repository.PublisherRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.PublisherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    DtoMapper dtoMapper;

//    @Autowired
//    BookRepository bookRepository;

    @Transactional
    public void addPublisher(Publisher publisher) {
        this.publisherRepository.save(publisher);
    }

    @Transactional
    public void addPublisher(PublisherDto publisherDto) {
        Publisher publisher = this.dtoMapper.fromPublisherDto(publisherDto);
        this.publisherRepository.save(publisher);
    }

    public List<Publisher> getAllPublishers() {
        return this.publisherRepository.findAll();
    }

    public Optional<Publisher> findById(Integer id) {
        return this.publisherRepository.findById(id);
    }

    @Transactional
    public void deletePublisher(Integer id) {
        Publisher toDelete = this.publisherRepository.findById(id).get();
        boolean flag = toDelete.getName() != MainConfig.DEFAULT_PUBLISHER_NAME;
        if (flag) {
//            Publisher defaultPublisher = this.getDefaultPublisher();
//            Collection<Book> booksOfPublisherToDelete = this.bookRepository.findByPublisherId(id);
//            booksOfPublisherToDelete.stream().forEach(book -> {
//                book.setPublisher(defaultPublisher);
//                this.bookRepository.save(book);
//            });
            this.publisherRepository.delete(toDelete);
        }
    }

    private Publisher getDefaultPublisher() {
        return this.publisherRepository.findPublisherByName(MainConfig.DEFAULT_PUBLISHER_NAME).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultPublisher() {
       this.publisherRepository
                .findPublisherByName(MainConfig.DEFAULT_PUBLISHER_NAME)
                .stream()
                .findAny()
                .orElse(this.publisherRepository.save(Publisher.builder().name(MainConfig.DEFAULT_PUBLISHER_NAME).build()));
    }


}