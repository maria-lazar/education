package repository;

import domain.Assignment;
import validators.Validator;

public class AssignmentFileRepository extends FileRepository<String, Assignment> {
    public AssignmentFileRepository(Validator<Assignment> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected String writeEntity(Assignment assignment) {
        String result = assignment.getId() + "," + assignment.getDescription() + "," + assignment.getStartWeek() + "," + assignment.getDeadlineWeek();
        return result;
    }

    @Override
    public Assignment createEntity(String line) {
        String[] l = line.split(",");
        return new Assignment(l[0], l[1], Integer.parseInt(l[2]), Integer.parseInt(l[3]));
    }

}
