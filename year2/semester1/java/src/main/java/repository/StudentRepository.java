package repository;

import domain.Student;
import validators.Validator;

public class StudentRepository extends InMemoryRepository<String, Student> {
    public StudentRepository(Validator<Student> validator) {
        super(validator);
    }
}
