package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.AuthorService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@Component
public class AddAuthorController implements Initializable {

    @Autowired
    AuthorService authorService;

    @Autowired
    EntityValidator entityValidator;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Integer authorId;

    private boolean inEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
//        if (!inEditMode) {
//            Stage stage = (Stage) rootPane.getScene().getWindow();
//            stage.close();
//        }
    }

    public void addAuthor(ActionEvent actionEvent) {

        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();

        AuthorDto authorDto = AuthorDto.builder()
                .id(this.authorId)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        Set<ConstraintViolation<EntityDto>> errors = this.entityValidator.validateEntity(authorDto);

        if (errors.isEmpty()) {
            this.authorService.addAuthor(authorDto);
        } else {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Error", errors);
            return;
        }
    }

    public void inflateUI(AuthorDto authorDto) {
        this.authorId = authorDto.getId();
        this.firstName.setText(authorDto.getFirstName());
        this.lastName.setText(authorDto.getLastName());

        this.inEditMode = true;
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
}
