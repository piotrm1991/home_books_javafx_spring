package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.*;
import com.example.home_books_javafx_spring.dto.models.*;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.example.home_books_javafx_spring.util.HomeBooksUtil;
import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import static com.example.home_books_javafx_spring.config.FieldsConfig.*;

@Component
public class AddBookController implements Initializable {

    @Autowired
    BookService bookService;

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
    private JFXComboBox<AuthorDto> comboBoxAuthor;
    @FXML
    private JFXComboBox<RoomDto> comboBoxRoom;
    @FXML
    private JFXComboBox<StatusTypeDto> comboBoxStatusType;
    @FXML
    private JFXTextArea comment;

    private Integer bookId;

    private StatusDto statusOfEditBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void prepareRoomList() {
        this.roomList = FXCollections.observableList(this.roomService.getAllRoomsDto());
        RoomDto roomDto = this.roomList.stream().filter(r -> r.getName().equals(DEFAULT_ROOM_NAME)).findFirst().get();
        HomeBooksUtil.setTopItem(this.roomList, HomeBooksUtil.findPosition(this.roomList, roomDto));
    }

    private void addActionListenerToAuthorComboBox() {
        this.comboBoxAuthor.valueProperty().addListener(new ChangeListener<AuthorDto>() {
            @Override
            public void changed(ObservableValue<? extends AuthorDto> observable, AuthorDto oldValue, AuthorDto newValue) {
                if (newValue
                    != null
                    && (newValue.getName()).equals(DEFAULT_NEW_AUTHOR_NAME)) {
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
                        comboBoxAuthor.setValue(null);
                    });
                }
            }
        });
    }

    private void addActionListenerToPublisherComboBox() {
        this.comboBoxPublisher.valueProperty().addListener(new ChangeListener<PublisherDto>() {
            @Override
            public void changed(ObservableValue<? extends PublisherDto> observable, PublisherDto oldValue, PublisherDto newValue) {
                if (newValue
                    != null
                    && newValue.getName().equals(DEFAULT_NEW_PUBLISHER_NAME)) {
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
                        comboBoxPublisher.setValue(null);
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
                        .id(this.statusOfEditBook
                            == null ? null : this.statusOfEditBook.getId())
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
        AuthorDto authorDtoDefault = this.authorList.stream().filter(r -> r.getName().equals(DEFAULT_AUTHOR_NAME)).findFirst().get();
        AuthorDto authorDtoNew = AuthorDto.builder().name(DEFAULT_NEW_AUTHOR_NAME).build();
//        AuthorDto authorDtoChoose = AuthorDto.builder().name(DEFAULT_CHOOSE_AUTHOR_NAME).build();
        this.authorList.add(authorDtoNew);
//        this.authorList.add(authorDtoChoose);
        HomeBooksUtil.setTopItem(this.authorList, HomeBooksUtil.findPosition(this.authorList, authorDtoNew));
        HomeBooksUtil.setTopItem(this.authorList, HomeBooksUtil.findPosition(this.authorList, authorDtoDefault));
//        HomeBooksUtil.setTopItem(this.authorList, HomeBooksUtil.findPosition(this.authorList, authorDtoChoose));
    }

    private void preparePublisherList() {
        this.publisherList = FXCollections.observableList(this.publisherService.getAllPublishersDto());
        PublisherDto publisherDtoDefault = this.publisherList.stream().filter(r -> r.getName().equals(DEFAULT_PUBLISHER_NAME)).findFirst().get();
        PublisherDto publisherDtoNew = PublisherDto.builder().name(DEFAULT_NEW_PUBLISHER_NAME).build();
//        PublisherDto publisherDtoChoose = PublisherDto.builder().name(DEFAULT_CHOOSE_PUBLISHER_NAME).build();
        this.publisherList.add(publisherDtoNew);
//        this.publisherList.add(publisherDtoChoose);
        HomeBooksUtil.setTopItem(this.publisherList, HomeBooksUtil.findPosition(this.publisherList, publisherDtoNew));
        HomeBooksUtil.setTopItem(this.publisherList, HomeBooksUtil.findPosition(this.publisherList, publisherDtoDefault));
//        HomeBooksUtil.setTopItem(this.publisherList, HomeBooksUtil.findPosition(this.publisherList, publisherDtoChoose));
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

    private void prepareStatusTypeComboBox() {
        this.comboBoxStatusType.setItems(this.statusTypeList);
//        this.comboBoxStatusType.setValue(this.statusTypeList.stream().filter(st -> st.getName().equals(DEFAULT_STATUS_TYPE)).findFirst().get());
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
//        this.comboBoxAuthor.setValue(this.authorList.stream().filter(r -> (
//                r.getName()
//                        .equals(DEFAULT_CHOOSE_AUTHOR_NAME)))
//                .findFirst()
//                .get());
        this.comboBoxAuthor.setConverter(new StringConverter<AuthorDto>() {
            @Override
            public String toString(AuthorDto object) {
                return object.getName();
            }

            @Override
            public AuthorDto fromString(String string) {
                return comboBoxAuthor.getItems().stream().filter(r -> (r.getName()).equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void preparePublisherComboBox() {
        this.comboBoxPublisher.setItems(this.publisherList);
//        this.comboBoxPublisher.setValue(this.publisherList.stream().filter(r -> (
//                r.getName()
//                        .equals(DEFAULT_CHOOSE_PUBLISHER_NAME)))
//                .findFirst()
//                .get());
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

        this.comboBoxStatusType.setValue(bookDto.getStatusDto().getStatusTypeDto());
        this.comment.setText(bookDto.getStatusDto().getComment());

        this.comboBoxShelf.setDisable(false);
        this.prepareShelfList(bookDto.getShelfDto().getRoomDto());
        this.prepareShelfComboBox();
        this.comboBoxShelf.setValue(bookDto.getShelfDto());
        this.statusOfEditBook = bookDto.getStatusDto();

        this.prepareComboBoxesForEdit();
    }

    private void prepareComboBoxesForEdit() {
        this.preparePublisherComboBoxForEdit();
        this.prepareAuthorComboBoxForEdit();
        this.prepareRoomComboBoxForEdit();
        this.prepareStatusTypeComboBoxForEdit();
        this.prepareShelfComboBoxForEdit();
    }

    private void prepareShelfComboBoxForEdit() {
        this.comboBoxShelf.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {
            Color selectedColor = Color.valueOf("#FFFF8D");
            return new ListCell<ShelfDto>() {
                @Override
                protected void updateItem(ShelfDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getLetter()
                                + " - "
                                + item.getNumber());
                        setTextFill(selectedColor);
                    }
                    if (empty
                        && item
                           == null) {
                        setText("");
                    }
                }
            };
        }));
    }

    private void prepareStatusTypeComboBoxForEdit() {
        this.comboBoxStatusType.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {
            Color selectedColor = Color.valueOf("#FFFF8D");
            return new ListCell<StatusTypeDto>() {
                @Override
                protected void updateItem(StatusTypeDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getName());
                        setTextFill(selectedColor);
                    }
                    if (empty
                        && item
                           == null) {
                        setText("");
                    }
                }
            };
        }));
    }

    private void prepareRoomComboBoxForEdit() {

        this.comboBoxRoom.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {
            Color selectedColor = Color.valueOf("#FFFF8D");
            return new ListCell<RoomDto>() {
                @Override
                protected void updateItem(RoomDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getName());
                        setTextFill(selectedColor);
                    }
                    if (empty
                        && item
                           == null) {
                        setText("");
                    }
                }
            };
        }));
    }

    private void prepareAuthorComboBoxForEdit() {
        this.comboBoxAuthor.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {
            Color selectedColor = Color.valueOf("#FFFF8D");
            return new ListCell<AuthorDto>() {
                @Override
                protected void updateItem(AuthorDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getName());
                        setTextFill(selectedColor);
                    }
                    if (empty
                        && item
                           == null) {
                        setText("");
                    }
                }
            };
        }));
    }

    private void preparePublisherComboBoxForEdit() {
        this.comboBoxPublisher.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {
            Color selectedColor = Color.valueOf("#FFFF8D");
            return new ListCell<PublisherDto>() {
                @Override
                protected void updateItem(PublisherDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setText(item.getName());
                        setTextFill(selectedColor);
                    }
                    if (empty
                        && item
                           == null) {
                        setText("");
                    }
                }
            };
        }));
    }

    public JFXButton getSaveButton() {
        return saveButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }
}
