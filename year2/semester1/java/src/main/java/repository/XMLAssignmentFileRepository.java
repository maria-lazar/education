package repository;

import domain.Assignment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import validators.Validator;

public class XMLAssignmentFileRepository extends XMLFileRepository<String, Assignment> {
    public XMLAssignmentFileRepository(Validator<Assignment> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Element createElementFromEntity(Document document, Assignment assignment) {
        Element element = document.createElement("assignment");
        element.setAttribute("id", assignment.getId());
        Element description = document.createElement("description");
        description.setTextContent(assignment.getDescription());
        element.appendChild(description);
        Element startWeek = document.createElement("startWeek");
        startWeek.setTextContent(String.valueOf(assignment.getStartWeek()));
        element.appendChild(startWeek);
        Element deadlineWeek = document.createElement("deadlineWeek");
        deadlineWeek.setTextContent(String.valueOf(assignment.getDeadlineWeek()));
        element.appendChild(deadlineWeek);
        return element;
    }

    @Override
    public Assignment createEntityFromElement(Element el) {
        String id = el.getAttribute("id");
        String description = el.getElementsByTagName("description").item(0).getTextContent();
        String startWeek = el.getElementsByTagName("startWeek").item(0).getTextContent();
        String deadlineWeek = el.getElementsByTagName("deadlineWeek").item(0).getTextContent();
        Assignment a = new Assignment(id, description, Integer.parseInt(startWeek), Integer.parseInt(deadlineWeek));
        return a;
    }
}
