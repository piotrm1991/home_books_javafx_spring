package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.config.FieldsConfig;
import com.example.home_books_javafx_spring.database.entities.Publisher;
import com.example.home_books_javafx_spring.database.repository.PublisherRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.PublisherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.home_books_javafx_spring.config.FieldsConfig.*;

@Service
public class PublisherService {

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    DtoMapper dtoMapper;

    private Integer keyHolder;

    @Transactional
    public void addPublisher(Publisher publisher) {
        Publisher save = this.publisherRepository.save(publisher);
        this.keyHolder = save.getId();
    }

    @Transactional
    public void addPublisher(PublisherDto publisherDto) {
        Publisher publisher = this.dtoMapper.fromPublisherDto(publisherDto);
        Publisher save = this.publisherRepository.save(publisher);
        this.keyHolder = save.getId();
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
        boolean flag = toDelete.getName() != DEFAULT_PUBLISHER_NAME;
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
        return this.publisherRepository.findPublisherByName(DEFAULT_PUBLISHER_NAME).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultPublisher() {
        Optional<Publisher> first = this.publisherRepository.findPublisherByName(DEFAULT_PUBLISHER_NAME).stream().findFirst();
        if (first.isEmpty()) {
            this.publisherRepository.save(Publisher.builder().name(DEFAULT_PUBLISHER_NAME).build());
        }
    }


    public List<PublisherDto> getAllPublishersDto() {
        List<Publisher> publishers = this.publisherRepository.findAll();
        List<PublisherDto> publishersDto = new ArrayList<>();
        publishers.stream().forEach(publisher -> publishersDto.add(this.dtoMapper.fromPublisher(publisher)));
        return publishersDto;
    }

    public Integer getKeyHolder() {
        return keyHolder;
    }

    public PublisherDto getLastUpdatedPublisherDto() {
        Publisher publisher = this.publisherRepository.findById(this.keyHolder).get();
        return this.dtoMapper.fromPublisher(publisher);
    }
}
