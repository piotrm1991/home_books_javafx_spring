package com.example.home_books_javafx_spring.util;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.example.home_books_javafx_spring.ui.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DialogMaker {

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String sceneLocation;

    public Map<String, Object> showDialog(StackPane rootPane, AnchorPane rootAnchorPane, String dialogType, EntityDto entityDto) {

        String location = this.sceneLocation;
        String headerString;
        boolean edit = false;
        Integer minWidth = null;

        switch (dialogType) {
            case "addAuthor": {
                location = location + "add_author.fxml";
                headerString = "Add Author";
                break;
            }
            case "editAuthor": {
                location = location + "add_author.fxml";
                headerString = "Edit Author";
                edit = true;
                break;
            }
            case "addPublisher": {
                location = location + "add_publisher.fxml";
                headerString = "Add Publisher";
                break;
            }
            case "editPublisher": {
                location = location + "add_publisher.fxml";
                headerString = "Edit Publisher";
                edit = true;
                break;
            }
            case "addBook": {
                location = location + "add_book.fxml";
                headerString = "Add Book";
                minWidth = 700;
                break;
            }
            case "editBook": {
                location = location + "add_book.fxml";
                headerString = "Edit Book";
                edit = true;
                minWidth = 700;
                break;
            }
            default: {
                headerString = "Header";
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(location));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            ControllerForEntities controller = fxmlLoader.getController();
            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            if (edit) {
                controller.inflateUI(entityDto);
            }

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });

            Label header = new Label(headerString);
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);

            if (minWidth != null) {
                dialogLayout.setMinWidth(700);
            }

            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);

            Map<String, Object> controls = new HashMap<>();
            controls.put("dialog", dialog);
            controls.put("save", saveButton);
            controls.put("cancel", cancelButton);
            return controls;
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public JFXButton showDialogList(StackPane rootPane, AnchorPane rootAnchorPane, String dialogType, EntityDto selected) {
        String location = this.sceneLocation;
        String headerString;
        Integer minWidth = null;

        switch (dialogType) {
            case "bookList": {
                location = location + "book_list.fxml";
                headerString = "Book List";
                minWidth = 1300;
                break;
            }
            case "authorList": {
                location = location + "author_list.fxml";
                headerString = "Author List";
                minWidth = 700;
                break;
            }
            case "publisherList": {
                location = location + "publisher_list.fxml";
                headerString = "Publisher List";
                minWidth = 700;
                break;
            }
            default: {
                headerString = "Header";
            }
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(location));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            if (selected != null) {
                ControllerForList controller = fxmlLoader.getController();
                controller.inflateUI(selected);
            }

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            closeButton.getStyleClass().add("dialog-button");
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label(headerString);
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            if (minWidth != null) {
                dialogLayout.setMinWidth(minWidth);
            }
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);

            return closeButton;
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public void showSettingsDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "settings.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            closeButton.getStyleClass().add("dialog-button");
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Settings");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMaxWidth(250);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showStatusTypeListDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "status_type_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            closeButton.getStyleClass().add("dialog-button");
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Status Type List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(400);
            dialogLayout.setMinHeight(500);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showRoomListDialog(StackPane primaryRootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "room_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(primaryRootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            closeButton.getStyleClass().add("dialog-button");
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Room List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(400);
            dialogLayout.setMinHeight(500);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showShelvesListDialog(StackPane primaryRootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "shelf_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(primaryRootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            closeButton.getStyleClass().add("dialog-button");
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Shelf List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(600);
            dialogLayout.setMinHeight(500);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

