package validators;

import domain.Grade;
import org.junit.Test;

import java.time.LocalDate;

public class GradeValidatorTest {
    @Test
    public void create(){
        GradeValidator v = new GradeValidator();
        Grade g = new Grade(11, LocalDate.now(), "a", "1", "1");
        try{
            v.validate(g);
        }catch (ValidationException ve){

        }
    }

}
