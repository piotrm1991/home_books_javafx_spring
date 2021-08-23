package com.example.home_books_javafx_spring;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Launcher extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage primaryStage) {
        this.applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(HomeBooksJavafxSpringApplication.class).run();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
