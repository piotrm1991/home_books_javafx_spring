package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.AuthorService;
import com.example.home_books_javafx_spring.database.service.RoomService;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.dto.models.RoomDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;

@Component
public class AddRoomController implements Initializable {

    @Autowired
    RoomService roomService;

    @Autowired
    EntityValidator entityValidator;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTextField roomName;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Integer roomId;

    private boolean inEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    @FXML
    public void addRoom(ActionEvent actionEvent) {
        String roomName = this.roomName.getText();

        RoomDto roomDto = RoomDto.builder()
                .id(this.roomId)
                .name(roomName)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(roomDto);

        if (errors.isEmpty()) {
            this.roomService.addRoom(roomDto);
            this.roomId = null;
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }

    public JFXButton getSaveButton() {
        return saveButton;
    }

    public void inflateUI(RoomDto roomDto) {
        this.roomId = roomDto.getId();
        this.roomName.setText(roomDto.getName());

        this.inEditMode = true;
    }
}
