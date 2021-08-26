package com.example.home_books_javafx_spring.ui;

import com.example.home_books_javafx_spring.database.service.RoomService;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.RoomDto;
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
public class RoomListController implements Initializable {

    @Autowired
    RoomService roomService;

    @Autowired
    DtoMapper dtoMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.application.ui.scene.location}")
    private String scenesLocation;

    ObservableList<RoomUi> list = FXCollections.observableArrayList();

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TableView<RoomUi> tableView;
    @FXML
    private TableColumn<RoomUi, String> nameCol;
    @FXML
    private TableColumn<RoomUi, String> nOfShelves;
    @FXML
    private TableColumn<RoomUi, String> nOfBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initCol();
        this.loadData();
    }

    private void loadData() {
        this.list.clear();

        List<RoomDto> result = this.roomService.getAllRoomsDto();
        try {
            Iterator<RoomDto> iterator = result.listIterator();
            while (iterator.hasNext()) {
                this.list.add(new RoomUi(iterator.next()));
            }
        } catch (Exception e) {
            Logger.getLogger(RoomListController.class.getName()).log(Level.SEVERE, null, e);
        }
        this.tableView.setItems(list);
    }

    private void initCol() {
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.nOfShelves.setCellValueFactory(new PropertyValueFactory<>("numberShelves"));
        this.nOfBooks.setCellValueFactory(new PropertyValueFactory<>("numberBooks"));
    }

    @FXML
    public void handleEditRoomAction(ActionEvent actionEvent) {
        RoomUi selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Room Selected", "Please Select Room For Edit");
            return;
        }

        RoomDto selectedTypeForEdit = this.dtoMapper.fromRoomUI(selectedForEdit);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_room.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddRoomController controller = (AddRoomController) fxmlLoader.getController();
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

            Label header = new Label("Edit Room");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(RoomListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {

        RoomUi selectedItem = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedItem
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Room Selected", "Please Select The Room");
            return;
        }

        if (!selectedItem.getNumberShelves().equals("0")) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "Can't Delete The Room", "There Are Sill Shelves Added To This Room");
            return;
        }

        RoomDto selectedForDeletion = this.dtoMapper.fromRoomUI(selectedItem);

        JFXButton yButton = new JFXButton("YES");
        JFXButton nButton = new JFXButton("NO");
        yButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            this.roomService.deleteRoomById(selectedForDeletion.getId());
            this.handleRefreshAction(new ActionEvent());
        });
        AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(yButton, nButton), "Delete Room",
                "Are You Sure You Want To Delete This Room - "
                + selectedForDeletion.getName()
                + "?");

    }

    @FXML
    public void handleShowBooksListAction(ActionEvent actionEvent) {
        RoomUi selectedUI = this.tableView.getSelectionModel().getSelectedItem();

        if (selectedUI
            == null) {
            JFXButton button = new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootPane, rootAnchorPane, Arrays.asList(button), "No Publisher Selected", "Please Select Publisher To Show Books");
            return;
        }

        RoomDto selected = this.dtoMapper.fromRoomUI(selectedUI);

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
    public void handleAddRoomAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.scenesLocation + "add_room.fxml"));
            fxmlLoader.setControllerFactory(a -> this.applicationContext.getBean(a));
            StackPane parent = fxmlLoader.load();

            AddRoomController controller = (AddRoomController) fxmlLoader.getController();

            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton cancelButton = controller.getCancelButton();
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                dialog.close();
                this.handleRefreshAction(new ActionEvent());
            });

            Label header = new Label("Add Room");
            header.getStyleClass().add("dialog-header");
            dialogLayout.setHeading(header);
            dialogLayout.setBody(parent);
            dialog.show();
            dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
                rootAnchorPane.setEffect(null);
            });
            rootAnchorPane.setEffect(blur);
        } catch (IOException e) {
            Logger.getLogger(RoomListController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleRefreshAction(ActionEvent actionEvent) {
        this.loadData();
    }

    @FXML
    public void handleShowShelvesListAction(ActionEvent actionEvent) {

    }

    public static class RoomUi {

        private Integer id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty numberShelves;
        private final SimpleStringProperty numberBooks;

        public RoomUi(RoomDto roomDto) {
            this.id = roomDto.getId();
            this.name = new SimpleStringProperty(roomDto.getName());
            this.numberShelves = new SimpleStringProperty(roomDto.getNShelves().toString());
            this.numberBooks = new SimpleStringProperty(roomDto.getNBooks().toString());
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public String getNumberShelves() {
            return numberShelves.get();
        }

        public String getNumberBooks() {
            return numberBooks.get();
        }
    }
}
