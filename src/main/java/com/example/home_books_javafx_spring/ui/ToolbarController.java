package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.util.DialogMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ToolbarController implements Initializable {

    @Autowired
    DialogMaker dialogMaker;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleAddBookAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addBook", null);
    }

    @FXML
    public void handleAddAuthorAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addAuthor", null);
    }

    @FXML
    public void handleAddPublisherAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addPublisher", null);
    }

    @FXML
    public void handleShowBookListAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "bookList", null);
    }

    @FXML
    public void handleShowAuthorListAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "authorList", null);
    }

    @FXML
    public void handleShowPublisherListAction(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "publisherList", null);
    }

    @FXML
    public void handleShowSettingsAction(ActionEvent actionEvent) {
        this.dialogMaker.showSettingsDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    public void handleCloseAction(ActionEvent actionEvent) {
        ((Stage) this.rootPane.getScene().getWindow()).close();
    }

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setRootAnchorPane(AnchorPane rootAnchorPane) {
        this.rootAnchorPane = rootAnchorPane;
    }
}
