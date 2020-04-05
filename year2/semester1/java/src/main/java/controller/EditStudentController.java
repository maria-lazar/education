package controller;

import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.StudentService;
import validators.ValidationException;


public class EditStudentController {
    private StudentService service;
    private Stage stage;
    private Student student;

    @FXML
    TextField idTextField;
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField groupTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField professorTextField;

    public void setService(StudentService service, Stage stage, Student student) {
        this.service = service;
        this.stage = stage;
        this.student = student;
        if (student != null) {
            idTextField.setEditable(false);
            fillFields();
        }
    }

    private void fillFields() {
        idTextField.setText(student.getId());
        firstNameTextField.setText(student.getFirstName());
        lastNameTextField.setText(student.getLastName());
        groupTextField.setText(String.valueOf(student.getGroup()));
        emailTextField.setText(student.getEmail());
        professorTextField.setText(student.getGuidingProfessor());
    }

    private void add(String id, String lastName, String firstName, Integer group, String email, String professor) {
        try {
            Student s = service.add(id, lastName, firstName, group, email, professor);
            if (s != null) {
                AlertMessage.showMessage("Student id already exists", Alert.AlertType.ERROR);
                return;
            } else {
                AlertMessage.showMessage("Student added", Alert.AlertType.INFORMATION);
                stage.close();
            }
        } catch (ValidationException ve) {
            AlertMessage.showMessage(ve.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ie) {
            AlertMessage.showMessage(ie.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleSave() {
        try {
            String id = idTextField.getText();
            String lastName = lastNameTextField.getText();
            String firstName = firstNameTextField.getText();
            String group = groupTextField.getText();
            String email = emailTextField.getText();
            String professor = professorTextField.getText();
            if (id.equals("") || lastName.equals("") || firstName.equals("") || email.equals("") || professor.equals("") || group.equals("")) {
                AlertMessage.showMessage("All fields must be filled", Alert.AlertType.ERROR);
                return;
            }
            if (student == null) {
                add(id, lastName, firstName, Integer.parseInt(group), email, professor);
            } else {
                update(id, lastName, firstName, Integer.parseInt(group), email, professor);
            }
        }catch (NumberFormatException e){
            AlertMessage.showMessage("Invalid group", Alert.AlertType.ERROR);
        }
    }

    private void update(String id, String lastName, String firstName, Integer group, String email, String professor) {
        try {
            Student s = service.update(id, lastName, firstName, group, email, professor);
            if (s != null) {
                AlertMessage.showMessage("Student doesn't exist", Alert.AlertType.ERROR);
                return;
            }
            AlertMessage.showMessage("Student updated", Alert.AlertType.INFORMATION);
            stage.close();
        } catch (ValidationException ve) {
            AlertMessage.showMessage(ve.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            AlertMessage.showMessage(e.getMessage(), Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ie) {
            AlertMessage.showMessage(ie.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }
}
