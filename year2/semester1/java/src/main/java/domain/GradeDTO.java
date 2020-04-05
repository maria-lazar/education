package domain;

public class GradeDTO {
    private String name;
    private int assignmentNumber;
    private float grade;
    private int week;
    private int deadline;
    private String feedback;

    public GradeDTO(String n, int assignmentNumber, float grade, int week, int deadline, String feedback) {
        this.assignmentNumber = assignmentNumber;
        this.grade = grade;
        this.week = week;
        this.deadline = deadline;
        this.feedback = feedback;
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public int getAssignmentNumber() {
        return assignmentNumber;
    }

    public float getGrade() {
        return grade;
    }

    public int getWeek() {
        return week;
    }

    public int getDeadline() {
        return deadline;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "name: " + name + " assignmentNumber: " + assignmentNumber + " grade: " + grade + " week: " + week + " deadline: " + deadline;
    }
}
