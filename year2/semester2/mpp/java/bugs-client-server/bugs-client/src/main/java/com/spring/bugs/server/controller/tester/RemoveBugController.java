package com.spring.bugs.server.controller.tester;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.Bug;
import com.spring.bugs.server.domain.Developer;
import com.spring.bugs.server.service.BugsObserver;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.spring.bugs.server.service.BugsServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Optional;

public class RemoveBugController extends UnicastRemoteObject implements BugsObserver, Serializable {
    private App app;
    private BugsServices bugsServices;
    private Stage stage;
    private ObservableList<Bug> modelBugs = FXCollections.observableArrayList();
    @FXML
    private TableView<Bug> bugsTableView;
    @FXML
    TableColumn<Bug, String> titleColumn;
    @FXML
    TableColumn<Bug, String> registererColumn;
    @FXML
    TableColumn<Bug, LocalDateTime> registeredColumn;
    @FXML
    TableColumn<Bug, LocalDateTime> resolvedColumn;
    @FXML
    TableColumn<Bug, String> resolverColumn;
    @FXML
    TableColumn<Bug, String> statusColumn;

    @FXML
    TextArea descriptionArea;

    public RemoveBugController() throws RemoteException {
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("title"));
        registeredColumn.setCellValueFactory(new PropertyValueFactory<Bug, LocalDateTime>("created"));
        resolvedColumn.setCellValueFactory(new PropertyValueFactory<Bug, LocalDateTime>("resolved"));
        statusColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStatus().toString()));
        resolverColumn.setCellValueFactory(cell -> {
            Developer dev = cell.getValue().getResolvedBy();
            if (dev != null) {
                return new ReadOnlyStringWrapper(dev.getUsername());
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });
        registererColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getRegisteredBy().getUsername()));
        bugsTableView.setItems(modelBugs);
        bugsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                descriptionArea.setText(newSelection.getDescription());
            }
        });
    }

    public void setService(BugsServices bugsServices, Stage stage, App application) {
        this.bugsServices = bugsServices;
        this.stage = stage;
        this.app = application;
        this.descriptionArea.setDisable(true);
        modelBugs.setAll(bugsServices.getAllBugsOfApplication(app));
    }

    @FXML
    public void handleDelete() {
        Bug bug = bugsTableView.getSelectionModel().getSelectedItem();
        if (bug == null) {
            AlertMessage.showMessage("Bug must be selected", Alert.AlertType.ERROR);
            return;
        }
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.setContentText("Are you sure you want to delete the bug?");
        alert.getButtonTypes().add(confirm);
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> b = alert.showAndWait();
        if (!b.isEmpty() && b.get() == confirm) {
            try {
                bugsServices.deleteBug(bug);
                AlertMessage.showMessage("Bug deleted", Alert.AlertType.INFORMATION);
                descriptionArea.setText("");
            } catch (Exception e) {
                AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
    }

    @Override
    public void updateEmployeeList() throws RemoteException {

    }

    @Override
    public void updateBugsList() throws RemoteException {
        modelBugs.setAll(bugsServices.getAllBugsOfApplication(app));
    }
}
