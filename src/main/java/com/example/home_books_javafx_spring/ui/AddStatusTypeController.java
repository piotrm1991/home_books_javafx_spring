package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.StatusTypeService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.dto.models.StatusTypeDto;
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
public class AddStatusTypeController implements Initializable {

    @Autowired
    StatusTypeService statusTypeService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    EntityValidator entityValidator;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField statusTypeName;

    private Integer statusTypeId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    @FXML
    public void addStateType(ActionEvent actionEvent) {
        String stateTypeName = this.statusTypeName.getText();

        StatusTypeDto statusTypeDto = StatusTypeDto.builder()
                .id(this.statusTypeId)
                .name(stateTypeName)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(statusTypeDto);

        if (errors.isEmpty()) {
            this.statusTypeService.addStatusType(statusTypeDto);
            this.statusTypeService = null;
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

    public void inflateUI(StatusTypeDto statusTypeDto) {
        this.statusTypeId = statusTypeDto.getId();
        this.statusTypeName.setText(statusTypeDto.getName());
    }
}
