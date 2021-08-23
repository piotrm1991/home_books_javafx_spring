package com.example.home_books_javafx_spring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("${spring.application.ui.resource.main.xml}")
    private Resource mainResource;

    private String applicationTitle;

    private ApplicationContext applicationContext;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.mainResource.getURL());
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
