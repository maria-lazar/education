package services;

import domain.Grade;
import domain.Student;
import util.Event;

public class GradeAddedEvent implements Event {
    private Grade grade;

    public GradeAddedEvent(Grade g) {
        this.grade = g;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade g) {
        this.grade = g;
    }
}
