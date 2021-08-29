package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.AuthorService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.AuthorDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthorListController implements Initializable {

    @Autowired
    DialogMaker dialogMaker;

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
    public TableColumn<AuthorUi, String> nameCol;
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
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.nOfBooksCol.setCellValueFactory(new PropertyValueFactory<>("numberBooks"));
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

        Map<String, Object> controls = this.dialogMaker.showDialog(rootPane, rootAnchorPane, "editAuthor", selectedAuthorForEdit);
        JFXButton button = (JFXButton) controls.get("cancel");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.handleRefreshAction(new ActionEvent());
        });
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
                                                                                                                  + selectedForDeletion.getName()
                                                                                                                  + "?");
    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
        AuthorUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedUI
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Author Selected", "Please Select Author To Show Books");
            return;
        }

        AuthorDto selected = this.dtoMapper.fromAuthorUI(selectedUI);

        JFXButton button = this.dialogMaker.showDialogList(rootPane, rootAnchorPane, "bookList", selected);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.handleRefreshAction(new ActionEvent());
        });
    }

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    public static class AuthorUi {

        private Integer id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty numberBooks;

        public AuthorUi(AuthorDto authorDto) {
            this.id = authorDto.getId();
            this.name = new SimpleStringProperty(authorDto.getName());
            this.numberBooks = new SimpleStringProperty(authorDto.getNBooks().toString());
        }

        public String getName() {
            return name.get();
        }

        public Integer getId() {
            return id;
        }

        public String getNumberBooks() {
            return numberBooks.get();
        }
    }
}
