package services;

import domain.Student;
import util.Event;

public class StudentUpdatedEvent extends StudentEvent {
    private Student oldStudent;
    private Student student;

    public Student getOldStudent() {
        return oldStudent;
    }

    public void setOldStudent(Student oldStudent) {
        this.oldStudent = oldStudent;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentUpdatedEvent(Student oldStudent, Student student) {
        this.oldStudent = oldStudent;
        this.student = student;
    }
}
