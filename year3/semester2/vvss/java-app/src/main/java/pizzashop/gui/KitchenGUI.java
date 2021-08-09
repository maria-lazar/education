package pizzashop.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import pizzashop.controller.KitchenGUIController;
import pizzashop.controller.MainGUIController;

import java.io.IOException;
import java.util.Optional;


public class KitchenGUI {

    public KitchenGUI() {
        VBox vBoxKitchen = null;

        try {
            vBoxKitchen = FXMLLoader.load(getClass().getResource("/fxml/kitchenGUIFXML.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Kitchen");
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (KitchenGUIController.order.size() == 0) {
                        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to exit Kitchen window?", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> result = exitAlert.showAndWait();
                        if (result.get() == ButtonType.YES) {
                            //Stage stage = (Stage) this.getScene().getWindow();
                            stage.close();
                        }
                        // consume event
                        else if (result.get() == ButtonType.NO) {
                            event.consume();
                        } else {
                            event.consume();
                        }
                    } else {
                        Alert exitAlert = new Alert(Alert.AlertType.INFORMATION, "The restaurant is not empty");
                        exitAlert.show();
                        event.consume();
                    }
                }

            });
            stage.setAlwaysOnTop(false);
            stage.setScene(new Scene(vBoxKitchen));
            stage.show();
            stage.toBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

