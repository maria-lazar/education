package domain;

import java.util.Objects;

public class Student extends Entity<String> {
    private String lastName;
    private String firstName;
    private int group;
    private String email;
    private String guidingProfessor;

    public Student(String id, String lastName, String firstName, int group, String email, String guidingProfessor) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.group = group;
        this.email = email;
        this.guidingProfessor = guidingProfessor;
        this.setId(id);
    }

    /**
     * Returns the student's last name
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's first name
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's group
     * @return the value of group
     */
    public int getGroup() {
        return group;
    }

    /**
     * Returns the student's email
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the student's guiding professor
     * @return the value of guidingProfessor
     */
    public String getGuidingProfessor() {
        return guidingProfessor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return group == student.group &&
                lastName.equals(student.lastName) &&
                firstName.equals(student.firstName) &&
                email.equals(student.email) &&
                guidingProfessor.equals(student.guidingProfessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, group, email, guidingProfessor);
    }

    @Override
    public String toString() {
        return getId() + ". " + lastName + " " + firstName + " " + group;
    }
}
