package domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;

import static junit.framework.TestCase.assertEquals;

public class AssignmentTest {
    @Test
    public void create() {
        Assignment a = new Assignment("2019-1", "a", 5, 5);
        assertEquals("2019-1", a.getId());
        assertEquals(5, a.getStartWeek());
        assertEquals(5, a.getDeadlineWeek());
        assertEquals("a", a.getDescription());
        a.setDeadlineWeek(7);
        assertEquals(7, a.getDeadlineWeek());
        a.setDescription("b");
        assertEquals("b", a.getDescription());
    }
}
