package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.util.DialogMaker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MainController implements Initializable {

    public static StackPane PRIMARY_ROOT_PANE;

    @Autowired
    DialogMaker dialogMaker;

    @Autowired
    ApplicationContext applicationContext;

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
    private JFXTextField authorFirstNameInput;
    @FXML
    private JFXTextField authorLastNameInput;
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
        this.dialogMaker.showAddEditBookDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuAddPublisher(ActionEvent actionEvent) {
        this.dialogMaker.showAddEditPublisherDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuAddAuthor(ActionEvent actionEvent) {
        this.dialogMaker.showAddEditAuthorDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuViewBooks(ActionEvent actionEvent) {
        this.dialogMaker.showBookListDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuViewPublishers(ActionEvent actionEvent) {
        this.dialogMaker.showPublisherListDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuViewAuthors(ActionEvent actionEvent) {
        this.dialogMaker.showAuthorListDialog(this.rootPane, this.rootAnchorPane);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent actionEvent) {
        Stage stage = ((Stage) this.rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void searchBooks(ActionEvent actionEvent) {
//        String title = this.bookTitleInput.getText();
//        String authorFirstName = this.authorFirstNameInput.getText();
//        String authorLastName = this.authorLastNameInput.getText();
//        String publisherName = this.publisherNameInput.getText();
//
//        if (title.isEmpty()) {
//            JFXButton button = new JFXButton("OK");
//            AlertMaker.showMaterialDialog(this.rootPane, this.rootAnchorPane, Arrays.asList(button), "You Have To At Last Fill The Title to Search For Book!", null);
//            return;
//        }
//
//        List<Book> list = DatabaseHandler.getInstance().searchForBooksLazy(title, authorFirstName, authorLastName, publisherName);
//        if (list == null || list.size() <= 0) {
//            JFXButton button = new JFXButton("OK");
//            AlertMaker.showMaterialDialog(this.rootPane, this.rootAnchorPane, Arrays.asList(button), "No Books Found!", null);
//        } else {
//            this.showSearchResults(list);
//        }
//        DatabaseHandler.getInstance().closeSession();
    }

    private void showSearchResults() {
//    private void showSearchResults(List<Book> books) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/listBooks/book_list.fxml"));
//            StackPane parent = fxmlLoader.load();
//
//            BookListController controller = (BookListController) fxmlLoader.getController();
//            JFXButton closeButton = controller.inflateUI(books);
//
//            BoxBlur blur = new BoxBlur(3, 3, 3);
//
//            JFXDialogLayout dialogLayout = new JFXDialogLayout();
//            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
//
//            closeButton.getStyleClass().add("app.dialog-button");
//            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
//                dialog.close();
//            });
//            List<JFXButton> controls = Arrays.asList(closeButton);
//
//            Label header = new Label("Book List");
//            header.getStyleClass().add("app.dialog-header");
//            dialogLayout.setHeading(header);
//            dialogLayout.setBody(parent);
//            dialogLayout.setMinWidth(1300);
//            dialog.show();
//            dialogLayout.setActions(controls);
//            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
//                rootAnchorPane.setEffect(null);
//            });
//            rootAnchorPane.setEffect(blur);
//        } catch (IOException e) {
//            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, e);
//        }
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
