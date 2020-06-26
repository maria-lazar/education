package com.spring.bugs.server.controller.developer;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.controller.LoginController;
import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.Bug;
import com.spring.bugs.server.domain.Developer;
import com.spring.bugs.server.service.BugsObserver;
import com.spring.bugs.server.service.BugsServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Optional;

public class DeveloperController extends UnicastRemoteObject implements BugsObserver, Serializable {
    private Developer developer;
    private BugsServices bugsServices;
    private Stage stage;
    private ObservableList<Bug> modelBugs = FXCollections.observableArrayList();
    private ObservableList<App> modelApps = FXCollections.observableArrayList();
    @FXML
    private TableView<Bug> bugsTableView;
    @FXML
    TableColumn<Bug, String> titleColumn;
    @FXML
    TableColumn<Bug, String> registererColumn;
    @FXML
    TableColumn<Bug, LocalDateTime> registeredColumn;
    @FXML
    ListView<App> appsListView;

    @FXML
    TextArea descriptionArea;

    public DeveloperController() throws RemoteException {
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("title"));
        registeredColumn.setCellValueFactory(new PropertyValueFactory<Bug, LocalDateTime>("created"));
        registererColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getRegisteredBy().getUsername()));
        bugsTableView.setItems(modelBugs);
        bugsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                descriptionArea.setText(newSelection.getDescription());
            }
        });
        appsListView.setItems(modelApps);
        appsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                modelBugs.setAll(bugsServices.getUnresolvedBugsOfApplication(newSelection));
            }
        });
    }

    public void setService(BugsServices bugsServices, Stage stage, Developer developer) {
        this.bugsServices = bugsServices;
        this.stage = stage;
        this.developer = developer;
        this.descriptionArea.setDisable(true);
        modelApps.setAll(bugsServices.getAllApps(developer.getCompany()));
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/loginView.fxml"));
            Parent parent = fxmlLoader.load();
            LoginController loginController = fxmlLoader.getController();
            Stage stage = new Stage();
            loginController.setService(bugsServices, stage);
            stage.setTitle("Login");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.setScene(new Scene(parent, 600, 400));
            bugsServices.removeObserver(this);
            this.stage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleResolve() {
        Bug bug = bugsTableView.getSelectionModel().getSelectedItem();
        if (bug == null) {
            AlertMessage.showMessage("A bug must be selected", Alert.AlertType.ERROR);
            return;
        }
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.setContentText("Are you sure you want to resolve this bug?");
        alert.getButtonTypes().add(confirm);
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> b = alert.showAndWait();
        if (!b.isEmpty() && b.get() == confirm) {
            try {
                bugsServices.resolveBug(developer, bug);
                AlertMessage.showMessage("Bug resolved!", Alert.AlertType.INFORMATION);
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
        App app = appsListView.getSelectionModel().getSelectedItem();
        if (app != null) {
            modelBugs.setAll(bugsServices.getUnresolvedBugsOfApplication(app));
        }
    }
}
