package services;

import domain.Student;
import util.Event;
import util.Observable;
import util.Observer;
import repository.XMLStudentFileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentService implements Observable<Event> {
    //private StudentFileRepository repository;
    private XMLStudentFileRepository repository;
    private List<Observer<Event>> observers = new ArrayList<Observer<Event>>();

    public StudentService(XMLStudentFileRepository repository) {
        this.repository = repository;
    }

    public Student add(String s, String s1, String s2, Integer s3, String s4, String s5) {
        Student student = new Student(s, s1, s2, s3, s4, s5);
        Student st = repository.save(student);
        notifyAll(new StudentAddedEvent(st));
        return st;
    }

    public Iterable<Student> getAll() {
        return repository.findAll();
    }

    public Student delete(String id) {
        Student s = repository.delete(id);
        notifyAll(new StudentDeletedEvent(s));
        return s;
    }

    public Student update(String s, String s1, String s2, Integer s3, String s4, String s5) {
        Student student = new Student(s, s1, s2, s3, s4, s5);
        Student st = repository.update(student);
        notifyAll(new StudentUpdatedEvent(st, student));
        return st;
    }

    public List<Student> getSameGroup(int g) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(s -> s.getGroup() == g)
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
}
