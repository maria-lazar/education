package repository;

import domain.Assignment;
import validators.AssignmentValidator;
import validators.Validator;

public class AssignmentRepositoryTest extends InMemoryRepositoryTest<String, Assignment> {

    @Override
    protected Validator<Assignment> validator() {
        return new AssignmentValidator();
    }

    @Override
    protected Assignment createInvalidEntity() {
        return new Assignment("3", "a", 15, 1);
    }

    @Override
    protected Assignment createExistingEntity() {
        return new Assignment("1", "b", 5, 6);
    }

    @Override
    protected Assignment createValidEntity() {
        return new Assignment("1", "a", 10, 13);
    }
}
