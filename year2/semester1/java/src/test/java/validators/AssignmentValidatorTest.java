package validators;

import domain.AcademicYearStructure;
import domain.Assignment;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class AssignmentValidatorTest{
    @Test
    public void test() {
        Assignment a = new Assignment("2019-1", "a", 5, 6);
        AssignmentValidator validator = new AssignmentValidator();
        try {
            AcademicYearStructure structure = AcademicYearStructure.readFromFile("data/academicyeartest.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        validator.validate(a);
        a.setDeadlineWeek(4);
        try {
            validator.validate(a);
            fail();
        } catch (ValidationException e) {
            assertEquals("Deadline week must be greater than start week", e.getMessage());
        }
        a.setDeadlineWeek(16);
        try {
            validator.validate(a);
            fail();
        } catch (ValidationException e) {
            assertEquals("Invalid deadline week", e.getMessage());
        }
    }
}
