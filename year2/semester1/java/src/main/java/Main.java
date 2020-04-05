import domain.AcademicYearStructure;
import repository.*;
import services.AssignmentService;
import services.GradeService;
import services.StudentService;
import validators.AssignmentValidator;
import validators.GradeValidator;
import validators.StudentValidator;

import java.io.IOException;


public class Main {


    public static void main(String[] args) {
        try {
            AcademicYearStructure yearStructure = AcademicYearStructure.readFromFile("data/academicyear.txt");
//            XMLStudentFileRepository xmlRepoStudent = new XMLStudentFileRepository(new StudentValidator(), "data/students.xml");
//            StudentService serviceStudent = new StudentService(xmlRepoStudent);
//            XMLAssignmentFileRepository repoAssignment = new XMLAssignmentFileRepository(new AssignmentValidator(), "data/assignments.xml");
//            AssignmentService serviceAssignment = new AssignmentService(repoAssignment);
//            XMLGradeFileRepository repoGrade = new XMLGradeFileRepository(new GradeValidator(), "data/grades.xml");
//            //GradeService gradeService = new GradeService(repoGrade, xmlRepoStudent, repoAssignment);
//            UI ui = new UI(serviceStudent, serviceAssignment, gradeService);
//            ui.run();
            MainApp.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
