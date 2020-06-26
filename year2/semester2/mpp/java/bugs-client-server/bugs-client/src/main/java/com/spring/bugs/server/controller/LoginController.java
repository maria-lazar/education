package com.spring.bugs.server.controller;

import com.spring.bugs.server.controller.admin.AdminController;
import com.spring.bugs.server.controller.developer.DeveloperController;
import com.spring.bugs.server.controller.tester.TesterController;
import com.spring.bugs.server.domain.Admin;
import com.spring.bugs.server.domain.Developer;
import com.spring.bugs.server.domain.Employee;
import com.spring.bugs.server.domain.Tester;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import com.spring.bugs.server.service.BugsServices;

public class LoginController {

    private BugsServices bugsServices;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    Button loginButton;
    private Stage stage;

    public LoginController() {
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty()) {
            AlertMessage.showMessage("Username must be entered", Alert.AlertType.ERROR);
            return;
        }
        if (password.isEmpty()) {
            AlertMessage.showMessage("Password must be entered", Alert.AlertType.ERROR);
            return;
        }
        Employee employee = bugsServices.login(username, password);
        if (employee == null) {
            AlertMessage.showMessage("Invalid username or password", Alert.AlertType.ERROR);
            return;
        }
        if (employee instanceof Admin) {
            stage.close();
            createAdminStage(employee);
        }

        if (employee instanceof Developer) {
            stage.close();
            createDeveloperStage((Developer)employee);
        }

        if (employee instanceof Tester) {
            stage.close();
            createTesterStage((Tester) employee);
        }
    }

    private void createDeveloperStage(Developer developer) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/developer/developerView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage developerStage = new Stage();
            DeveloperController developerController = loader.getController();
            bugsServices.addObserver(developerController);
            developerController.setService(bugsServices, developerStage, developer);
            Scene developerScene = new Scene(root);
            developerStage.setTitle("Developer");
            developerStage.setScene(developerScene);
            developerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    developerController.handleLogout();
                }
            });
            developerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTesterStage(Tester employee) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/tester/testerView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage testerStage = new Stage();
            TesterController testerController = loader.getController();
            testerController.setService(employee, bugsServices, testerStage);
            Scene testerScene = new Scene(root, 600, 400);
            testerStage.setTitle("Tester");
            testerStage.setScene(testerScene);
            testerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    testerController.handleLogout();
                }
            });
            testerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAdminStage(Employee admin) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin/adminView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage adminStage = new Stage();
            AdminController adminController = loader.getController();
            bugsServices.addObserver(adminController);
            adminController.setService(admin, bugsServices, adminStage);
            Scene adminScene = new Scene(root, 600, 400);
            adminStage.setTitle("Admin");
            adminStage.setScene(adminScene);
            adminStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    adminController.handleLogout();
                }
            });
            adminStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setService(BugsServices bugsServices, Stage stage) {
        this.bugsServices = bugsServices;
        this.stage = stage;
    }
}
