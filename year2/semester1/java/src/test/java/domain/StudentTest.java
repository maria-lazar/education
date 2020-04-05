package domain;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class StudentTest {
    @Test
    public void create(){
        Student s = new Student("1", "a", "b", 224, "a@a", "z");
        assertEquals("1", s.getId());
        assertEquals("a", s.getLastName());
        assertEquals("b", s.getFirstName());
        assertEquals(224, s.getGroup());
        assertEquals("a@a", s.getEmail());
        assertEquals("z", s.getGuidingProfessor());
    }
}
