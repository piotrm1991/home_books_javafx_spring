package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.entities.Author;
import com.example.home_books_javafx_spring.database.service.AuthorService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthorListController implements Initializable {

    @Autowired
    AuthorService authorService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<AuthorUi> list = FXCollections.observableArrayList();

    @FXML
    public StackPane rootPane;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public TableColumn<AuthorUi, String> firstNameCol;
    @FXML
    public TableColumn<AuthorUi, String> lastNameCol;
    @FXML
    public TableColumn<AuthorUi, String> nOfBooksCol;
    @FXML
    public TableView<AuthorUi> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }


    private void loadData() {
        this.list.clear();

        List<AuthorDto> result = this.authorService.getAllAuthorsDto();
        try {
            Iterator<AuthorDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new AuthorUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(AuthorListController.class.getName()).log(Level.SEVERE, null, e);
        }
        this.tableView.setItems(list);
    }

    private void initCol() {
        this.firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//        this.nOfBooksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfBooks"));
    }

    @FXML
    public void handleEditAuthorAction(ActionEvent actionEvent) {
        AuthorUi selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Author Selected", "Please Select Author For Edit");
            return;
        }

        AuthorDto selectedAuthorForEdit = this.dtoMapper.fromAuthorUI(selectedForEdit);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_author.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddAuthorController controller = (AddAuthorController) fxmlLoader.getController();
            controller.inflateUI(selectedAuthorForEdit);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Edit Author");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(AuthorListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {

        if (this.tableView.getSelectionModel().getSelectedItem()
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Author Selected", "Please Select The Author");
            return;
        }

        AuthorDto selectedForDeletion = this.dtoMapper.fromAuthorUI(this.tableView.getSelectionModel().getSelectedItem());

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.authorService.deleteAuthor(selectedForDeletion.getId());
            this.handleRefreshAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Author", "Are You Sure You Want To Delete Book - "
                                                                                                                  + selectedForDeletion.getFirstName()
                                                                                                                  + " "
                                                                                                                  + selectedForDeletion.getLastName()
                                                                                                                  + "?");
    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
//        AuthorUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();
//
//        if (selectedUI
//            == null) {
//            JFXButton button = new JFXButton("OK");
//            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Author Selected", "Please Select Author To Show Books");
//            return;
//        }
//
//        Author selected = selectedUI.getAuthor();
//
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/listBooks/book_list.fxml"));
//            StackPane parent = fxmlLoader.load();
//
//            BookListController controller = (BookListController) fxmlLoader.getController();
//            controller.inflateUI(selected);
//
//            BoxBlur blur = new BoxBlur(3, 3, 3);
//
//            JFXDialogLayout dialogLayout = new JFXDialogLayout();
//            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
//
//            JFXButton closeButton = new JFXButton("Close");
//            closeButton.getStyleClass().add("app.dialog-button");
//            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
//                dialog.close();
//                this.handleRefreshAction(new ActionEvent());
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

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    public static class AuthorUi {

        private Integer id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
//        private final SimpleStringProperty numberBooks;

        public AuthorUi(AuthorDto authorDto) {
            this.id = authorDto.getId();
            this.firstName = new SimpleStringProperty(authorDto.getFirstName());
            this.lastName = new SimpleStringProperty(authorDto.getLastName());
//            this.numberBooks = new SimpleStringProperty(authorDto.getNBooks().toString());
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public Integer getId() {
            return id;
        }

        //        public String getNumberBooks() {
//            return numberBooks.get();
//        }
    }
}
