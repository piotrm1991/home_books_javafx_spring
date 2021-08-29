package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;

public interface ControllerForEntities extends Initializable {

    void inflateUI(EntityDto entityDto);

    JFXButton getCancelButton();

    JFXButton getSaveButton();
}
