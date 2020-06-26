package com.spring.bugs.server.controller.tester;

import com.spring.bugs.server.controller.AlertMessage;
import com.spring.bugs.server.domain.App;
import com.spring.bugs.server.domain.Tester;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.spring.bugs.server.service.BugsServices;

import java.util.Optional;

public class RegisterController {
    private App app;
    private BugsServices bugsServices;
    private Stage stage;
    @FXML
    public TextField appField;
    @FXML
    public TextField titleField;
    @FXML
    public TextArea descriptionArea;
    private Tester tester;

    public void setService(BugsServices bugsServices, Stage stage, App application, Tester tester) {
        this.bugsServices = bugsServices;
        this.stage = stage;
        this.app = application;
        appField.setText(application.getName());
        appField.setDisable(true);
        this.tester = tester;
    }

    @FXML
    public void handleRegister() {
        String name = titleField.getText();
        if (name.equals("")) {
            AlertMessage.showMessage("Name must be entered", Alert.AlertType.ERROR);
            return;
        }
        String description = descriptionArea.getText();
        if (description.equals("")) {
            AlertMessage.showMessage("Description must be entered", Alert.AlertType.ERROR);
            return;
        }
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.setContentText("Are you sure you want to register the bug?");
        alert.getButtonTypes().add(confirm);
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> b = alert.showAndWait();
        if (!b.isEmpty() && b.get() == confirm) {
            try {
                bugsServices.registerBug(name, description, app, tester);
                AlertMessage.showMessage("Bug registered", Alert.AlertType.INFORMATION);
                titleField.clear();
                descriptionArea.clear();
            } catch (Exception e) {
                AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }
    }
}
