package services;

import domain.*;
import repository.XMLAssignmentFileRepository;
import repository.XMLGradeFileRepository;
import repository.XMLStudentFileRepository;
import util.Event;
import util.Observable;
import util.Observer;
import validators.ValidationException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GradeService implements Observable<Event> {
    private XMLGradeFileRepository repository;
    private XMLStudentFileRepository repoStudent;
    private XMLAssignmentFileRepository repoAssignment;
    private List<Observer<Event>> observers = new ArrayList<>();


    public GradeService(XMLGradeFileRepository repository, XMLStudentFileRepository repoStudent, XMLAssignmentFileRepository repoAssignment) {
        this.repository = repository;
        this.repoStudent = repoStudent;
        this.repoAssignment = repoAssignment;
    }

    public int calculateMaxGrade(String idAssignment, int m, LocalDate date, int flag) {
        if (repoAssignment.findOne(idAssignment) == null) {
            throw new ValidationException("Assignment does not exist");
        }
        int currentWeek = AcademicYearStructure.getInstance().getCurrentWeek();
        int deadline = repoAssignment.findOne(idAssignment).getDeadlineWeek();
        if (currentWeek <= deadline) {
            return 10;
        }
        if (flag == 1) {
            if (date.isAfter(LocalDate.now())) {
                throw new ValidationException("Invalid date");
            }
            currentWeek = AcademicYearStructure.getInstance().getWeek(date);
        }
        int late = currentWeek - deadline - m;
        if (late > 2) {
            throw new ValidationException("The assignment deadline passed by more than 2 weeks");
        }
        if (late < 0) {
            return 10;
        }
        return 10 - late;
    }


    public Grade add(String idStudent, String idAssignment, float grade, String p, LocalDate date) {
        if (repoStudent.findOne(idStudent) == null) {
            throw new ValidationException("Student does not exist");
        }
        Grade gr = new Grade(grade, date, p, idStudent, idAssignment);
        Grade g = repository.save(gr);
        notifyAll(new GradeAddedEvent(g));
        return g;
    }

    public Iterable<Grade> getAll() {
        return repository.findAll();
    }

    public void addFeedback(String idS, String idA, String f) throws IOException {
        Grade g = repository.findOne(new GradeID(idS, idA));
        Assignment a = repoAssignment.findOne(idA);
        int asNumber = Integer.parseInt(a.getId());
        float grade = g.getGrade();
        int week = AcademicYearStructure.getInstance().getWeek(g.getDate());
        int deadline = a.getDeadlineWeek();
        Student student = repoStudent.findOne(idS);
        String name = student.getFirstName() + student.getLastName();
        repoStudent.saveFeedback(new GradeDTO(name, asNumber, grade, week, deadline, f));
    }

    public Grade getGrade(String s, String a) {
        return repository.findOne(new GradeID(s, a));
    }

    public List<Student> getStudentsAssignment(String a) {
        if (repoAssignment.findOne(a) == null) {
            throw new ValidationException("Assignment not found");
        }
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(g -> g.getId().getAssignmentId().equals(a))
                .map(g -> {
                    return repoStudent.findOne(g.getId().getStudentId());
                })
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsAssignmentProfessor(String a, String p) {
        if (repoAssignment.findOne(a) == null) {
            throw new ValidationException("Assignment not found");
        }
        Predicate<Grade> p1 = x -> x.getId().getAssignmentId().equals(a);
        Predicate<Grade> p2 = x -> x.getProfessor().equals(p);
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(p1.and(p2))
                .map(g -> repoStudent.findOne(g.getId().getStudentId()))
                .collect(Collectors.toList());
    }

    public List<GradeDTO> getGradesAssignment(String a, int w) {
        if (repoAssignment.findOne(a) == null) {
            throw new ValidationException("Assignment not found");
        }
        if (w < 1 || w > 14) {
            throw new ValidationException("Invalid week");
        }
        Predicate<Grade> p1 = x -> x.getId().getAssignmentId().equals(a);
        Predicate<Grade> p2 = x -> AcademicYearStructure.getInstance().getWeek(x.getDate()) == w;
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(p1.and(p2))
                .map(g -> {
                    int week = AcademicYearStructure.getInstance().getWeek(g.getDate());
                    int deadline = repoAssignment.findOne(g.getId().getAssignmentId()).getDeadlineWeek();
                    Student s = repoStudent.findOne(g.getId().getStudentId());
                    String name = s.getFirstName() + " " + s.getLastName();
                    return new GradeDTO(name, Integer.parseInt(a), g.getGrade(), week, deadline, "");
                })
                .collect(Collectors.toList());
    }

    public List<GradeDTO> getGrades() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(g -> {
                    int week = AcademicYearStructure.getInstance().getWeek(g.getDate());
                    Assignment a = repoAssignment.findOne(g.getId().getAssignmentId());
                    int deadline = a.getDeadlineWeek();
                    Student s = repoStudent.findOne(g.getId().getStudentId());
                    String name = s.getFirstName() + " " + s.getLastName();
                    return new GradeDTO(name, Integer.parseInt(a.getId()), g.getGrade(), week, deadline, "");
                })
                .collect(Collectors.toList());
    }

    public double getStudentGrade(String idStudent) {
        List<Grade> grades = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(g -> g.getId().getStudentId().equals(idStudent))
                .collect(Collectors.toList());
        float sum = 0;
        int p = 0;
        for (Assignment a : repoAssignment.findAll()) {
            int pond = a.getDeadlineWeek() - a.getStartWeek();
            p = p + pond;
        }
        for (Grade g : grades) {
            Assignment a = repoAssignment.findOne(g.getId().getAssignmentId());
            int pond = a.getDeadlineWeek() - a.getStartWeek();
            sum = sum + g.getGrade() * pond;
        }
        if (p == 0){
            return 0;
        }
        return sum / p;
    }

    public List<StudentGrade> studentsGrade() {
        return StreamSupport.stream(repoStudent.findAll().spliterator(), false)
                .map(s -> {
                    return new StudentGrade(s.getFirstName() + " " + s.getLastName(), s.getGroup(), getStudentGrade(s.getId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<Event> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Event> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAll(Event event) {
        observers.forEach(o -> o.update(event));
    }

    public String findMostDifficult() {
        List<Assignment> assignments = StreamSupport.stream(repoAssignment.findAll().spliterator(), false)
                .filter(a -> a.getDeadlineWeek() < AcademicYearStructure.getInstance().getCurrentWeek())
                .collect(Collectors.toList());
        Assignment max = null;
        float m = 11;
        for (Assignment a : assignments) {
            double grade = StreamSupport.stream(repository.findAll().spliterator(), false)
                    .filter(g -> g.getId().getAssignmentId().equals(a.getId()))
                    .map(Grade::getGrade)
                    .reduce((float) 0, Float::sum);
            int s = (int) StreamSupport.stream(repoStudent.findAll().spliterator(), false).count();
            if (grade / s < m) {
                max = a;
                m = (float) (grade / s);
            }
        }
        assert max != null;
        return max.getId();
    }

    public List<StudentGrade> onTimeStudents() {
        List<StudentGrade> students = new ArrayList<>();
        int nrA = (int) StreamSupport.stream(repoAssignment.findAll().spliterator(), false)
                .filter(as -> as.getDeadlineWeek() < AcademicYearStructure.getInstance().getCurrentWeek())
                .count();
        for (Student s : repoStudent.findAll()) {
            int t = (int) StreamSupport.stream(repository.findAll().spliterator(), false)
                    .filter(g -> {
                        Assignment a = repoAssignment.findOne(g.getId().getAssignmentId());
                        int week = AcademicYearStructure.getInstance().getWeek(g.getDate());
                        return g.getId().getStudentId().equals(s.getId()) && week <= a.getDeadlineWeek() && a.getDeadlineWeek() < AcademicYearStructure.getInstance().getCurrentWeek();
                    })
                    .count();
            if (t == nrA) {
                students.add(new StudentGrade(s.getFirstName() + " " + s.getLastName(), s.getGroup(), getStudentGrade(s.getId())));
            }
        }
        return students;
    }
}