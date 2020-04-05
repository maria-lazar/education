package repository;

import domain.GradeDTO;
import domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import validators.Validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLStudentFileRepository extends XMLFileRepository<String, Student> {
    public XMLStudentFileRepository(Validator<Student> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Element createElementFromEntity(Document document, Student student) {
        Element element = document.createElement("student");
        element.setAttribute("id", student.getId());
        Element lastName = document.createElement("lastName");
        lastName.setTextContent(student.getLastName());
        element.appendChild(lastName);
        Element firstName = document.createElement("firstName");
        firstName.setTextContent(student.getFirstName());
        element.appendChild(firstName);
        Element group = document.createElement("group");
        group.setTextContent(String.valueOf(student.getGroup()));
        element.appendChild(group);
        Element email = document.createElement("email");
        email.setTextContent(student.getEmail());
        element.appendChild(email);
        Element guidingProf = document.createElement("guidingProfessor");
        guidingProf.setTextContent(student.getGuidingProfessor());
        element.appendChild(guidingProf);
        return element;
    }

    @Override
    public Student createEntityFromElement(Element el) {
        String id = el.getAttribute("id");
        String lastName = el.getElementsByTagName("lastName").item(0).getTextContent();
        String firstName = el.getElementsByTagName("firstName").item(0).getTextContent();
        String group = el.getElementsByTagName("group").item(0).getTextContent();
        String email = el.getElementsByTagName("email").item(0).getTextContent();
        String guidingProfessor = el.getElementsByTagName("guidingProfessor").item(0).getTextContent();
        Student s = new Student(id, lastName, firstName, Integer.parseInt(group), email, guidingProfessor);
        return s;
    }

    public void saveFeedback(GradeDTO g) throws IOException {
        File file = new File("data/" + g.getName() + ".txt");
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
