package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.PublisherService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.PublisherDto;
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
public class PublisherListController implements Initializable {

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

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_publisher.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddPublisherController controller = (AddPublisherController) fxmlLoader.getController();
            controller.inflateUI(selectedPublisherForEdit);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Edit Publisher");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(PublisherListController.class.getName()).log(Level.SEVERE, null, e);
        }
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

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "book_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BookListController controller = (BookListController) fxmlLoader.getController();
            controller.inflateUI(selected);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.getStyleClass().add("dialog-button");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
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
