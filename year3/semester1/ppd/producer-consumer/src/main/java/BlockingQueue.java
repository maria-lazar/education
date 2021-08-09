import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueue {
    private ArrayList<Monomial> queue = new ArrayList<>();
    private AtomicInteger readersDone = new AtomicInteger(0);
    private int nrReaders;

    public BlockingQueue(int nrReaders) {
        this.nrReaders = nrReaders;
    }

    public synchronized void enqueue(Monomial monomial) {
//        System.out.println(monomial + "added");
        this.queue.add(monomial);
        notifyAll();
    }

    public synchronized Monomial dequeue() throws InterruptedException {
        while (this.queue.size() == 0 && readersDone.get() < nrReaders) {
            wait();
        }
        if (readersDone.get() == nrReaders) {
            return new Monomial(0, 0);
        }
        Monomial monomial = this.queue.get(0);
        if (monomial == null) {
            int readersDone = this.readersDone.get() + 1;
            this.readersDone.set(readersDone);
            if (readersDone == nrReaders) {
//                System.out.println(null + " final removed");
                notifyAll();
                return new Monomial(0, 0);
            }
        }
//        System.out.println(monomial + "removed");
        this.queue.remove(0);
        return monomial;
    }
}
