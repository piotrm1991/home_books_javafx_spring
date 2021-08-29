package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.dto.models.EntityDto;
import javafx.fxml.Initializable;

public interface ControllerForList extends Initializable {
    void inflateUI(EntityDto entity);
}
