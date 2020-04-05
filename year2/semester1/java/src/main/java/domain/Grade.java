package domain;

import java.time.LocalDate;

public class Grade extends Entity<GradeID> {
    private LocalDate date;
    private String professor;
    private float grade;

    public Grade(float grade, LocalDate date, String professor, String idStudent, String idAssignment) {
        this.grade = grade;
        this.date = date;
        this.professor = professor;
        setId(new GradeID(idStudent, idAssignment));
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProfessor() {
        return professor;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "student ID: " + getId().getStudentId() + " assignment ID: " + getId().getAssignmentId() + " " + grade;
    }
}
