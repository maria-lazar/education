package domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static junit.framework.TestCase.assertEquals;

public class VacationTest {
    @Test
    public void create(){
        LocalDate start = LocalDate.of(2019, Month.DECEMBER, 20);
        LocalDate end = LocalDate.of(2020, Month.JANUARY, 5);
        Vacation v = new Vacation(start, end);
        assertEquals(start, v.getStartDate());
        assertEquals(end, v.getEndDate());
        assertEquals(17, v.getDuration());
    }
}
