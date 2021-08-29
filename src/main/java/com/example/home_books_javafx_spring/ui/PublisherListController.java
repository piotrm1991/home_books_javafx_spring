package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.PublisherService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.PublisherDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.DialogMaker;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PublisherListController implements Initializable {

    @Autowired
    DialogMaker dialogMaker;

    @Autowired
    PublisherService publisherService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<PublisherUi> list = FXCollections.observableArrayList();

    @FXML
    public StackPane rootPane;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public TableColumn<PublisherUi, String> nameCol;
    @FXML
    public TableColumn<PublisherUi, String> nOfBooksCol;
    @FXML
    public TableView<PublisherUi> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }


    private void loadData() {
        this.list.clear();

        List<PublisherDto> result = this.publisherService.getAllPublishersDto();
        try {
            Iterator<PublisherDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new PublisherUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(PublisherListController.class.getName()).log(Level.SEVERE, null, e);
        }
        this.tableView.setItems(list);
    }

    private void initCol() {
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.nOfBooksCol.setCellValueFactory(new PropertyValueFactory<>("numberBooks"));
    }

    @FXML
    public void handleEditPublisherAction(ActionEvent actionEvent) {
        PublisherUi selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select Publisher For Edit");
            return;
        }

        PublisherDto selectedPublisherForEdit = this.dtoMapper.fromPublisherUI(selectedForEdit);

        Map<String, Object> controls = this.dialogMaker.showDialog(rootPane, rootAnchorPane, "editPublisher", selectedPublisherForEdit);
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
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select The Publisher");
            return;
        }

        PublisherDto selectedForDeletion = this.dtoMapper.fromPublisherUI(this.tableView.getSelectionModel().getSelectedItem());

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.publisherService.deletePublisher(selectedForDeletion.getId());
            this.handleRefreshAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Publisher", "Are You Sure You Want To Delete Publisher - "
                                                                                                                     + selectedForDeletion.getName()
                                                                                                                     + " ?");
    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
        PublisherUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedUI
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select Publisher To Show Books");
            return;
        }

        PublisherDto selected = this.dtoMapper.fromPublisherUI(selectedUI);

        JFXButton button = this.dialogMaker.showDialogList(rootPane, rootAnchorPane, "bookList", selected);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.handleRefreshAction(new ActionEvent());
        });
    }

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    public static class PublisherUi {

        private Integer id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty numberBooks;

        public PublisherUi(PublisherDto publisherDto) {
            this.id = publisherDto.getId();
            this.name = new SimpleStringProperty(publisherDto.getName());
            this.numberBooks = new SimpleStringProperty(publisherDto.getNBooks().toString());
        }

        public String getName() {
            return name.get();
        }

        public String getNumberBooks() {
            return numberBooks.get();
        }

        public Integer getId() {
            return id;
        }
    }
}
