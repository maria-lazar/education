package controller;

import domain.Student;
import domain.StudentGrade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.GradeService;

import javax.swing.text.html.ListView;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {
    GradeService gradeService;
    Stage stage;
    @FXML
    private TableColumn<StudentGrade, String> nameColumn;
    @FXML
    private TableColumn<StudentGrade, Integer> groupColumn;
    @FXML
    private TableColumn<StudentGrade, Double> gradeColumn;
    @FXML
    private TableView<StudentGrade> studentsTableView;
    private ObservableList<StudentGrade> modelStudents = FXCollections.observableArrayList();

    @FXML
    private Button gradeButton;
    @FXML
    private TextField assignmentTextField;

    public void setService(GradeService gradeService, Stage reportsStage) {
        this.gradeService = gradeService;
        this.stage = stage;
    }

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<StudentGrade, String>("studentName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<StudentGrade, Integer>("group"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<StudentGrade, Double>("grade"));
        studentsTableView.setItems(modelStudents);
        assignmentTextField.setEditable(false);
    }
    @FXML
    private void handleGrades(){
        modelStudents.setAll(gradeService.studentsGrade());
        assignmentTextField.clear();
    }
    @FXML
    private void handleDifficultAssignment(){
        assignmentTextField.setText("assignment: " + gradeService.findMostDifficult());
    }
    @FXML
    private void handleExam(){
        List<StudentGrade> students = gradeService.studentsGrade().stream()
                .filter(s -> s.getGrade() >= 4)
                .collect(Collectors.toList());
        modelStudents.setAll(students);
        assignmentTextField.clear();
    }
    @FXML
    private void handleOnTime(){
        modelStudents.setAll(gradeService.onTimeStudents());
    }
}
