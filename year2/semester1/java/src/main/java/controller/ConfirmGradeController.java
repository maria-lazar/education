package controller;

import domain.Assignment;
import domain.Grade;
import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GradeService;

import java.io.IOException;
import java.time.LocalDate;


public class ConfirmGradeController {
    private GradeService service;
    private Stage stage;
    private Student student;
    private Assignment assignment;
    private float grade;
    private String professor;
    private String feedback;
    private int excusedWeeks;
    private int penalty;
    private LocalDate date;

    @FXML
    private TextField studentTextField;
    @FXML
    private TextField assignmentTextField;
    @FXML
    private TextField gradeTextField;
    @FXML
    private TextField excusedTextField;
    @FXML
    private TextField penaltyTextField;

    public void setService(GradeService service, Stage stage, Student s, Assignment a, float grade, String professor, String text, int excused, int i, LocalDate d) {
        this.service = service;
        this.stage = stage;
        this.student = s;
        this.assignment = a;
        this.grade = grade;
        this.professor = professor;
        this.feedback = text;
        this.excusedWeeks = excused;
        this.penalty = i;
        this.date = d;
        fillInformation();
    }

    private void fillInformation() {
        studentTextField.setEditable(false);
        assignmentTextField.setEditable(false);
        gradeTextField.setEditable(false);
        excusedTextField.setEditable(false);
        penaltyTextField.setEditable(false);
        studentTextField.setText(student.getFirstName() + " " + student.getLastName());
        assignmentTextField.setText(assignment.getId());
        gradeTextField.setText(String.valueOf(grade));
        excusedTextField.setText(String.valueOf(excusedWeeks));
        penaltyTextField.setText(String.valueOf(penalty));
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }

    @FXML
    private void handleConfirm() throws IOException {
        Grade g = service.add(student.getId(), assignment.getId(), grade, professor, date);
        if (g != null) {
            AlertMessage.showMessage("Grade already exists", Alert.AlertType.ERROR);
            stage.close();
            return;
        }
        service.addFeedback(student.getId(), assignment.getId(), feedback);
        stage.close();
        AlertMessage.showMessage("Grade added", Alert.AlertType.INFORMATION);
    }
}
