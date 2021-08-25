package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.database.entities.Publisher;
import com.example.home_books_javafx_spring.database.entities.Room;
import com.example.home_books_javafx_spring.database.repository.RoomRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.home_books_javafx_spring.config.FieldsConfig.*;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    DtoMapper dtoMapper;

    @Transactional
    public void addRoom(Room room) {
        this.roomRepository.save(room);
    }

    @Transactional
    public void addRoom(RoomDto roomDto) {
        Room room = this.dtoMapper.fromRoomDto(roomDto);
        this.roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    public Room findRoomByName(String name) {
        Room room = this.roomRepository.findRoomByName(name).stream().findFirst().get();
        return room;
    }

    public Room findRoomById(Integer idRoom) {
        return this.roomRepository.findRoomById(idRoom);
    }

    public RoomDto findRoomDtoById(Integer idRoom) {
        RoomDto roomDto = this.dtoMapper.fromRoom(this.roomRepository.findRoomById(idRoom));
        return roomDto;
    }

    public List<RoomDto> getAllRoomsDto() {
        List<Room> rooms = this.roomRepository.findAll();
        List<RoomDto> roomsDto = new ArrayList<>();
        rooms.stream().forEach(room -> roomsDto.add(this.dtoMapper.fromRoom(room)));
        return roomsDto;
    }

    private Room getDefaultRoom() {
        return this.roomRepository.findRoomByName(DEFAULT_ROOM_NAME).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultRoom() {
        Optional<Room> first = this.roomRepository.findRoomByName(DEFAULT_ROOM_NAME).stream().findFirst();
        if (first.isEmpty()) {
            this.roomRepository.save(Room.builder().name(DEFAULT_ROOM_NAME).build());
        }
    }

    @Transactional
    public void deleteRoomById(Integer id) {
        this.roomRepository.deleteById(id);
    }
}
