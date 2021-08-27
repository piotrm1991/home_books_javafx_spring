package com.example.home_books_javafx_spring.util;

import com.example.home_books_javafx_spring.ui.AddAuthorController;
import com.example.home_books_javafx_spring.ui.AddBookController;
import com.example.home_books_javafx_spring.ui.AddPublisherController;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DialogMaker {

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String sceneLocation;

    public JFXDialog showAddEditPublisherDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "add_publisher.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddPublisherController controller = (AddPublisherController) fxmlLoader.getController();
            JFXButton cancelButton = controller.getCancelButton();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });

            Label header = new Label("Add Publisher");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
            return dialog;
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public JFXDialog showAddEditAuthorDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "add_author.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddAuthorController controller = (AddAuthorController) fxmlLoader.getController();
            JFXButton cancelButton = controller.getCancelButton();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });

            Label header = new Label("Add Author");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
            return dialog;
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public void showAddEditBookDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "add_book.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddBookController controller = (AddBookController) fxmlLoader.getController();
            JFXButton cancelButton = controller.getCancelButton();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });

            Label header = new Label("Add Book");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialogLayout.setMinWidth(700);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showBookListDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "book_list.fxml"));
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

            Label header = new Label("Book List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(1300);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showAuthorListDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "author_list.fxml"));
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

            Label header = new Label("Author List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(700);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showPublisherListDialog(StackPane rootPane, AnchorPane rootAnchorPane) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogMaker.class.getResource(this.sceneLocation + "publisher_list.fxml"));
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

            Label header = new Label("Publisher List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialogLayout.setActions(controls);
            dialogLayout.setMinWidth(700);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(DialogMaker.class.getName()).log(Level.SEVERE, null, e);
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

