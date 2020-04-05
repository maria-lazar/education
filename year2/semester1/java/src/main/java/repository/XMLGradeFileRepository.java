package repository;

import domain.Grade;
import domain.GradeID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import validators.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XMLGradeFileRepository extends XMLFileRepository<GradeID, Grade> {
    public XMLGradeFileRepository(Validator<Grade> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Element createElementFromEntity(Document document, Grade grade) {
        Element element = document.createElement("grade");
        Element idStudent = document.createElement("idStudent");
        idStudent.setTextContent(grade.getId().getStudentId());
        element.appendChild(idStudent);
        Element idAssignment = document.createElement("idAssignment");
        idAssignment.setTextContent(grade.getId().getAssignmentId());
        element.appendChild(idAssignment);
        Element date = document.createElement("date");
        date.setTextContent(String.valueOf(grade.getDate()));
        element.appendChild(date);
        Element professor = document.createElement("professor");
        professor.setTextContent(grade.getProfessor());
        element.appendChild(professor);
        Element value = document.createElement("grade");
        value.setTextContent(String.valueOf(grade.getGrade()));
        element.appendChild(value);
        return element;
    }

    @Override
    public Grade createEntityFromElement(Element el) {
        String idStudent = el.getElementsByTagName("idStudent").item(0).getTextContent();
        String idAssignment = el.getElementsByTagName("idAssignment").item(0).getTextContent();
        String d = el.getElementsByTagName("date").item(0).getTextContent();
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String professor = el.getElementsByTagName("professor").item(0).getTextContent();
        float value = Float.parseFloat(el.getElementsByTagName("grade").item(0).getTextContent());
        Grade g = new Grade(value, date, professor, idStudent, idAssignment);
        return g;
    }
}
