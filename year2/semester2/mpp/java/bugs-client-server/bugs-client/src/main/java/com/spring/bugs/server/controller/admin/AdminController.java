package com.spring.bugs.server.controller.admin;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.controller.LoginController;
import com.spring.bugs.server.domain.Employee;
import com.spring.bugs.server.service.BugsObserver;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import com.spring.bugs.server.service.BugsServices;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

public class AdminController extends UnicastRemoteObject implements BugsObserver, Serializable {
    ObservableList<Employee> modelEmployees = FXCollections.observableArrayList();
    @FXML
    TableView<Employee> employeeTableView;
    @FXML
    TableColumn<Employee, String> usernameColumn;
    @FXML
    TableColumn<Employee, String> passwordColumn;
    @FXML
    TableColumn<Employee, String> typeColumn;
    private Employee admin;
    private BugsServices bugsServices;
    private Stage stage;

    public AdminController() throws RemoteException {
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("password"));
        typeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getClass().getSimpleName()));
        employeeTableView.setItems(modelEmployees);
    }

    public void setService(Employee admin, BugsServices bugsServices, Stage adminStage) {
        this.admin = admin;
        this.bugsServices = bugsServices;
        modelEmployees.setAll(bugsServices.getAllEmployeesByCompany(admin.getCompany()));
        this.stage = adminStage;
    }

    @FXML
    public void handleCreate() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin/editEmployeeView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage editStage = new Stage();
            EditEmployeeController editEmployeeController = loader.getController();
            editEmployeeController.setService(admin, bugsServices, editStage, 0);
            Scene editScene = new Scene(root);
            editStage.setTitle("Create");
            editStage.setScene(editScene);
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
        if (employee == null) {
            AlertMessage.showMessage("Employee must be selected", Alert.AlertType.ERROR);
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin/editEmployeeView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage editStage = new Stage();
            EditEmployeeController editEmployeeController = loader.getController();
            editEmployeeController.setService(employee, bugsServices, editStage, 1);
            Scene editScene = new Scene(root);
            editStage.setTitle("Modify");
            editStage.setScene(editScene);
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.show();
        } catch (Exception e) {
//            LOGGER.warn("handle login failed " + e);
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDelete() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
        if (employee == null) {
            AlertMessage.showMessage("Employee must be selected", Alert.AlertType.ERROR);
            return;
        }
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.setContentText("Are you sure you want to delete this employee?");
        alert.getButtonTypes().add(confirm);
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> b = alert.showAndWait();
        if (!b.isEmpty() && b.get() == confirm) {
            try {
                bugsServices.deleteEmployee(employee);
                AlertMessage.showMessage("Employee deleted", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
    }

    @FXML
    public void handleLogout() {
        try {
            bugsServices.removeObserver(this);
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
            this.stage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployeeList() throws RemoteException {
        modelEmployees.setAll(bugsServices.getAllEmployeesByCompany(admin.getCompany()));
    }

    @Override
    public void updateBugsList() throws RemoteException {

    }
}


