import controller.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.TripServices;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            LOGGER.info("creating application context");
            ApplicationContext context = new ClassPathXmlApplicationContext("tripXMLClient.xml");
            TripServices tripServices = context.getBean(TripServices.class);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/loginView.fxml"));
            AnchorPane root = loader.load();
            LoginController loginController = loader.getController();
            loginController.setService(tripServices, primaryStage);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    tripServices.logOut();
                }
            });
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


