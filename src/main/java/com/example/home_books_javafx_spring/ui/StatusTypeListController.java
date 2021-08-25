package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.StatusTypeService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.StatusTypeDto;
import com.example.home_books_javafx_spring.util.AlertMaker;
import com.example.home_books_javafx_spring.util.EntityValidator;
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
public class StatusTypeListController implements Initializable {

    @Autowired
    StatusTypeService statusTypeService;

    @Autowired
    EntityValidator entityValidator;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<StatusTypeUi> list = FXCollections.observableArrayList();

    @FXML
    public StackPane rootPane;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public TableView<StatusTypeUi> tableView;
    @FXML
    public TableColumn<StatusTypeUi, String> nameCol;
    @FXML
    public TableColumn<StatusTypeUi, String> nOfBooksCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }

    private void loadData() {
        this.list.clear();

        List<StatusTypeDto> result = this.statusTypeService.getAllStatusTypesDto();
        try {
            Iterator<StatusTypeDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new StatusTypeUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(StatusTypeListController.class.getName()).log(Level.SEVERE, null, e);
        }
        this.tableView.setItems(list);
    }

    private void initCol() {
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.nOfBooksCol.setCellValueFactory(new PropertyValueFactory<>("numberBooks"));
    }

    @FXML
    public void handleEditStatusTypeAction(ActionEvent actionEvent) {
        StatusTypeUi selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Status Type Selected", "Please Select Status Type For Edit");
            return;
        }

        StatusTypeDto selectedTypeForEdit = this.dtoMapper.fromStatusTypeUI(selectedForEdit);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_status_type.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddStatusTypeController controller = (AddStatusTypeController) fxmlLoader.getController();
            controller.inflateUI(selectedTypeForEdit);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Edit Status Type");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(StatusTypeListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {

        StatusTypeUi selectedItem = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedItem
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Status Type Selected", "Please Select The Status Type");
            return;
        }

        if (!selectedItem.getNumberBooks().equals("0")) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Can't Delete The Status Type", "There Are Sill Books Added To This Status Type");
            return;
        }

        StatusTypeDto selectedForDeletion = this.dtoMapper.fromStatusTypeUI(selectedItem);

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.statusTypeService.deleteStatusType(selectedForDeletion.getId());
            list.remove(selectedForDeletion);
            this.handleRefreshAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Status Type",
                "Are You Sure You Want To Delete This Status Type - " + selectedForDeletion.getName() + "?");

    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
        StatusTypeUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedUI
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select Publisher To Show Books");
            return;
        }

        StatusTypeDto selected = this.dtoMapper.fromStatusTypeUI(selectedUI);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "book_list.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            BookListController controller = (BookListController) fxmlLoader.getController();
            controller.inflateUI(selected);

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(MainController.PRIMARY_ROOT_PANE, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.getStyleClass().add("app.dialog-button");
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });
            List<JFXButton> controls = Arrays.asList(closeButton);

            Label header = new Label("Book List");
            header.getStyleClass().add("app.dialog-header");
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
    public void handleAddStatusTypeAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_status_type.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddStatusTypeController controller = (AddStatusTypeController) fxmlLoader.getController();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Add Status Type");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(StatusTypeListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    public static class StatusTypeUi {

        private Integer id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty numberBooks;

        public StatusTypeUi(StatusTypeDto statusTypeDto) {
            this.id = statusTypeDto.getId();
            this.name = new SimpleStringProperty(statusTypeDto.getName());
            this.numberBooks = new SimpleStringProperty(statusTypeDto.getNBooks().toString());
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public String getNumberBooks() {
            return numberBooks.get();
        }
    }
}
