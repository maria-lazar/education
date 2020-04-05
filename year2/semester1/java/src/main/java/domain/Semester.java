package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Semester extends Entity<String>{
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Vacation> vacations;

    public Semester(String id, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.setId(id);
        vacations = new ArrayList<>();
    }

    public static Semester readFrom(BufferedReader reader, int nr) throws IOException {
        String[] line = reader.readLine().split(" ");
        LocalDate start = LocalDate.of(Integer.parseInt(line[2]), Integer.parseInt(line[1]), Integer.parseInt(line[0]));
        line = reader.readLine().split(" ");
        LocalDate end = LocalDate.of(Integer.parseInt(line[2]), Integer.parseInt(line[1]), Integer.parseInt(line[0]));
        Semester s = new Semester(String.valueOf(nr), start, end);
        int nrVacations = Integer.parseInt(reader.readLine());
        for (int i = 0; i < nrVacations; i++) {
            line = reader.readLine().split(" ");
            start = LocalDate.of(Integer.parseInt(line[2]), Integer.parseInt(line[1]), Integer.parseInt(line[0]));
            line = reader.readLine().split(" ");
            end = LocalDate.of(Integer.parseInt(line[2]), Integer.parseInt(line[1]), Integer.parseInt(line[0]));
            Vacation v = new Vacation(start, end);
            s.addVacation(v);
        }
        return s;
    }

    /**
     * Returns the semester's end date
     * @return the value of endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Adds a vacation to the list vacations
     * @param v the vacation
     */
    public void addVacation(Vacation v){
        vacations.add(v);
    }


    /**
     * Returns the semester's start date
     * @return the value of startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the semester's vacations
     * @return a list of Vacation containing the semester's vacations
     */
    public List<Vacation> getVacations() {
        return vacations;
    }

    /**
     * Returns the current week of the semester
     * @return the value of the current week of the semester
     */
    public int getCurrentWeek(){
        LocalDate currentDate = LocalDate.now(ZoneId.of("Europe/Bucharest"));
        long days = ChronoUnit.DAYS.between(startDate, currentDate);
        days++;
        int vacationDays = 0;
        for (Vacation v: vacations){
            if (currentDate.isAfter(v.getStartDate())){
                vacationDays += v.getDuration() + 1;
            }
        }
        days = days - vacationDays;
        return (int) (days / 7) + 1;
    }

    @Override
    public String toString() {
        return getId();
    }

    public int getWeek(LocalDate date) {
        long days = ChronoUnit.DAYS.between(startDate, date);
        days++;
        int vacationDays = 0;
        for (Vacation v: vacations){
            if (date.isAfter(v.getStartDate())){
                vacationDays += v.getDuration() + 1;
            }
        }
        days = days - vacationDays;
        return (int) (days / 7) + 1;
    }
}
