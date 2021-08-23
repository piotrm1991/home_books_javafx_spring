package com.example.home_books_javafx_spring.util;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

public class AlertMaker {

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String headerText, String bodyText) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        if (controls.isEmpty()) {
            controls.add(new JFXButton("Okay"));
        }
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });
        });
        Label header = new Label(headerText);
        header.getStyleClass().add("dialog-header");
        Label body = new Label(bodyText);
        header.getStyleClass().add("dialog-header");
        dialogLayout.setHeading(header);
        dialogLayout.setBody(body);
        dialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur);
    }

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String headerText, Set<ConstraintViolation<EntityDto>> errors) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        if (controls.isEmpty()) {
            controls.add(new JFXButton("Okay"));
        }
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });
        });
        StringBuilder bodyText = new StringBuilder("");
        errors.stream().forEach(error -> bodyText.append(error.getMessage() + "\n"));
        Label header = new Label(headerText);
        header.getStyleClass().add("dialog-header");
        Label body = new Label(bodyText.toString());
        header.getStyleClass().add("dialog-header");
        dialogLayout.setHeading(header);
        dialogLayout.setBody(body);
        dialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur);
    }
}
