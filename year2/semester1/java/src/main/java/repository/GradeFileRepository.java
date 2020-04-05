package repository;

import domain.Grade;
import domain.GradeID;
import validators.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GradeFileRepository extends FileRepository<GradeID, Grade> {
    public GradeFileRepository(Validator<Grade> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected String writeEntity(Grade grade) {
        return grade.getId().getStudentId() + "," + grade.getId().getAssignmentId() + "," + grade.getDate() + "," + grade.getProfessor() + ","
                + grade.getGrade();
    }

    @Override
    public Grade createEntity(String line) {
        String[] g = line.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(g[2], formatter);
        return new Grade(Float.parseFloat(g[4]), date, g[3], g[0], g[1]);
    }

}
