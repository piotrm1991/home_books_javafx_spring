package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.util.DialogMaker;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class SettingsController implements Initializable {

    @Autowired
    DialogMaker dialogMaker;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXButton statusTypeButton;
    @FXML
    private JFXButton roomListButton;
    @FXML
    private JFXButton shelfListButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void showStatusTypeList(ActionEvent actionEvent) {
        this.dialogMaker.showStatusTypeListDialog(MainController.PRIMARY_ROOT_PANE, this.rootAnchorPane);
    }

    @FXML
    public void showRoomList(ActionEvent actionEvent) {
        this.dialogMaker.showRoomListDialog(MainController.PRIMARY_ROOT_PANE, this.rootAnchorPane);
    }

    @FXML
    public void showShelfList(ActionEvent actionEvent) {
        this.dialogMaker.showShelvesListDialog(MainController.PRIMARY_ROOT_PANE, this.rootAnchorPane);
    }
}
