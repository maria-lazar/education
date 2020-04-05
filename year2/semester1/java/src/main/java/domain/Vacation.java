package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Vacation {
    private LocalDate startDate;
    private LocalDate endDate;

    public Vacation(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the vacation's start date
     * @return value of startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the vacation's end date
     * @return value of endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns vacation's week number
     * @return the value of weekNumber
     */
    public int getDuration() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
