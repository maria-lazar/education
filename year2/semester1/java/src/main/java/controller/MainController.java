package controller;

import domain.AcademicYearStructure;
import domain.Assignment;
import domain.GradeDTO;
import domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.AssignmentService;
import services.GradeService;
import services.StudentService;
import util.Observer;
import validators.ValidationException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MainController implements Observer<util.Event> {

    private StudentService studentService;
    private GradeService gradeService;
    private AssignmentService assignmentService;

    ObservableList<Student> modelStudents = FXCollections.observableArrayList();
    ObservableList<GradeDTO> modelGrades = FXCollections.observableArrayList();
    ObservableList<Assignment> modelAssignments = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, Integer> groupColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> professorColumn;
    @FXML
    private TableView<Student> studentsTableView;

    @FXML
    private TableColumn<GradeDTO, String> nameColumn;
    @FXML
    private TableColumn<GradeDTO, Integer> assignmentColumn;
    @FXML
    private TableColumn<GradeDTO, Double> gradeColumn;
    @FXML
    private TableColumn<GradeDTO, Integer> weekColumn;
    @FXML
    private TableColumn<GradeDTO, Integer> deadlineColumn;
    @FXML
    private TableView<GradeDTO> gradesTableView;

    @FXML
    private TextField studentNameTextField;
    @FXML
    private TextField gradeTextField;
    @FXML
    private TextField professorTextField;
    @FXML
    private TextField excusedTextField;
    @FXML
    private TextField dateTextField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button reportButton;
    @FXML
    private ComboBox<Assignment> comboBoxAssignments;
    @FXML
    private TextArea textAreaFeedback;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("guidingProfessor"));
        studentsTableView.setItems(modelStudents);

        nameColumn.setCellValueFactory(new PropertyValueFactory<GradeDTO, String>("name"));
        assignmentColumn.setCellValueFactory(new PropertyValueFactory<GradeDTO, Integer>("assignmentNumber"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<GradeDTO, Double>("grade"));
        weekColumn.setCellValueFactory(new PropertyValueFactory<GradeDTO, Integer>("week"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<GradeDTO, Integer>("deadline"));
        gradesTableView.setItems(modelGrades);
        studentNameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleStudentName();
            }
        });
        comboBoxAssignments.setItems(modelAssignments);
        comboBoxAssignments.valueProperty().addListener(new ChangeListener<Assignment>() {
            @Override
            public void changed(ObservableValue<? extends Assignment> observable, Assignment oldValue, Assignment newValue) {
                handleChangeAssignment();
            }
        });
        excusedTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleChangeAssignment();
            }
        });
        dateTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleChangeAssignment();
            }
        });
    }

    public void setService(StudentService s, AssignmentService a, GradeService serviceGrade) {
        this.studentService = s;
        this.assignmentService = a;
        this.gradeService = serviceGrade;
        modelStudents.setAll(getStudents());
        modelGrades.setAll(getGrades());
        initializeAssignments();
        studentService.addObserver(this);
        gradeService.addObserver(this);
    }

    private void handleChangeAssignment() {
        try {
            textAreaFeedback.clear();
            int week = AcademicYearStructure.getInstance().getCurrentWeek();
            int p = 0;
            try {
                if (!dateTextField.getText().isEmpty()) {
                    p = Integer.parseInt(dateTextField.getText());
                }
            } catch (NumberFormatException e) {

            } catch (DateTimeException de) {

            } catch (ValidationException ve) {

            }
            int deadline = comboBoxAssignments.getSelectionModel().getSelectedItem().getDeadlineWeek();
            int pen = week - deadline - p;
            //int pen = week - deadline;
            if (!excusedTextField.getText().isEmpty()) {
                pen = pen - Integer.parseInt(excusedTextField.getText());
            }
            if (pen > 0 && pen < 3) {
                textAreaFeedback.textProperty().setValue("NOTA A FOST DIMINUATA CU " + String.valueOf(pen) + " PUNCTE DATORITA INTARZIERILOR");
            } else {
                textAreaFeedback.clear();
            }
        } catch (ValidationException ve) {
            textAreaFeedback.clear();
        }
    }

    private void handleStudentName() {
        List<Student> students = getStudents().stream()
                .filter(s -> {
                    String name = s.getFirstName() + " " + s.getLastName();
                    return name.startsWith(studentNameTextField.getText());
                })
                .collect(Collectors.toList());
        modelStudents.setAll(students);
    }

    private List<Student> getStudents() {
        return StreamSupport.stream(studentService.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @FXML
    public void handleAdd() {
        showStudentsEditScene(null);
    }

    @FXML
    public void handleUpdate() {
        Student s = studentsTableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            AlertMessage.showMessage("Please select student to update", Alert.AlertType.ERROR);
            return;
        }
        showStudentsEditScene(s);
    }

    @FXML
    private void handleDelete() {
        Student s = studentsTableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            AlertMessage.showMessage("Please select student to delete", Alert.AlertType.ERROR);
            return;
        }
        Student st = studentService.delete(s.getId());
        AlertMessage.showMessage("Student deleted", Alert.AlertType.INFORMATION);
    }

    private void initializeAssignments() {
        modelAssignments.setAll(getAssignments());
        defaultValueCombo();
    }

    private void defaultValueCombo() {
        int currentWeek = AcademicYearStructure.getInstance().getCurrentWeek();
        Assignment a = assignmentService.getCurrentAssignment(currentWeek);
        comboBoxAssignments.getSelectionModel().select(a);
    }

    private List<Assignment> getAssignments() {
        return StreamSupport.stream(assignmentService.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private List<GradeDTO> getGrades() {
        return gradeService.getGrades().stream()
                .sorted((g1, g2) -> {
                    return g1.getName().compareTo(g2.getName());
                })
                .collect(Collectors.toList());
    }

    public void showStudentsEditScene(Student student) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/modifyStudent.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.setTitle("Edit");
            EditStudentController editController = loader.getController();
            editController.setService(studentService, editStage, student);
            Scene editScene = new Scene(root);
            editStage.setScene(editScene);
            editStage.show();
        } catch (IOException e) {

        }
    }

    public void handleAddGrade() {
        Student s = studentsTableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            AlertMessage.showMessage("Please select student", Alert.AlertType.ERROR);
            return;
        }
        Assignment a = comboBoxAssignments.getSelectionModel().getSelectedItem();
        if (a == null) {
            AlertMessage.showMessage("Please select assignment", Alert.AlertType.ERROR);
            return;
        }
        try {
            float grade = Float.parseFloat(gradeTextField.getText());
            String professor = professorTextField.getText();
            if (professor.equals("")) {
                AlertMessage.showMessage("Invalid professor", Alert.AlertType.ERROR);
                return;
            }
            int excused = 0;
            if (!excusedTextField.getText().isEmpty()) {
                excused = Integer.parseInt(excusedTextField.getText());
            }
            LocalDate d = LocalDate.now();
            int flag = 0;
            String dateS = dateTextField.getText();

            if (!dateS.isEmpty()) {
                flag = 1;
                int weeksLate = Integer.parseInt(dateS);
                if (weeksLate > AcademicYearStructure.getInstance().getCurrentWeek()) {
                    throw new ValidationException("Invalid number of weeks");
                }
                d = d.minusWeeks(weeksLate);
            }
            int max = gradeService.calculateMaxGrade(a.getId(), excused, d, flag);
            int pen = 10 - max;
            grade = grade - pen;
            if (grade > max) {
                AlertMessage.showMessage("Grade value exceeds the maximum value", Alert.AlertType.ERROR);
                return;
            }
            confirmGrade(s, a, grade, professor, excused, pen, d);
        } catch (NumberFormatException e) {
            AlertMessage.showMessage("Invalid integer", Alert.AlertType.ERROR);
            return;
        } catch (ValidationException ve) {
            AlertMessage.showMessage(ve.getMessage(), Alert.AlertType.ERROR);
            textAreaFeedback.clear();
            return;
        }
    }

    private void confirmGrade(Student s, Assignment a, float grade, String professor, int excused, int pen, LocalDate d) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/confirmGrade.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage confirmStage = new Stage();
            confirmStage.initModality(Modality.APPLICATION_MODAL);
            confirmStage.setTitle("Confirm Grade");
            ConfirmGradeController confirmGradeController = loader.getController();
            confirmGradeController.setService(gradeService, confirmStage, s, a, grade, professor, textAreaFeedback.getText(), excused, pen, d);
            Scene confirmScene = new Scene(root);
            confirmStage.setScene(confirmScene);
            confirmStage.show();
        } catch (IOException e) {

        } catch (NumberFormatException e) {
            AlertMessage.showMessage("Invalid number of weeks", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void update(util.Event event) {
        modelStudents.setAll(getStudents());
        modelGrades.setAll(getGrades());
        defaultValueCombo();

        studentNameTextField.clear();
        professorTextField.clear();
        textAreaFeedback.clear();
        gradeTextField.clear();
        excusedTextField.clear();
        dateTextField.clear();
    }

    @FXML
    private void handleReport() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/reports.fxml"));
        try {
            AnchorPane root = loader.load();
            Stage reportsStage = new Stage();
            reportsStage.initModality(Modality.APPLICATION_MODAL);
            reportsStage.setTitle("Reports");
            ReportsController reportsController = loader.getController();
            reportsController.setService(gradeService, reportsStage);
            Scene reportsScene = new Scene(root);
            reportsStage.setScene(reportsScene);
            reportsStage.show();
        } catch (IOException e) {

        }
    }
}
