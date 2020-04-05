package domain;

public class StudentGrade {
    private String studentName;
    private Integer group;
    private Double grade;

    public StudentGrade(String student, Integer g, Double grade) {
        this.studentName = student;
        this.group = g;
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
