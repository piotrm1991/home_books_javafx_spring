package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.*;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.*;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.example.home_books_javafx_spring.util.HomeBooksUtil;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.util.*;

import static com.example.home_books_javafx_spring.config.FieldsConfig.*;

@Component
public class AddBookController implements Initializable {

    @Autowired
    BookService bookService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    EntityValidator entityValidator;

    @Autowired
    RoomService roomService;

    @Autowired
    PublisherService publisherService;

    @Autowired
    AuthorService authorService;

    @Autowired
    StatusTypeService statusTypeService;

    @Autowired
    ShelfService shelfService;

    @Autowired
    DialogMaker dialogMaker;

    @Autowired
    AddAuthorController addAuthorController;

    @Autowired
    AddPublisherController addPublisherController;

    ObservableList<PublisherDto> publisherList;
    ObservableList<AuthorDto> authorList;
    ObservableList<RoomDto> roomList;
    ObservableList<ShelfDto> shelfList;
    ObservableList<StatusTypeDto> statusTypeList;

    @FXML
    private ComboBox<ShelfDto> comboBoxShelf;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField title;
    @FXML
    private ComboBox<PublisherDto> comboBoxPublisher;
    @FXML
    private JFXCheckBox checkBoxNewPublisher;
    @FXML
    private JFXDrawer drawerPublisher;
    @FXML
    private ComboBox<AuthorDto> comboBoxAuthor;
    @FXML
    private JFXCheckBox checkBoxNewAuthor;
    @FXML
    private JFXDrawer drawerAuthor;
    @FXML
    private ComboBox<RoomDto> comboBoxRoom;
    @FXML
    private ComboBox<StatusTypeDto> comboBoxStatusType;
    @FXML
    private JFXTextArea comment;
    @FXML
    private JFXTextField newPublisherName;
    @FXML
    private JFXTextField newAuthorFirstName;
    @FXML
    private JFXTextField newAuthorLastName;

    private Integer bookId;

    private StatusDto statusOfEditBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initDrawers();
        this.prepareLists();
        this.prepareComboBoxes();
        this.comboBoxShelf.setDisable(true);

