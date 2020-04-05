package domain;

import validators.ValidationException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Year;

public class AcademicYearStructure extends Entity<Integer> {
    private int year;
    private Semester semester1;
    private Semester semester2;
    private static AcademicYearStructure instance = null;

    private AcademicYearStructure(int year) {
        this.year = year;
    }

    public static AcademicYearStructure getInstance(){
        return instance;
    }

    public static AcademicYearStructure readFromFile(String fileName) throws IOException {
        AcademicYearStructure structure = new AcademicYearStructure(Year.now().getValue());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        structure.semester1 = Semester.readFrom(reader, 1);
        structure.semester2 = Semester.readFrom(reader, 2);
        instance = structure;
        return structure;
    }

    public int getCurrentWeek() {
        LocalDate currentDate = LocalDate.now();
        Semester sem;
        if (currentDate.isAfter(semester1.getStartDate()) && currentDate.isBefore(semester1.getEndDate())) {
            sem = semester1;
        } else if (currentDate.isAfter(semester2.getStartDate()) && currentDate.isBefore(semester2.getEndDate())) {
            sem = semester2;
        } else {
            throw new ValidationException("You're not placed in a semester");
        }
        return sem.getCurrentWeek();
    }

    public int getWeek(LocalDate date) {
        Semester sem;
        if (date.isAfter(semester1.getStartDate()) && date.isBefore(semester1.getEndDate())) {
            sem = semester1;
        } else if (date.isAfter(semester2.getStartDate()) && date.isBefore(semester2.getEndDate())) {
            sem = semester2;
        } else {
            throw new ValidationException("You're not placed in a semester");
        }
        return sem.getWeek(date);
    }
}
