package services;

import domain.Student;
import util.Event;

public class StudentAddedEvent extends StudentEvent{
    private Student student;

    public StudentAddedEvent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
