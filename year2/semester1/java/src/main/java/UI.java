import domain.Assignment;
import domain.Grade;
import domain.GradeDTO;
import domain.Student;
import services.AssignmentService;
import services.GradeService;
import services.StudentService;
import validators.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UI {
    private AssignmentService assignmentService;
    private StudentService studentService;
    private GradeService gradeService;

    public UI(StudentService studentService, AssignmentService assignmentService, GradeService gradeService) {
        this.assignmentService = assignmentService;
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

    private void printMenu() {
        System.out.println("0 - Exit");
        System.out.println("1 - Students");
        System.out.println("2 - Assignments");
        System.out.println("3 - Grades");
        System.out.println("4 - Filter");
    }

    public void run() {
        printMenu();
        Scanner reader = new Scanner(System.in);
        String cmd = reader.nextLine();
        while (!cmd.equals("0")) {
            switch (cmd) {
                case "1": {
                    printMenuStudents();
                    break;
                }
                case "2": {
                    printMenuAssignments();
                    break;
                }
                case "3": {
                    printMenuGrades();
                    break;
                }
                case "4": {
                    printMenuFilter();
                    break;
                }
            }
            printMenu();
            cmd = reader.nextLine();
        }
    }

    private void optionsFilter() {
        System.out.println("0 - Back");
        System.out.println("1 - Students of the same group");
        System.out.println("2 - Students that turned in a given assignment");
        System.out.println("3 - Students that turned in a given assignment to a given professor");
        System.out.println("4 - Grades for an assignment");
    }

    private void printMenuFilter() {
        Scanner reader = new Scanner(System.in);
        optionsFilter();
        String cmd = reader.nextLine();
        while (!cmd.equals("0")) {
            try {
                switch (cmd) {
                    case "1": {
                        filterGroup();
                        break;
                    }
                    case "2": {
                        filterAssignment();
                        break;
                    }
                    case "3": {
                        filterAssignmentProfessor();
                        break;
                    }
                    case "4":{
                        filterGrades();
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
            } catch (ValidationException | IllegalArgumentException ve) {
                System.out.println(ve.getMessage());
            } finally {
                optionsFilter();
                cmd = reader.nextLine();
            }
        }
    }

    private void filterGrades() {
        Scanner reader = new Scanner(System.in);
        assignmentService.getAll().forEach(System.out::println);
        System.out.println("Assignment ID: ");
        String a = reader.nextLine();
        System.out.println("Week nr: ");
        int w = Integer.parseInt(reader.nextLine());
        List<GradeDTO> result = gradeService.getGradesAssignment(a, w);
        if (result.size() == 0) {
            System.out.println("No grades found");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void filterAssignmentProfessor() {
        Scanner reader = new Scanner(System.in);
        assignmentService.getAll().forEach(System.out::println);
        System.out.println("Assignment ID: ");
        String a = reader.nextLine();
        System.out.println("Professor: ");
        String p = reader.nextLine();
        List<Student> result = gradeService.getStudentsAssignmentProfessor(a, p);
        if (result.size() == 0) {
            System.out.println("No students found");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void filterAssignment() {
        Scanner reader = new Scanner(System.in);
        assignmentService.getAll().forEach(System.out::println);
        System.out.println("Assignment ID: ");
        String a = reader.nextLine();
        List<Student> result = gradeService.getStudentsAssignment(a);
        if (result.size() == 0) {
            System.out.println("No students found");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void filterGroup() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Group: ");
        int g = Integer.parseInt(reader.nextLine());
        List<Student> result = studentService.getSameGroup(g);
        if (result.size() == 0) {
            System.out.println("No students found");
        } else {
            result.forEach(System.out::println);
        }
    }


    private void optionsGrades() {
        System.out.println("0 - Back");
        System.out.println("1 - Add grade");
        System.out.println("2 - Show grades");
    }

    private void printMenuGrades() {
        Scanner reader = new Scanner(System.in);
        optionsGrades();
        String cmd = reader.nextLine();
        while (!cmd.equals("0")) {
            try {
                switch (cmd) {
                    case "1": {
                        addGrade();
                        break;
                    }
                    case "2": {
                        showGrades();
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
            } catch (ValidationException | IllegalArgumentException ve) {
                System.out.println(ve.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                optionsGrades();
                cmd = reader.nextLine();
            }
        }
    }

    private void showGrades() {
        gradeService.getAll().forEach(System.out::println);
    }

    private String lateProfessor(Scanner reader) {
        System.out.println("Is this the date the student turned in the assignment?");
        System.out.println("1 - Yes");
        System.out.println("2 - No");
        String answer = reader.nextLine();
        if (!answer.equals("1") && !answer.equals("2")) {
            throw new ValidationException("Invalid answer");
        }
        String s = "";
        if (answer.equals("2")) {
            System.out.println("Correct date: ");
            s = reader.nextLine();
        }
        return s;
    }

    private void addGrade() throws IOException {
        System.out.println("Choose student: ");
        studentService.getAll().forEach(System.out::println);
        System.out.println("Student ID: ");
        Scanner reader = new Scanner(System.in);
        String idStudent = reader.nextLine();
        System.out.println("Choose assignment: ");
        assignmentService.getAll().forEach(System.out::println);
        System.out.println("Assignment ID: ");
        String idAssignment = reader.nextLine();
        System.out.println("Professor: ");
        String p = reader.nextLine();
        System.out.println("Excused(nr weeks): ");
        int m = Integer.parseInt(reader.nextLine());
        String s = lateProfessor(reader);
        LocalDate d = LocalDate.now();
        int flag = 0;
        if (!s.equals("")){
            flag = 1;
            String[] date = s.split("\\.");
            if (date.length != 3){
                throw new ValidationException("Invalid date");
            }
            d = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        }

        float max = gradeService.calculateMaxGrade(idAssignment, m, d, flag);
        System.out.println("Max grade: " + max);
        System.out.println("Grade: ");
        float grade = Float.parseFloat(reader.nextLine());
        if (grade > max) {
            throw new ValidationException("Invalid grade value");
        }
        Grade g = gradeService.add(idStudent, idAssignment, grade, p, d);
        if (g != null) {
            System.out.println("The student already has a grade for the assignment");
        } else {
            System.out.println("Grade added");
            feedback(idStudent, idAssignment, reader);
        }
    }

    private void feedback(String idStudent, String idAssignment, Scanner reader) throws IOException {
        System.out.println("Add feedback?");
        System.out.println("1 - Yes");
        System.out.println("2 - No");
        String answer = reader.nextLine();
        String f = "";
        if (answer.equals("1")) {
            System.out.println("Feedback: ");
            f = reader.nextLine();
        }
        gradeService.addFeedback(idStudent, idAssignment, f);
    }

    private void printMenuAssignments() {
        Scanner reader = new Scanner(System.in);
        try {
            optionsAssignments();
            String cmd = reader.nextLine();
            while (!cmd.equals("0")) {
                try {
                    switch (cmd) {
                        case "1": {
                            addAssignment();
                            break;
                        }
                        case "2": {
                            deleteAssignment();
                            break;
                        }
                        case "3": {
                            updateAssignment();
                            break;
                        }
                        case "4": {
                            printAssignments();
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                } catch (ValidationException | IllegalArgumentException ve) {
                    System.out.println(ve.getMessage());
                } finally {
                    optionsAssignments();
                    cmd = reader.nextLine();
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


    private void optionsAssignments() {
        System.out.println("0 - Back");
        System.out.println("1 - Add assignment");
        System.out.println("2 - Delete assignment");
        System.out.println("3 - Update assignment");
        System.out.println("4 - Show assignments");
    }

    private void addAssignment() throws IOException {
        System.out.println("Assignment(ID description deadline): ");
        Scanner reader = new Scanner(System.in);
        String a = reader.nextLine();
        String[] assignment = a.split(" ");
        if (assignment.length < 3) {
            System.out.println("Invalid number of parameters");
        }
        Assignment assignment1 = this.assignmentService.add(assignment[0], assignment[1], assignment[2]);
        if (assignment1 != null) {
            System.out.println("Assignment already exists");
        } else {
            System.out.println("Assignment added");
        }
    }

    private void updateAssignment() throws IOException {
        assignmentService.getAll().forEach(System.out::println);
        System.out.println("New assignment(ID description deadlineWeek): ");
        Scanner reader = new Scanner(System.in);
        String[] a = reader.nextLine().split(" ");
        if (a.length < 3) {
            System.out.println("Invalid number of parameters");
        }
        Assignment assignment = assignmentService.update(a[0], a[1], a[2]);
        System.out.println("Assignment updated");
    }

    private void deleteAssignment() {
        assignmentService.getAll().forEach(System.out::println);
        Scanner reader = new Scanner(System.in);
        System.out.println("Assignment ID: ");
        String id = reader.nextLine();
        Assignment a = assignmentService.delete(id);
        if (a == null) {
            System.out.println("The assignment ID does not exist");
        } else {
            System.out.println("Assignment deleted");
        }
    }

    private void printAssignments() {
        assignmentService.getAll().forEach(System.out::println);
    }


    private void optionsStudents() {
        System.out.println("0 - Back");
        System.out.println("1 - Add student");
        System.out.println("2 - Delete student");
        System.out.println("3 - Update student");
        System.out.println("4 - Show students");
    }

    private void printMenuStudents() {
        optionsStudents();
        Scanner reader = new Scanner(System.in);
        String cmd = reader.nextLine();
        try {
            while (!cmd.equals("0")) {
                try {
                    switch (cmd) {
                        case "1": {
                            addStudent();
                            break;
                        }
                        case "2": {
                            deleteStudent();
                            break;
                        }
                        case "3": {
                            updateStudent();
                            break;
                        }
                        case "4": {
                            printStudents();
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                } catch (ValidationException | IllegalArgumentException ve) {
                    System.out.println(ve.getMessage());
                } finally {
                    optionsStudents();
                    cmd = reader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Student(ID lastName firstName group email professor): ");
        String st = reader.nextLine();
        String[] student = st.split(" ");
        if (student.length < 6) {
            throw new ValidationException("Invalid number of parameters");
        }
        Student s = this.studentService.add(student[0], student[1], student[2], Integer.parseInt(student[3]), student[4], student[5]);
        if (s != null) {
            System.out.println("Student id already exists");
        }
        else{
            System.out.println("Student added");
        }
    }

    private void updateStudent() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Student(ID lastName firstName group email professor): ");
        String st = reader.nextLine();
        String[] student = st.split(" ");
        if (student.length < 6) {
            throw new ValidationException("Invalid number of parameters");
        }
        Student s = this.studentService.update(student[0], student[1], student[2], Integer.parseInt(student[3]), student[4], student[5]);
        if (s != null) {
            System.out.println("Student doesn't exist");
        }
        else{
            System.out.println("Student updated");
        }
    }

    private void deleteStudent() throws IOException {
        studentService.getAll().forEach(System.out::println);
        System.out.println("ID: ");
        Scanner reader = new Scanner(System.in);
        String id = reader.nextLine();
        Student s = studentService.delete(id);
        if (s == null) {
            System.out.println("Student doesn't exist");
        }
        else{
            System.out.println("Student deleted");
        }
    }

    private void printStudents() {
        studentService.getAll().forEach(System.out::println);
    }
}
