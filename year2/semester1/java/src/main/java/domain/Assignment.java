package domain;

public class Assignment extends Entity<String> {
    private String description;
    private int startWeek;
    private int deadlineWeek;

    public Assignment(String id, String description, int startWeek, int deadlineWeek) {
        this.description = description;
        this.setId(id);
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    /**
     * Returns the assignment's description
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of description to the specified parameter: description
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the assignment's start week
     * @return the value of startWeek
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * Returns the assignment's deadline week
     * @return the value of deadlineWeek
     */
    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    /**
     * Sets the value of deadlineWeek to the specified parameter: deadlineWeek
     * @param deadlineWeek the new deadlineWeek
     */
    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    @Override
    public String toString() {
        return "lab " + getId() + " "+ " start week: " + startWeek + " deadline week: " + deadlineWeek;
    }
}
