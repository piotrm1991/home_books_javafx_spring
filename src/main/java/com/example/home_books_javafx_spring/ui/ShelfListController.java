package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.ShelfService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.ShelfDto;
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
public class ShelfListController implements Initializable {

    @Autowired
    ShelfService shelfService;

    @Autowired
    EntityValidator entityValidator;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<ShelfUi> list = FXCollections.observableArrayList();

    @FXML
    public StackPane rootPane;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public TableView<ShelfUi> tableView;
    @FXML
    public TableColumn<ShelfUi, String> roomNameCol;
    @FXML
    public TableColumn<ShelfUi, String> letter;
    @FXML
    public TableColumn<ShelfUi, String> number;
    @FXML
    public TableColumn<ShelfUi, String> nOfBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }

    private void loadData() {
        this.list.clear();

        List<ShelfDto> result = this.shelfService.getAllShelvesDto();
        try {
            Iterator<ShelfDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new ShelfUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(ShelfListController.class.getName()).log(Level.SEVERE, null, e);
        }

        this.tableView.setItems(list);
    }

    private void initCol() {
        this.roomNameCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        this.letter.setCellValueFactory(new PropertyValueFactory<>("letter"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.nOfBooks.setCellValueFactory(new PropertyValueFactory<>("numberBooks"));
    }

    @FXML
    public void handleEditShelfAction(ActionEvent actionEvent) {
        ShelfUi selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Room Selected", "Please Select Room For Edit");
            return;
        }

        ShelfDto selectedTypeForEdit = this.dtoMapper.fromShelfUI(selectedForEdit);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_shelf.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddShelfController controller = (AddShelfController) fxmlLoader.getController();
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

            Label header = new Label("Edit Shelf");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
                this.handleRefreshAction(new ActionEvent());
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(ShelfListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {

        ShelfUi selectedItem = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedItem
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Shelf Selected", "Please Select The Shelf");
            return;
        }

        if (!selectedItem.getNumberBooks().equals("0")) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Can't Delete The Shelf", "There Are Sill Books Added To Shelf");
            return;
        }

        ShelfDto selectedForDeletion = this.dtoMapper.fromShelfUI(selectedItem);

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.shelfService.deleteShelf(selectedForDeletion.getId());
            this.handleRefreshAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Status Type",
                "Are You Sure You Want To Delete This Shelf ?");

    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
        ShelfUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedUI
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select Publisher To Show Books");
            return;
        }

        ShelfDto selected = this.dtoMapper.fromShelfUI(selectedUI);

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
    public void handleAddShelfAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_shelf.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddShelfController controller = (AddShelfController) fxmlLoader.getController();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton saveButton = controller.getSaveButton();
            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Add Shelf");
            header.getStyleClass().add("app.dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(ShelfListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    public static class ShelfUi {

        private Integer id;
        private Integer roomId;
        private final SimpleStringProperty roomName;
        private final SimpleStringProperty letter;
        private final SimpleStringProperty number;
        private final SimpleStringProperty numberBooks;

        public ShelfUi(ShelfDto shelfDto) {
            this.id = shelfDto.getId();
            this.roomId = shelfDto.getRoomDto().getId();
            this.roomName = new SimpleStringProperty(shelfDto.getRoomDto().getName());
            this.letter = new SimpleStringProperty(shelfDto.getLetter());
            this.number = new SimpleStringProperty(shelfDto.getNumber().toString());
            this.numberBooks = new SimpleStringProperty(shelfDto.getNBooks().toString());
        }

        public Integer getId() {
            return id;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public String getRoomName() {
            return roomName.get();
        }

        public String getLetter() {
            return letter.get();
        }

        public String getNumber() {
            return number.get();
        }

        public String getNumberBooks() {
            return numberBooks.get();
        }
    }
}
