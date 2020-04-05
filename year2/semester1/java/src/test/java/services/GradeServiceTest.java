package services;

import domain.AcademicYearStructure;
import domain.Assignment;
import domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.*;
import validators.AssignmentValidator;
import validators.GradeValidator;
import validators.StudentValidator;
import validators.ValidationException;

import java.io.*;
import java.time.LocalDate;
import java.util.stream.StreamSupport;

import static junit.framework.TestCase.assertEquals;

public class GradeServiceTest {
    private XMLAssignmentFileRepository assignmentRepository;
    private XMLStudentFileRepository studentRepository;
    private XMLGradeFileRepository gradeRepository;
    private GradeService gradeService;

    @Before
    public void writeData() {
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now().plusMonths(5);
        LocalDate start2 = end.plusMonths(1);
        LocalDate end2 = end.plusMonths(5);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testdata/academicyeartest.txt")));
            writer.write(start.getDayOfMonth() + " " + start.getMonthValue() + " " + start.getYear());
            writer.newLine();
            writer.write(end.getDayOfMonth() + " " + end.getMonthValue() + " " + end.getYear());
            writer.newLine();
            writer.write(String.valueOf(0));
            writer.newLine();
            writer.write(start2.getDayOfMonth() + " " + start2.getMonthValue() + " " + start2.getYear());
            writer.newLine();
            writer.write(end2.getDayOfMonth() + " " + end2.getMonthValue() + " " + end2.getYear());
            writer.newLine();
            writer.write(String.valueOf(0));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void before() {
        try {
            AcademicYearStructure structure = AcademicYearStructure.readFromFile("testdata/academicyeartest.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assignmentRepository = new XMLAssignmentFileRepository(new AssignmentValidator(), "testdata/assignmenttest.xml");
        studentRepository = new XMLStudentFileRepository(new StudentValidator(), "testdata/studenttest.xml");
        gradeRepository = new XMLGradeFileRepository(new GradeValidator(), "testdata/gradetest.xml");
        gradeService = new GradeService(gradeRepository, studentRepository, assignmentRepository);
    }

    @Test
    public void calculateMaxGrade() {
        Assignment a = new Assignment("1", "a", 5, 6);
        assignmentRepository.save(a);
        assertEquals(10, gradeService.calculateMaxGrade(a.getId(), 0, ""));
        Assignment a2 = new Assignment("2", "b", 3, 4);
        assignmentRepository.save(a2);
        int g = gradeService.calculateMaxGrade(a2.getId(), 0, "");
        assertEquals(9, g);
        g = gradeService.calculateMaxGrade(a2.getId(), 1, "");
        assertEquals(10, g);
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        g = gradeService.calculateMaxGrade(a2.getId(), 0, String.valueOf(oneWeekAgo.getDayOfMonth() + "." + oneWeekAgo.getMonthValue() + "." + oneWeekAgo.getYear()));
        assertEquals(10, g);
        try {
            Assignment a3 = new Assignment("3", "b", 1, 2);
            assignmentRepository.save(a3);
            gradeService.calculateMaxGrade(a3.getId(), 0, "");
        } catch (ValidationException ve) {

        }
        try {
            Assignment a3 = new Assignment("3", "b", 1, 2);
            assignmentRepository.save(a3);
            LocalDate oneWeek = LocalDate.now().plusWeeks(1);
            gradeService.calculateMaxGrade(a3.getId(), 0, String.valueOf(oneWeek.getDayOfMonth() + "." + oneWeek.getMonthValue() + "." + oneWeek.getYear()));
        } catch (ValidationException ve) {

        }
        try {
            gradeService.calculateMaxGrade("4", 0, "");
        } catch (ValidationException ve) {

        }
    }

    @Test
    public void add(){
        Assignment a = new Assignment("1", "a", 5, 6);
        assignmentRepository.save(a);
        Student s = new Student("1", "a", "a", 224, "a", "a");
        studentRepository.save(s);
        gradeService.add("1", "1", 10, "a", "");
        int count = (int) StreamSupport.stream(gradeService.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
        try{
            gradeService.add("2", "1", 10, "a", "");
        }catch (ValidationException ve){

        }
    }

    @After
    public void after() {
        try {
            PrintWriter writer = new PrintWriter("testdata/studenttest.xml");
            PrintWriter writer1 = new PrintWriter("testdata/assignmenttest.xml");
            PrintWriter writer2 = new PrintWriter("testdata/gradetest.xml");
            writer.close();
            writer1.close();
            writer2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
