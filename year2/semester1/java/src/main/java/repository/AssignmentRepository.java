package repository;

import domain.Assignment;
import domain.Semester;
import validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class AssignmentRepository extends InMemoryRepository<String, Assignment> {
    public AssignmentRepository(Validator<Assignment> validator) {
        super(validator);
    }
}
