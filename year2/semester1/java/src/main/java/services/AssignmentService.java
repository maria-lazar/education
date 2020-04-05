package services;

import domain.AcademicYearStructure;
import domain.Assignment;
import repository.AssignmentFileRepository;
import repository.XMLAssignmentFileRepository;
import validators.AssignmentValidator;
import validators.ValidationException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AssignmentService {
    //private AssignmentFileRepository repository;
    private XMLAssignmentFileRepository repository;

    public AssignmentService(XMLAssignmentFileRepository repository) {
        this.repository = repository;
    }

    public Iterable<Assignment> getAll() {
        return repository.findAll();
    }

    public Assignment add(String s, String s1, String s2) {
        int currentWeek = AcademicYearStructure.getInstance().getCurrentWeek();
        Assignment a = new Assignment(s, s1, currentWeek, Integer.parseInt(s2));
        return repository.save(a);
    }

    public Assignment update(String s, String s1, String s2) {
        Assignment found = repository.findOne(s);
        if (found == null){
            throw new ValidationException("Assignment not found");
        }
        Assignment a = new Assignment(s, s1, found.getStartWeek(), Integer.parseInt(s2));
        AssignmentValidator validator = new AssignmentValidator();
        validator.validateDeadline(a);
        return repository.update(a);
    }

    public Assignment delete(String id) {
        return repository.delete(id);
    }

    public Assignment get(String id) {
        return repository.findOne(id);
    }

    public Assignment getCurrentAssignment(int currentWeek) {
        List<Assignment> assignments = StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        for (Assignment a: assignments) {
            if (a.getDeadlineWeek() == currentWeek){
                return a;
            }
        }
        return null;
    }
}
