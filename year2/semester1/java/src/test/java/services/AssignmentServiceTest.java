package services;

import domain.AcademicYearStructure;
import domain.Assignment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.AssignmentFileRepository;
import repository.XMLAssignmentFileRepository;
import validators.AssignmentValidator;
import validators.ValidationException;

import java.io.*;
import java.time.LocalDate;
import java.util.stream.StreamSupport;

import static junit.framework.TestCase.assertEquals;

public class AssignmentServiceTest {
    private XMLAssignmentFileRepository repository;
    private AssignmentService service;

    @Before
    public void writeData(){
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
        repository = new XMLAssignmentFileRepository(new AssignmentValidator(), "testdata/assignmenttest.xml");
        service = new AssignmentService(repository);
    }

    @Test
    public void add(){
        Assignment a = service.add("1", "a", "6");
        assertEquals(null, a);
        int count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
        Assignment a2 = service.add("1", "b", "7");
        count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
        try{
            service.add("2", "a", "4");
        }catch (ValidationException ve){

        }
    }

    @Test
    public void update(){
        Assignment a = service.add("1", "a", "6");
        Assignment a2 = service.update("1", "b", "7");
        assertEquals(null, a2);
        assertEquals(5, service.get("1").getStartWeek());
        try{
            service.update("2", "a", "6");
        }catch (ValidationException ve){

        }
        try{
            service.update("2", "a", "5");
        }catch (ValidationException ve){

        }
    }

    @Test
    public void delete(){
        assertEquals(null, service.delete("1"));
        Assignment a = service.add("1", "a", "6");
        int count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
        Assignment a2 = service.delete("1");
        assertEquals("a", a2.getDescription());
        count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(0, count);
    }

    @After
    public void after(){
        try {
            PrintWriter writer = new PrintWriter("testdata/assignmenttest.xml");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
