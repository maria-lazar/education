package services;

import domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.StudentFileRepository;
import repository.XMLStudentFileRepository;
import validators.StudentValidator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.StreamSupport;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StudentServiceTest {
    private XMLStudentFileRepository repository;
    private StudentService service;

    @Before
    public void before() {
        repository = new XMLStudentFileRepository(new StudentValidator(), "testdata/studenttest.xml");
        service = new StudentService(repository);
    }

    @Test
    public void add() {
        Student s = service.add("1", "a", "a", "224", "a@a", "z");
        assertEquals(null, s);
        Student s2 = service.add("1", "b", "b", "224", "a@a", "z");
        int count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
    }

    @Test
    public void update() {
        Student s = service.add("1", "a", "a", "224", "a@a", "z");
        Student s2 = service.update("1", "b", "b", "224", "a@a", "z");
        assertEquals(null, s2);
        Student s3 = service.update("2", "b", "b", "224", "a@a", "z");
        assertNotEquals(null, s3);
    }

    @Test
    public void delete() {
        assertEquals(null, service.delete("1"));
        Student s = service.add("1", "a", "a", "224", "a@a", "z");
        int count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(1, count);
        Student s2 = service.delete("1");
        count = (int) StreamSupport.stream(service.getAll().spliterator(), false)
                .count();
        assertEquals(0, count);
    }

    @After
    public void after() {
        try {
            PrintWriter writer = new PrintWriter("testdata/studenttest.xml");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
