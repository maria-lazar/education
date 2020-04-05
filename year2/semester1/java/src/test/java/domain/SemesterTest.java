package domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;

import static junit.framework.TestCase.assertEquals;

public class SemesterTest {
    @Test
    public void create(){
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now().plusMonths(5);
        Vacation v = new Vacation(LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(3).plusWeeks(1));
        Semester sem = new Semester("2019-2020-1", start, end);
        sem.addVacation(v);
        assertEquals("2019-2020-1", sem.getId());
        assertEquals("2019-2020-1", sem.toString());
        assertEquals(start, sem.getStartDate());
        assertEquals(end, sem.getEndDate());
        assertEquals(5, sem.getCurrentWeek());
        Vacation v2 = new Vacation(LocalDate.now().minusWeeks(2), LocalDate.now().minusWeeks(1));
        sem.addVacation(v2);
        assertEquals(4, sem.getCurrentWeek());
    }
}
