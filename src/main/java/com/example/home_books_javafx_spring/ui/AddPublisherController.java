package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.PublisherService;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.dto.models.PublisherDto;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@Component
public class AddPublisherController implements Initializable {

    @Autowired
    PublisherService publisherService;

    @Autowired
    EntityValidator entityValidator;

    @FXML
    private JFXTextField publisherName;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Integer publisherId;

    private boolean isSaved = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void addPublisher(ActionEvent actionEvent) {
        this.isSaved = false;
        String publisherName = this.publisherName.getText();

        PublisherDto publisherDto = PublisherDto.builder()
                .id(this.publisherId)
                .name(publisherName)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(publisherDto);

        if (errors.isEmpty()) {
            this.publisherService.addPublisher(publisherDto);
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Saved", "");
            this.isSaved = true;
            this.publisherId = null;
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    public void inflateUI(PublisherDto publisherDto) {
        this.publisherId = publisherDto.getId();
        this.publisherName.setText(publisherDto.getName());
    }

    public List<JFXButton> getControls() {
        List<JFXButton> list = Arrays.asList(this.saveButton, this.cancelButton);
        return list;
    }

    public JFXButton getSaveButton() {
        return this.saveButton;
    }

    public JFXButton getCancelButton() {
        return this.cancelButton;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
