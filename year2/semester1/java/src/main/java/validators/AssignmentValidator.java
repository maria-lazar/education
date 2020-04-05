package validators;

import domain.AcademicYearStructure;
import domain.Assignment;
import domain.Entity;

public class AssignmentValidator implements Validator<Assignment> {

    @Override
    public void validate(Assignment entity) throws ValidationException {
        if (entity.getStartWeek() < 1 || entity.getStartWeek() > 14) {
            throw new ValidationException("Invalid start week");
        }
        if (entity.getDeadlineWeek() < 1 || entity.getDeadlineWeek() > 14) {
            throw new ValidationException("Invalid deadline week");
        }
        if (entity.getStartWeek() > entity.getDeadlineWeek()) {
            throw new ValidationException("Deadline week must be greater than start week");
        }
    }
    public void validateDeadline(Assignment entity){
        int currentWeek = AcademicYearStructure.getInstance().getCurrentWeek();
        if (entity.getDeadlineWeek() < currentWeek) {
            throw new ValidationException("The deadline week introduced passed");
        }
    }
}
