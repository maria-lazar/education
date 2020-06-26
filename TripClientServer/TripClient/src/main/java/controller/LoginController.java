package controller;

import domain.Account;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.TripServices;


public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    private Stage stage;
    private TripServices tripServices;

    @FXML
    public void handleLogin(ActionEvent event) {
        LOGGER.traceEntry("handle login");
        String name = nameField.getText();
        String password = passwordField.getText();
        if (name.isEmpty()) {
            AlertMessage.showMessage("Name must be entered", Alert.AlertType.ERROR);
            return;
        }
        if (password.isEmpty()) {
            AlertMessage.showMessage("Password must be entered", Alert.AlertType.ERROR);
            return;
        }
        Account account = tripServices.getAccountByNamePassword(name, password);
        if (account == null) {
            AlertMessage.showMessage("Invalid name or password", Alert.AlertType.ERROR);
            return;
        }
        stage.close();
        nameField.clear();
        passwordField.clear();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainView.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage mainStage = new Stage();
            MainController mainController = loader.getController();
            tripServices.addTripObserver(mainController);
            mainController.setService(account, tripServices, mainStage);
            Scene mainScene = new Scene(root, 1000, 400);
            mainStage.setTitle(account.getName());
            mainStage.setScene(mainScene);
            mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainController.handleLogout();
                }
            });
            mainStage.show();
        } catch (Exception e) {
            LOGGER.warn("handle login failed " + e);
            e.printStackTrace();
        }
    }

    public void setService(TripServices tripServices, Stage primaryStage) {
        this.tripServices = tripServices;
        this.stage = primaryStage;
    }
}
