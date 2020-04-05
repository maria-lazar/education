package repository;

import domain.Entity;
import domain.Student;
import org.junit.Test;
import validators.StudentValidator;
import validators.ValidationException;
import validators.Validator;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class StudentRepositoryTest extends InMemoryRepositoryTest<String, Student> {
    @Override
    protected StudentValidator validator() {
        return new StudentValidator();
    }

    @Override
    protected Student createInvalidEntity() {
        throw new ValidationException("Invalid student");
    }

    @Override
    protected Student createExistingEntity() {
        return new Student("1", "b", "b", 224, "b", "b");
    }

    @Override
    protected Student createValidEntity() {
        return new Student("1", "a", "a", 224, "a", "a");
    }
}
