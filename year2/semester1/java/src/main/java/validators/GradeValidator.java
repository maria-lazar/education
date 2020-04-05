package validators;

import domain.Grade;

public class GradeValidator implements Validator<Grade> {
    @Override
    public void validate(Grade entity) throws ValidationException {
        if (entity.getGrade() < 1 || entity.getGrade() > 10) {
            throw new ValidationException("Grade must be between 1 and 10");
        }
    }
}
