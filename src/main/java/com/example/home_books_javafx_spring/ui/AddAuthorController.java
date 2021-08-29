package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.AuthorService;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
public class AddAuthorController implements ControllerForEntities {

    @Autowired
    AuthorService authorService;

    @Autowired
    EntityValidator entityValidator;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Integer authorId;

    private boolean isSaved = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
    }

    public void addAuthor(ActionEvent actionEvent) {

        this.isSaved = false;
        String name = this.name.getText();

        AuthorDto authorDto = AuthorDto.builder()
                .id(this.authorId)
                .name(name)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(authorDto);

        if (errors.isEmpty()) {
            this.authorService.addAuthor(authorDto);
            this.authorId = null;
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Saved", "");
            this.isSaved = true;
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    @Override
    public void inflateUI(EntityDto authorDto) {
        this.authorId = authorDto.getId();
        this.name.setText(authorDto.getName());
    }

    public List<JFXButton> getControls() {
        List<JFXButton> list = Arrays.asList(this.saveButton, this.cancelButton);
        return list;
    }

    public JFXButton getSaveButton() {
        return saveButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
