package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.database.entities.Shelf;
import com.example.home_books_javafx_spring.database.repository.ShelfRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.ShelfDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShelfService {

    @Autowired
    ShelfRepository shelfRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    DtoMapper dtoMapper;

    @Transactional
    public void addShelf(Shelf shelf) {
        entityManager.merge(shelf);
    }

    @Transactional
    public void addShelf(ShelfDto shelfDto) {
        Shelf shelf = this.dtoMapper.fromShelfDto(shelfDto);
        entityManager.merge(shelf);
    }

    public List<Shelf> getAllShelves() {
        return this.shelfRepository.findAll();
    }

    public List<Shelf> getShelvesByRoom(Integer id) {
        return (List<Shelf>) this.shelfRepository.findByRoomId(id);
    }

    public Shelf getShelfById(Integer id) {
        return this.shelfRepository.findById(id).get();
    }

    public List<ShelfDto> getAllShelvesDto() {
        List<Shelf> shelves = this.shelfRepository.findAll();
        List<ShelfDto> shelvesDto = new ArrayList<>();
        shelves.stream().forEach(shelf -> shelvesDto.add(this.dtoMapper.fromShelf(shelf)));
        return shelvesDto;
    }

    @Transactional
    public void deleteShelf(Integer id) {
        this.shelfRepository.deleteById(id);
    }

    public List<ShelfDto> getAllShelvesDtoByRoomId(Integer id) {
        List<Shelf> shelves = this.shelfRepository.findByRoomId(id);
        List<ShelfDto> shelvesDto = new ArrayList<>();
        shelves.stream().forEach(shelf -> shelvesDto.add(this.dtoMapper.fromShelf(shelf)));
        return shelvesDto;
    }

    public Integer countShelvesByRoomId(Integer id) {
        return this.shelfRepository.findByRoomId(id).size();
    }
}