        this.prepareRoomList();
        this.prepareRoomComboBox();
        this.addActionListeners();
    }

    private void addActionListeners() {
        this.addActionListenerToComboBox();
    }

    private void addActionListenerToComboBox() {
        this.addActionListenerToPublisherComboBox();
        this.addActionListenerToAuthorComboBox();
    }

    private void prepareComboBoxes() {
        this.preparePublisherComboBox();
        this.prepareAuthorComboBox();
        this.prepareStatusTypeComboBox();
    }

    private void initDrawers() {
        this.initAuthorDrawer();
        this.initPublisherDrawer();
    }

    private void prepareRoomList() {
        this.roomList = FXCollections.observableList(this.roomService.getAllRoomsDto());
        RoomDto roomDto = this.roomList.stream().filter(r -> r.getName().equals(DEFAULT_ROOM_NAME)).findFirst().get();
        HomeBooksUtil.setTopItem(this.roomList, HomeBooksUtil.findPosition(this.roomList, roomDto));
    }

    private void addActionListenerToAuthorComboBox() {
         this.comboBoxAuthor.valueProperty().addListener(new ChangeListener<AuthorDto>() {
             @Override
             public void changed(ObservableValue<? extends AuthorDto> observable, AuthorDto oldValue, AuthorDto newValue) {
                 if (newValue!= null && (newValue.getFirstName() + newValue.getLastName()).equals(DEFAULT_NEW_AUTHOR_FIRST_NAME + DEFAULT_NEW_AUTHOR_LAST_NAME)) {
                     JFXDialog dialog = dialogMaker.showAddEditAuthorDialog(rootPane, rootAnchorPane);
                     JFXButton saveButton = addAuthorController.getSaveButton();
                     JFXButton cancelButton = addAuthorController.getCancelButton();
                     saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                         if (addAuthorController.isSaved()) {
                             dialog.close();
                             authorList.clear();
                             prepareAuthorList();
                             AuthorDto newAuthorDto = authorService.getLastUpdatedAuthorDto();
                             comboBoxAuthor.setItems(authorList);
                             comboBoxAuthor.setValue(authorList.stream().filter(authorDto -> authorDto.equals(newAuthorDto))
                                     .findFirst()
                                     .get());
                         }
                     });
                     cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                         comboBoxAuthor.setValue(authorList.stream().filter(authorDto -> (authorDto.getFirstName()+authorDto.getLastName()).equals(DEFAULT_AUTHOR_FIRST_NAME+DEFAULT_AUTHOR_LAST_NAME)).findFirst().get());
                     });
                 }
             }
         });
    }

    private void addActionListenerToPublisherComboBox() {
        this.comboBoxPublisher.valueProperty().addListener(new ChangeListener<PublisherDto>() {
            @Override
            public void changed(ObservableValue<? extends PublisherDto> observable, PublisherDto oldValue, PublisherDto newValue) {
                if (newValue!= null && newValue.getName().equals(DEFAULT_NEW_PUBLISHER_NAME)) {
                    JFXDialog dialog = dialogMaker.showAddEditPublisherDialog(rootPane, rootAnchorPane);
                    JFXButton saveButton = addPublisherController.getSaveButton();
                    JFXButton cancelButton = addPublisherController.getCancelButton();
                    saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                        if (addPublisherController.isSaved()) {
                            dialog.close();
                            publisherList.clear();
                            preparePublisherList();
                            PublisherDto newPublisherDto = publisherService.getLastUpdatedPublisherDto();
                            comboBoxPublisher.setItems(publisherList);
                            comboBoxPublisher.setValue(publisherList.stream().filter(publisherDto -> publisherDto.equals(newPublisherDto)).findFirst().get());
                        }
                    });
                    cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                        comboBoxPublisher.setValue(publisherList.stream().filter(publisherDto -> publisherDto.getName().equals(DEFAULT_PUBLISHER_NAME)).findFirst().get());
                    });
                }
            }
        });
    }

    @FXML
    public void addBook(ActionEvent actionEvent) {

        BookDto bookDto = BookDto.builder()
                .id(this.bookId)
                .name(this.title.getText())
                .authorDto(this.comboBoxAuthor.getValue())
                .publisherDto(this.comboBoxPublisher.getValue())
                .shelfDto(this.comboBoxShelf.getValue())
                .statusDto(StatusDto.builder()
                        .id(this.statusOfEditBook == null ? null : this.statusOfEditBook.getId())
                        .statusTypeDto(this.comboBoxStatusType.getValue())
                        .comment(this.comment.getText())
                        .dateUp(new Date(System.currentTimeMillis()))
                        .build())
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(bookDto);

        if (errors.isEmpty()) {
            this.bookService.addBook(bookDto);
            this.bookId = null;
            this.statusOfEditBook = null;
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Saved", "");
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    private void prepareAuthorList() {
        this.authorList = FXCollections.observableList(this.authorService.getAllAuthorsDto());
        AuthorDto authorDtoDefault = this.authorList.stream().filter(r -> (r.getFirstName() + r.getLastName()).equals(DEFAULT_AUTHOR_FIRST_NAME + DEFAULT_AUTHOR_LAST_NAME)).findFirst().get();
        AuthorDto authorDtoNew = AuthorDto.builder().firstName(DEFAULT_NEW_AUTHOR_FIRST_NAME).lastName(DEFAULT_NEW_AUTHOR_LAST_NAME).build();
        this.authorList.add(authorDtoNew);
        HomeBooksUtil.setTopItem(this.authorList, HomeBooksUtil.findPosition(this.authorList, authorDtoNew));
        HomeBooksUtil.setTopItem(this.authorList, HomeBooksUtil.findPosition(this.authorList, authorDtoDefault));
    }

    private void preparePublisherList() {
        this.publisherList = FXCollections.observableList(this.publisherService.getAllPublishersDto());
        PublisherDto publisherDtoDefault = this.publisherList.stream().filter(r -> r.getName().equals(DEFAULT_PUBLISHER_NAME)).findFirst().get();
        PublisherDto publisherDtoNew = PublisherDto.builder().name(DEFAULT_NEW_PUBLISHER_NAME).build();
        this.publisherList.add(publisherDtoNew);
        HomeBooksUtil.setTopItem(this.publisherList, HomeBooksUtil.findPosition(this.publisherList, publisherDtoNew));
        HomeBooksUtil.setTopItem(this.publisherList, HomeBooksUtil.findPosition(this.publisherList, publisherDtoDefault));
    }

    private void prepareStatusTypeList() {
        this.statusTypeList = FXCollections.observableList(this.statusTypeService.getAllStatusTypesDto());
        StatusTypeDto statusTypeDto = this.statusTypeList.stream().filter(r -> r.getName().equals(DEFAULT_STATUS_TYPE)).findFirst().get();
        HomeBooksUtil.setTopItem(this.statusTypeList, HomeBooksUtil.findPosition(this.statusTypeList, statusTypeDto));
    }

    private void prepareLists() {
        this.prepareAuthorList();
        this.preparePublisherList();
        this.prepareStatusTypeList();
    }

    private void initAuthorDrawer() {
        drawerAuthor.setMinWidth(0);
        drawerAuthor.setMinHeight(0);
        checkBoxNewAuthor.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            drawerAuthor.toggle();
        });
        drawerAuthor.setOnDrawerOpening((event) -> {
            drawerAuthor.setMinWidth(300);
            drawerAuthor.setMinHeight(70);
            comboBoxAuthor.setDisable(true);
        });
        drawerAuthor.setOnDrawerClosed((event) -> {
            drawerAuthor.setMinWidth(0);
            drawerAuthor.setMinHeight(0);
            comboBoxAuthor.setDisable(false);
        });
    }

    private void initPublisherDrawer() {
        drawerPublisher.setMinWidth(0);
        drawerPublisher.setMinHeight(0);
        checkBoxNewPublisher.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            drawerPublisher.toggle();
        });
        drawerPublisher.setOnDrawerOpening((event) -> {
            drawerPublisher.setMinWidth(300);
            drawerPublisher.setMinHeight(25);
            comboBoxPublisher.setDisable(true);
        });
        drawerPublisher.setOnDrawerClosed((event) -> {
            drawerPublisher.setMinWidth(0);
            drawerPublisher.setMinHeight(0);
            comboBoxPublisher.setDisable(false);
        });
    }

    private void prepareStatusTypeComboBox() {
        this.comboBoxStatusType.setItems(this.statusTypeList);
        this.comboBoxStatusType.setValue(this.statusTypeList.stream().filter(st -> st.getName().equals(DEFAULT_STATUS_TYPE)).findFirst().get());
        this.comboBoxStatusType.setConverter(new StringConverter<StatusTypeDto>() {
            @Override
            public String toString(StatusTypeDto object) {
                return object.getName();
            }

            @Override
            public StatusTypeDto fromString(String string) {
                return comboBoxStatusType.getItems().stream().filter(st -> st.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void prepareRoomComboBox() {
        this.comboBoxRoom.setItems(this.roomList);
        this.comboBoxRoom.setValue(this.roomList.stream().filter(r -> r.getName().equals(DEFAULT_ROOM_NAME)).findFirst().get());
        this.comboBoxRoom.setConverter(new StringConverter<RoomDto>() {
            @Override
            public String toString(RoomDto object) {
                return object.getName();
            }

            @Override
            public RoomDto fromString(String string) {
                return comboBoxRoom.getItems().stream().filter(r -> r.getName().equals(string)).findFirst().orElse(null);
            }
        });
        this.comboBoxRoom.setOnAction(e -> {
            if (!this.comboBoxRoom.getValue().getName().equals(DEFAULT_ROOM_NAME)) {
                this.comboBoxShelf.setDisable(false);
                this.prepareShelfList(this.comboBoxRoom.getValue());
                this.prepareShelfComboBox();
            } else {
                this.comboBoxShelf.setDisable(true);
                this.shelfList.clear();
            }
        });
    }

    private void prepareShelfComboBox() {
        this.comboBoxShelf.setItems(this.shelfList);
        this.comboBoxShelf.setConverter(new StringConverter<ShelfDto>() {
            @Override
            public String toString(ShelfDto object) {
                return object.getLetter()
                       + " - "
                       + object.getNumber();
            }

            @Override
            public ShelfDto fromString(String string) {
                return comboBoxShelf.getItems().stream().filter(r -> (r.getLetter()
                                                                      + " - "
                                                                      + r.getNumber()).equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void prepareShelfList(RoomDto roomDto) {
        this.shelfList = FXCollections.observableList(this.shelfService.getAllShelvesDtoByRoomId(roomDto.getId()));
    }

    private void prepareAuthorComboBox() {
        this.comboBoxAuthor.setItems(this.authorList);
        this.comboBoxAuthor.setValue(this.authorList.stream().filter(r -> (
                r.getFirstName()
                + " "
                + r.getLastName())
                .equals(DEFAULT_AUTHOR_FIRST_NAME + " " + DEFAULT_AUTHOR_LAST_NAME))
                .findFirst()
                .get());
        this.comboBoxAuthor.setConverter(new StringConverter<AuthorDto>() {
            @Override
            public String toString(AuthorDto object) {
                return object.getFirstName()
                       + " "
                       + object.getLastName();
            }

            @Override
            public AuthorDto fromString(String string) {
                return comboBoxAuthor.getItems().stream().filter(r -> (r.getFirstName()
                                                                       + " "
                                                                       + r.getLastName()).equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void preparePublisherComboBox() {
        this.comboBoxPublisher.setItems(this.publisherList);
        this.comboBoxPublisher.setValue(this.publisherList.stream().filter(r -> r.getName().equals(DEFAULT_PUBLISHER_NAME)).findFirst().get());
        this.comboBoxPublisher.setConverter(new StringConverter<PublisherDto>() {
            @Override
            public String toString(PublisherDto object) {
                return object.getName();
            }

            @Override
            public PublisherDto fromString(String string) {
                return comboBoxPublisher.getItems().stream().filter(r -> r.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void inflateUI(BookDto bookDto) {
        this.bookId = bookDto.getId();
        this.title.setText(bookDto.getName());
        this.comboBoxPublisher.setValue(bookDto.getPublisherDto());
        this.comboBoxAuthor.setValue(bookDto.getAuthorDto());
        this.comboBoxRoom.setValue(bookDto.getShelfDto().getRoomDto());
        this.comboBoxShelf.setValue(bookDto.getShelfDto());
        this.comboBoxStatusType.setValue(bookDto.getStatusDto().getStatusTypeDto());
        this.comment.setText(bookDto.getStatusDto().getComment());

        this.comboBoxShelf.setDisable(false);
        this.prepareShelfList(bookDto.getShelfDto().getRoomDto());
        this.prepareShelfComboBox();
        this.statusOfEditBook = bookDto.getStatusDto();
    }

    public JFXButton getSaveButton() {
        return saveButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }
}