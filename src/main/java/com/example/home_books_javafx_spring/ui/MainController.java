package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.BookService;
import com.example.home_books_javafx_spring.dto.models.BookDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MainController implements Initializable {

    public static StackPane PRIMARY_ROOT_PANE;

    @Autowired
    private BookService bookService;

    @Autowired
    private DialogMaker dialogMaker;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private HBox book_info;
    @FXML
    private JFXTextField bookTitleInput;
    @FXML
    private JFXTextField authorNameInput;
    @FXML
    private JFXTextField publisherNameInput;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initDrawer();
        PRIMARY_ROOT_PANE = this.rootPane;
    }

    @FXML
    private void handleMenuClose(ActionEvent actionEvent) {
        ((Stage) this.rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleMenuAddBook(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addBook", null);
    }

    @FXML
    private void handleMenuAddPublisher(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addPublisher", null);
    }

    @FXML
    private void handleMenuAddAuthor(ActionEvent actionEvent) {
        this.dialogMaker.showDialog(this.rootPane, this.rootAnchorPane, "addAuthor", null);
    }

    @FXML
    private void handleMenuViewBooks(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "bookList", null);
    }

    @FXML
    private void handleMenuViewPublishers(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "publisherList", null);
    }

    @FXML
    private void handleMenuViewAuthors(ActionEvent actionEvent) {
        this.dialogMaker.showDialogList(this.rootPane, this.rootAnchorPane, "authorList", null);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent actionEvent) {
        Stage stage = ((Stage) this.rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void searchBooks(ActionEvent actionEvent) {
        String title = this.bookTitleInput.getText();
        String authorName = this.authorNameInput.getText();
        String publisherName = this.publisherNameInput.getText();

        if (title.isEmpty()) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(this.rootPane, this.rootAnchorPane, Arrays.asList(button), "You Have To At Last Fill The Title to Search For Book!", "");
            return;
        }

        List<BookDto> list = this.bookService.getBooksDtoByTitleAuthorPublisher(title, authorName, publisherName);
        if (list == null || list.size() <= 0) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(this.rootPane, this.rootAnchorPane, Arrays.asList(button), "No Books Found!", "");
        } else {
            if (list.size() > 1) {
                this.showSearchResults(list);
            } else {
                this.showOneBook(list.stream().findFirst().get());
            }

        }
    }

    private void showOneBook(BookDto bookDto) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_book.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            Parent parent = fxmlLoader.load();

            AddBookController controller = (AddBookController) fxmlLoader.getController();
            controller.inflateUI(bookDto);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });

            Label header = new Label("Edit Book");
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
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void showSearchResults(List<BookDto> books) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "book_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BookListController controller = (BookListController) fxmlLoader.getController();
            JFXButton closeButton = controller.inflateUI(books);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            closeButton.getStyleClass().add("dialog-button");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
            });
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Book List");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialogLayout.setMinWidth(1300);
            dialog.show();
            dialogLayout.setActions(controls);
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initDrawer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(this.scenesLocation + "toolbar.fxml"));
            loader.setControllerFactory(a -> this.applicationContext.getBean(a));
            VBox toolbar = loader.load();
            ToolbarController controller = loader.getController();
            controller.setRootPane(this.rootPane);
            controller.setRootAnchorPane(this.rootAnchorPane);
            this.drawer.setSidePane(toolbar);
            this.drawer.setMinWidth(0);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(this.hamburger);
        task.setRate(-1);
        this.hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            this.drawer.toggle();
        });
        this.drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate()
                         * -1);
            task.play();
            this.drawer.setMinWidth(150);
        });
        this.drawer.setOnDrawerClosed((event) -> {
            task.setRate(task.getRate()
                         * -1);
            task.play();
            this.drawer.setMinWidth(0);
        });
    }
}
