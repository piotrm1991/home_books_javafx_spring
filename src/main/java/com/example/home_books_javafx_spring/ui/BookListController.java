package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.BookService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.*;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class BookListController implements ControllerForList {

    @Autowired
    DialogMaker dialogMaker;

    @Autowired
    BookService bookService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<BookUi> list = FXCollections.observableArrayList();

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TableView<BookUi> tableView;
    @FXML
    private TableColumn<BookUi, String> titleCol;
    @FXML
    private TableColumn<BookUi, String> authorCol;
    @FXML
    private TableColumn<BookUi, String> publisherCol;
    @FXML
    private TableColumn<BookUi, String> statusCol;
    @FXML
    private TableColumn<BookUi, String> roomCol;
    @FXML
    private TableColumn<BookUi, String> shelfCol;
    @FXML
    private TableColumn<BookUi, String> dateCol;
    @FXML
    public TableColumn<BookUi, String> commentCol;

    private List<BookDto> searchResult = null;

    private EntityDto sortFlag = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }

    @FXML
    public void handleEditBookAction(ActionEvent actionEvent) {

        if (this.tableView.getSelectionModel().getSelectedItem()
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Book Selected", "Please Select Book For Edit");
            return;
        }

        BookDto selectedForEdit = this.dtoMapper.fromBookUI(this.tableView.getSelectionModel().getSelectedItem());

        Map<String, Object> controls = this.dialogMaker.showDialog(rootPane, rootAnchorPane, "editBook", selectedForEdit);
        JFXButton button = (JFXButton) controls.get("cancel");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.handleRefreshBookAction(new ActionEvent());
        });
    }

    @FXML
    public void handleDeleteBookAction(ActionEvent actionEvent) {

        if (this.tableView.getSelectionModel().getSelectedItem()
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Book Selected", "Please Select The Book");
            return;
        }

        BookDto selectedForDeletion = this.dtoMapper.fromBookUI(this.tableView.getSelectionModel().getSelectedItem());

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.bookService.deleteBook(selectedForDeletion.getId());
            list.remove(selectedForDeletion);
            this.handleRefreshBookAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Book", "Are You Sure You Want To Delete Book - "
                                                                                                                + selectedForDeletion.getName()
                                                                                                                + "?");
    }

    @FXML
    public void handleRefreshBookAction(ActionEvent actionEvent) {
        this.loadData();
    }

    private void loadData() {
        this.list.clear();
        List<BookDto> result = null;

        if (this.sortFlag
            != null) {
            if (this.sortFlag.getClass()
                == AuthorDto.class) {
                result = this.bookService.getBooksDtoByAuthorId(this.sortFlag.getId());
            } else if (this.sortFlag.getClass()
                       == StatusTypeDto.class) {
                result = this.bookService.getBooksDtoByStatusTypeId(this.sortFlag.getId());
            } else if (this.sortFlag.getClass()
                       == RoomDto.class) {
                result = this.bookService.getBooksDtoByRoomId(this.sortFlag.getId());
            } else if (this.sortFlag.getClass()
                       == ShelfDto.class) {
                result = this.bookService.getBooksDtoByShelfId(this.sortFlag.getId());
            } else if (this.sortFlag.getClass()
                       == PublisherDto.class) {
                result = this.bookService.getBooksDtoByPublisherId(this.sortFlag.getId());
            } else {
                result = this.bookService.getAllBooksDto();
            }
        } else if (this.searchResult
                   != null) {
            result = this.searchResult;
        } else {
            result = this.bookService.getAllBooksDto();
        }

        try {
            Iterator<BookDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new BookUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, e);
        }
        this.tableView.setItems(list);
    }

    private void initCol() {
        this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        this.publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        this.statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        this.shelfCol.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    @Override
    public void inflateUI(EntityDto entity) {
        this.sortFlag = entity;
        this.loadData();
    }

    public JFXButton inflateUI(List<BookDto> books) {
        this.searchResult = books;
        loadData();
        JFXButton close = new JFXButton("Close");
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.handleRefreshBookAction(new ActionEvent());
        });
        return close;
    }

    public static class BookUi {

        private Integer id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty status;
        private final SimpleStringProperty room;
        private final SimpleStringProperty shelf;
        private final SimpleStringProperty date;
        private final SimpleStringProperty comment;

        public BookUi(BookDto bookDto) {
            this.id = bookDto.getId();
            this.title = new SimpleStringProperty(bookDto.getName());
            this.author = new SimpleStringProperty(bookDto.getAuthorDto().getName());
            this.publisher = new SimpleStringProperty(bookDto.getPublisherDto().getName());
            this.status = new SimpleStringProperty(bookDto.getStatusDto().getStatusTypeDto().getName());
            this.room = new SimpleStringProperty(bookDto.getShelfDto().getRoomDto().getName());
            this.shelf = new SimpleStringProperty(bookDto.getShelfDto().getLetter()
                                                  + " - "
                                                  + bookDto.getShelfDto().getNumber());
            this.date = new SimpleStringProperty(bookDto.getStatusDto().getDateUp().toString());
            this.comment = new SimpleStringProperty(bookDto.getStatusDto().getComment());
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getStatus() {
            return status.get();
        }

        public String getRoom() {
            return room.get();
        }

        public String getShelf() {
            return shelf.get();
        }

        public String getDate() {
            return date.get();
        }

        public String getComment() {
            return comment.get();
        }
    }
}
