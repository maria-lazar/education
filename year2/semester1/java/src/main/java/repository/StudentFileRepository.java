package repository;

import domain.GradeDTO;
import domain.Student;
import validators.Validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StudentFileRepository extends FileRepository<String, Student> {
    public StudentFileRepository(Validator<Student> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected String writeEntity(Student student) {
        return student.getId() + "," + student.getLastName() + "," + student.getFirstName() + "," + student.getGroup() + "," + student.getEmail() +
                "," + student.getGuidingProfessor();
    }

    @Override
    public Student createEntity(String line) {
        String[] l = line.split(",");
        return new Student(l[0], l[1], l[2], Integer.parseInt(l[3]), l[4], l[5]);
    }

    public void saveFeedback(GradeDTO g, String name) throws IOException {
        File file = new File("data/" + name + ".txt");
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter w = new BufferedWriter(writer);
        w.newLine();
        String a = "Assignment: " + String.valueOf(g.getAssignmentNumber());
        w.write(a);
        w.newLine();
        String grade = "Grade: " + String.valueOf(g.getGrade());
        w.write(grade);
        w.newLine();
        String week = "Week: " + String.valueOf(g.getWeek());
        w.write(week);
        w.newLine();
        String deadline = "Deadline: " + String.valueOf(g.getDeadline());
        w.write(deadline);
        w.newLine();
        String f = "Feedback: " + String.valueOf(g.getFeedback());
        w.write(f);
        w.newLine();
        w.close();
    }
}
