package domain;

import java.util.Objects;

public class GradeID {
    private String studentId;
    private String assignmentId;

    public GradeID(String studentId, String assignmentId) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeID gradeID = (GradeID) o;
        return studentId.equals(gradeID.studentId) &&
                assignmentId.equals(gradeID.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, assignmentId);
    }
}
