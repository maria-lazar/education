public class WorkerThread implements Runnable {
    private BlockingQueue queue;
    private LinkedList list;
    private int name;

    public WorkerThread(BlockingQueue queue, LinkedList list, int i) {
        this.queue = queue;
        this.list = list;
        this.name = i;
    }

    public void run() {
        while (true) {
            try {
                Monomial monomial = queue.dequeue();
                if (monomial != null) {
                    if (monomial.getCoefficient() == 0) {
//                        System.out.println("Thread " + name);
                        return;
                    }
                    list.addMonomial(monomial.getExponent(), monomial.getCoefficient());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
