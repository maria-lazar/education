import controller.LoginController;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import service.TripServicesProxyGrpc;

import java.util.Properties;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            LOGGER.info("creating application context");
            Resource resource = new ClassPathResource("tripclient.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            ManagedChannel channel = ManagedChannelBuilder.forTarget(props.getProperty("trip.channel"))
                    .usePlaintext()
                    .build();
            TripServicesProxyGrpc tripServices = new TripServicesProxyGrpc(channel);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/loginView.fxml"));
            AnchorPane root = loader.load();
            LoginController loginController = loader.getController();
            loginController.setService(tripServices, primaryStage);
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
            LOGGER.info("application context created");
        } catch (Exception e) {
            LOGGER.warn("failed to create application context " + e);
            e.printStackTrace();
        }
    }
}


