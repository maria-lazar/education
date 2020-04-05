package domain;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

public class AcademicYearStructureTest {
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
    @Test
    public void create(){
        try {
            AcademicYearStructure structure = AcademicYearStructure.readFromFile("testdata/academicyeartest.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(5, AcademicYearStructure.getInstance().getCurrentWeek());
        assertEquals(4, AcademicYearStructure.getInstance().getWeek(LocalDate.now().minusWeeks(1)));
    }
}
