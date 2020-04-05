import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.XMLAssignmentFileRepository;
import repository.XMLGradeFileRepository;
import repository.XMLStudentFileRepository;
import services.AssignmentService;
import services.GradeService;
import services.StudentService;
import util.ApplicationContext;
import validators.AssignmentValidator;
import validators.GradeValidator;
import validators.StudentValidator;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String fileN = ApplicationContext.getPROPERTIES().getProperty("data.students");
        XMLStudentFileRepository xmlRepoStudent = new XMLStudentFileRepository(new StudentValidator(), fileN);
        StudentService serviceStudent = new StudentService(xmlRepoStudent);
        String fileN2 = ApplicationContext.getPROPERTIES().getProperty("data.assignments");
        XMLAssignmentFileRepository repoAssignment = new XMLAssignmentFileRepository(new AssignmentValidator(), fileN2);
        AssignmentService serviceAssignment = new AssignmentService(repoAssignment);
        String fileN3 = ApplicationContext.getPROPERTIES().getProperty("data.grades");
        XMLGradeFileRepository repoGrade = new XMLGradeFileRepository(new GradeValidator(), fileN3);
        GradeService serviceGrade = new GradeService(repoGrade, xmlRepoStudent, repoAssignment);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainView.fxml"));
        AnchorPane root = loader.load();

        MainController studentController = loader.getController();
        studentController.setService(serviceStudent, serviceAssignment, serviceGrade);
        Scene scene = new Scene(root, 1400,800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
