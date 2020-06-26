package com.spring.bugs.server.controller.tester;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.controller.LoginController;
import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.Tester;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.spring.bugs.server.service.BugsServices;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;

public class TesterController {
    private Tester tester;
    private BugsServices bugsServices;
    private Stage stage;
    private ObservableList<App> modelApps = FXCollections.observableArrayList();
    @FXML
    private ListView<App> applicationListView;

    public void setService(Tester employee, BugsServices bugsServices, Stage testerStage) {
        this.tester = employee;
        this.bugsServices = bugsServices;
        this.stage = testerStage;
        List<App> apps = bugsServices.getAllApps(tester.getCompany());
        modelApps.setAll(apps);
    }

    @FXML
    public void initialize() {
        applicationListView.setItems(modelApps);
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
            this.stage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleDelete() {
        App application = applicationListView.getSelectionModel().getSelectedItem();
        if (application == null) {
            AlertMessage.showMessage("App must be selected", Alert.AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/tester/removeBugView.fxml"));
            Parent parent = fxmlLoader.load();
            RemoveBugController removeBugController = fxmlLoader.getController();
            Stage stage = new Stage();
            bugsServices.addObserver(removeBugController);
            removeBugController.setService(bugsServices, stage, application);
            stage.setTitle("Delete bugs");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    bugsServices.removeObserver(removeBugController);
                }
            });
            stage.setScene(new Scene(parent, 600, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleRegister() {
        App application = applicationListView.getSelectionModel().getSelectedItem();
        if (application == null) {
            AlertMessage.showMessage("App must be selected", Alert.AlertType.ERROR);
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/tester/registerView.fxml"));
            Parent parent = fxmlLoader.load();
            RegisterController registerController = fxmlLoader.getController();
            Stage stage = new Stage();
            registerController.setService(bugsServices, stage, application, tester);
            stage.setTitle("Register bugs");
            stage.setScene(new Scene(parent, 600, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
