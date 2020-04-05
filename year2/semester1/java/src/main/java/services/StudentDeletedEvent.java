package services;

import domain.Student;
import util.Event;

public class StudentDeletedEvent extends StudentEvent{
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentDeletedEvent(Student student) {
        this.student = student;
    }
}
