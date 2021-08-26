package com.example.home_books_javafx_spring.dto;


import com.example.home_books_javafx_spring.database.entities.*;
import com.example.home_books_javafx_spring.database.service.AuthorService;
//import com.example.home_books_javafx_spring.database.service.BookService;
import com.example.home_books_javafx_spring.database.service.BookService;
import com.example.home_books_javafx_spring.database.service.RoomService;
import com.example.home_books_javafx_spring.database.service.ShelfService;
import com.example.home_books_javafx_spring.dto.models.*;
import com.example.home_books_javafx_spring.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.home_books_javafx_spring.ui.AuthorListController.*;
import static com.example.home_books_javafx_spring.ui.BookListController.*;
import static com.example.home_books_javafx_spring.ui.PublisherListController.*;
import static com.example.home_books_javafx_spring.ui.RoomListController.*;
import static com.example.home_books_javafx_spring.ui.ShelfListController.*;
import static com.example.home_books_javafx_spring.ui.StatusTypeListController.*;

@Component
public class DtoMapper {

    @Autowired
    RoomService roomService;

    @Autowired
    ShelfService shelfService;

    @Autowired
    BookService bookService;

    public Shelf fromShelfDto(ShelfDto shelfDto) {
        Shelf shelf = Shelf.builder()
                .id(shelfDto.getId())
                .letter(shelfDto.getLetter())
                .number(shelfDto.getNumber())
                .room(this.roomService.findRoomById(shelfDto.getRoomDto().getId()))
                .build();
        return shelf;
    }

    public ShelfDto fromShelf(Shelf shelf) {
        ShelfDto shelfDto = ShelfDto.builder()
                .id(shelf.getId())
                .letter(shelf.getLetter())
                .number(shelf.getNumber())
                .roomDto(this.fromRoom(shelf.getRoom()))
                .nBooks(this.bookService.countBooksByShelfId(shelf.getId()))
                .build();
        return shelfDto;
    }

    public ShelfDto fromShelfUI(ShelfUi shelfUi) {
        ShelfDto shelfDto = ShelfDto.builder()
                .id(shelfUi.getId())
                .letter(shelfUi.getLetter())
                .number(Integer.valueOf(shelfUi.getNumber()))
                .roomDto(this.roomService.findRoomDtoById(shelfUi.getRoomId()))
                .nBooks(Integer.valueOf(shelfUi.getNumberBooks()))
                .build();
        return shelfDto;
    }

    public StatusType fromStatusTypeDto(StatusTypeDto statusTypeDto) {
        StatusType statusType = StatusType.builder()
                .id(statusTypeDto.getId())
                .name(statusTypeDto.getName())
                .build();
        return statusType;
    }

    public StatusTypeDto fromStatusType(StatusType statusType) {
        StatusTypeDto statusTypeDto = StatusTypeDto.builder()
                .id(statusType.getId())
                .name(statusType.getName())
                .nBooks(this.bookService.countBooksByStatusTypeId(statusType.getId()))
                .build();
        return statusTypeDto;
    }
    public StatusTypeDto fromStatusTypeUI(StatusTypeUi statusTypeUi) {
        StatusTypeDto statusTypeDto = StatusTypeDto.builder()
                .id(statusTypeUi.getId())
                .name(statusTypeUi.getName())
                .nBooks(Integer.valueOf(statusTypeUi.getNumberBooks()))
                .build();
        return statusTypeDto;
    }

    public BookDto fromBook(Book book) {
        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorDto(this.fromAuthor(book.getAuthor()))
                .publisherDto(this.fromPublisher(book.getPublisher()))
                .statusDto(this.fromStatus(book.getStatus()))
                .shelfDto(this.fromShelf(book.getShelf()))
                .build();

        return bookDto;
    }

    private StatusDto fromStatus(Status status) {
        StatusDto statusDto = StatusDto.builder()
                .id(status.getId())
                .dateUp(status.getDateUp())
                .comment(status.getComment())
                .statusTypeDto(this.fromStatusType(status.getStatusType()))
                .build();

        return statusDto;
    }

    public Book fromBookDto(BookDto bookDto) {
        Book book = Book.builder()
                .id(bookDto.getId())
                .name(bookDto.getName())
                .author(this.fromAuthorDto(bookDto.getAuthorDto()))
                .publisher(this.fromPublisherDto(bookDto.getPublisherDto()))
                .shelf(this.fromShelfDto(bookDto.getShelfDto()))
                .status(this.fromStatusDto(bookDto.getStatusDto()))
                .build();
        return book;
    }

    private Status fromStatusDto(StatusDto statusDto) {
        Status status = Status.builder()
                .id(statusDto.getId())
                .comment(statusDto.getComment())
                .dateUp(statusDto.getDateUp())
                .statusType(this.fromStatusTypeDto(statusDto.getStatusTypeDto()))
                .build();
        return status;
    }

    public AuthorDto fromAuthor(Author author) {
        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .nBooks(this.bookService.countBooksByAuthorId(author.getId()))
                .build();
        return authorDto;
    }

    public Author fromAuthorDto(AuthorDto authorDto) {
        Author author = Author.builder()
                .id(authorDto.getId())
                .name(authorDto.getName())
                .build();
        return author;
    }

    public AuthorDto fromAuthorUI(AuthorUi authorUi) {
        AuthorDto authorDto = AuthorDto.builder()
                .id(authorUi.getId())
                .name(authorUi.getName())
                .nBooks(Integer.valueOf(authorUi.getNumberBooks()))
                .build();
        return authorDto;
    }

    public PublisherDto fromPublisher(Publisher publisher) {
        PublisherDto publisherDto = PublisherDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .nBooks(this.bookService.countBooksByPublisherId(publisher.getId()))
                .build();
        return publisherDto;
    }

    public Publisher fromPublisherDto(PublisherDto publisherDto) {
        Publisher publisher = Publisher.builder()
                .id(publisherDto.getId())
                .name(publisherDto.getName())
                .build();
        return publisher;
    }

    public PublisherDto fromPublisherUI(PublisherUi publisherUi) {
        PublisherDto publisherDto = PublisherDto.builder()
                .id(publisherUi.getId())
                .name(publisherUi.getName())
                .nBooks(Integer.valueOf(publisherUi.getNumberBooks()))
                .build();
        return publisherDto;
    }

    public RoomDto fromRoom(Room room) {
        RoomDto roomDto = RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .nBooks(this.bookService.countBooksByRoomId(room.getId()))
                .nShelves(this.shelfService.countShelvesByRoomId(room.getId()))
                .build();
        return roomDto;
    }

    public RoomDto fromRoomUI(RoomUi roomUi) {
        RoomDto roomDto = RoomDto.builder()
                .id(roomUi.getId())
                .name(roomUi.getName())
                .nShelves(Integer.valueOf(roomUi.getNumberShelves()))
                .nBooks(Integer.valueOf(roomUi.getNumberBooks()))
                .build();
        return roomDto;
    }

    public Room fromRoomDto(RoomDto roomDto) {
        Room room = Room.builder()
                .id(roomDto.getId())
                .name(roomDto.getName())
                .build();
        return room;
    }

    public BookDto fromBookUI(BookUi bookUi) {
        BookDto bookDto = this.bookService.getBookDtoById(bookUi.getId());
        return bookDto;
    }
}
