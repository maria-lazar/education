package com.spring.bugs.server.controller.admin;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.domain.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import com.spring.bugs.server.service.BugsServices;


public class EditEmployeeController {
    ObservableList<String> modelTypes = FXCollections.observableArrayList();
    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    @FXML
    ChoiceBox<String> typeChoice;
    private BugsServices bugsServices;
    private Stage stage;
    private int editType;
    private Employee employee;

    @FXML
    public void handleSave() {
        if (editType == 0) {
            handleCreate();
        } else {
            handleModify();
        }

    }

    private void handleModify() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            AlertMessage.showMessage("Password must be entered", Alert.AlertType.ERROR);
            return;
        }
        try {
            Employee e = bugsServices.updateEmployee(employee, password);
            AlertMessage.showMessage("Employee updated", Alert.AlertType.INFORMATION);
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } catch (Exception e) {
            AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleCreate() {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            AlertMessage.showMessage("Username must be entered", Alert.AlertType.ERROR);
            return;
        }
        String type = typeChoice.getValue();
        if (type == null) {
            AlertMessage.showMessage("Type must be selected", Alert.AlertType.ERROR);
            return;
        }
        try {
            Employee e = bugsServices.addEmployee(username, type, employee.getCompany());
            AlertMessage.showMessage("Employee added", Alert.AlertType.INFORMATION);
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } catch (Exception e) {
            AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }

    @FXML
    public void initialize() {
        modelTypes.setAll("Developer", "Tester", "Admin");
        typeChoice.setItems(modelTypes);
    }

    public void setService(Employee employee, BugsServices bugsServices, Stage editStage, int editType) {
        this.bugsServices = bugsServices;
        this.stage = editStage;
        this.editType = editType;
        this.employee = employee;
        if (editType == 0) {
            passwordField.setDisable(true);
        } else {
            usernameField.setText(employee.getUsername());
            passwordField.setText(employee.getPassword());
            typeChoice.setValue(employee.getClass().getSimpleName());
            typeChoice.setDisable(true);
            usernameField.setDisable(true);
        }
    }
}
