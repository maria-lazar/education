import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private int exponent;
    private int coefficient;
    private Node next;
    private Lock lock = new ReentrantLock();

    public Node(int exponent, int coefficient, Node next) {
        this.exponent = exponent;
        this.coefficient = coefficient;
        this.next = next;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "Node{" +
                "exponent=" + exponent +
                ", coefficient=" + coefficient +
                '}';
    }
}
