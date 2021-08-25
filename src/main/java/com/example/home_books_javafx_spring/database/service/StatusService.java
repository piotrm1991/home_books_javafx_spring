package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.database.entities.Status;
import com.example.home_books_javafx_spring.database.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public Optional<Status> findById(Integer id) {
        return this.statusRepository.findById(id);
    }
}
