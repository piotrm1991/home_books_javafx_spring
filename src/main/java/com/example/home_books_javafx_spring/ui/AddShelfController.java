package com.example.home_books_javafx_spring.ui;


import com.example.home_books_javafx_spring.config.FieldsConfig;
import com.example.home_books_javafx_spring.database.service.RoomService;
import com.example.home_books_javafx_spring.database.service.ShelfService;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.dto.models.RoomDto;
import com.example.home_books_javafx_spring.dto.models.ShelfDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.example.home_books_javafx_spring.util.HomeBooksUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Component
public class AddShelfController implements Initializable {

    @Autowired
    RoomService roomService;

    @Autowired
    EntityValidator entityValidator;

    @Autowired
    ShelfService shelfService;

    ObservableList<RoomDto> roomList;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXComboBox<RoomDto> comboBoxRoom;
    @FXML
    private JFXTextField letterInput;
    @FXML
    private JFXTextField numberInput;

    private Integer shelfId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prepareList();
        this.prepareComboBox();
        this.prepareNumberField();
        this.prepareLetterField();
    }

    private void prepareList() {
        this.roomList = FXCollections.observableList(this.roomService.getAllRoomsDto());

        RoomDto roomDto = this.roomList.stream().filter(r -> r.getName().equals(FieldsConfig.DEFAULT_ROOM_NAME)).findFirst().get();
        HomeBooksUtil.setTopItem(this.roomList, HomeBooksUtil.findPosition(this.roomList, roomDto));

    }

    private void prepareComboBox() {
        this.comboBoxRoom.setItems(this.roomList);
        this.comboBoxRoom.setValue(this.roomList.stream().filter(r -> r.getName().equals(FieldsConfig.DEFAULT_ROOM_NAME)).findFirst().get());
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
    }

    private void prepareNumberField() {
        DecimalFormat format = new DecimalFormat("0");
        this.numberInput.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object
                == null
                || parsePosition.getIndex()
                   < c.getControlNewText().length()) {
                return null;
            } else {
                return c;
            }
        }));
    }

    private void prepareLetterField() {
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        this.letterInput.setTextFormatter(formatter);
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    @FXML
    public void addShelf(ActionEvent actionEvent) {
        String letter = this.letterInput.getText();
        String number = this.numberInput.getText();
        RoomDto roomDto = this.comboBoxRoom.getValue();

        if (roomDto.getName().equals(FieldsConfig.DEFAULT_ROOM_NAME)) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "You Have To Choose Room", "");
            return;
        }

         ShelfDto shelfDto = ShelfDto.builder()
                .id(this.shelfId)
                .number(Integer.parseInt(number))
                .letter(letter)
                .roomDto(roomDto)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(shelfDto);

        if (errors.isEmpty()) {
            this.shelfService.addShelf(shelfDto);
            if (this.roomList.get(0).getId() == null) {
                this.prepareList();
                this.prepareComboBox();
            }
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    public JFXButton getSaveButton() {
        return saveButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }

    public void inflateUI(ShelfDto shelfDto) {
        this.shelfId = shelfDto.getId();
        this.comboBoxRoom.setValue(shelfDto.getRoomDto());
        this.letterInput.setText(shelfDto.getLetter());
        this.numberInput.setText(shelfDto.getNumber().toString());
    }
}
