package com.example.home_books_javafx_spring;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class HomeBooksJavafxSpringApplication {

    public static void main(String[] args) {
        Application.launch(Launcher.class, args);
    }

}
