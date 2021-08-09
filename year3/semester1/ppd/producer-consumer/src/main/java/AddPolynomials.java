import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AddPolynomials {
    public static void parallelVariant(int p1, int p2, int n) throws InterruptedException {
        long startTime = System.nanoTime();

        LinkedList l = new LinkedList();
        BlockingQueue blockingQueue = new BlockingQueue(p1);

        Thread[] readerThreads = new Thread[p1];
        int nrFilesPerReader = n / p1;
        int rest = n % p1;
        int begin = 0;
        int end;
        for (int i = 0; i < p1; i++) {
            end = begin + nrFilesPerReader;
            if (rest > 0) {
                rest--;
                end++;
            }
            readerThreads[i] = new Thread(new ReaderThread(blockingQueue, begin, end));
            readerThreads[i].start();
            begin = end;
        }

        Thread[] workerThreads = new Thread[p2];
        for (int i = 0; i < p2; i++) {
            workerThreads[i] = new Thread(new WorkerThread(blockingQueue, l, i));
            workerThreads[i].start();
        }

        for (int i = 0; i < p1; i++) {
            readerThreads[i].join();
        }
        for (int i = 0; i < p2; i++) {
            workerThreads[i].join();
        }

        long endTime = System.nanoTime();
        System.out.println((double) (endTime - startTime) / 1E6);
        l.writeListToFile("data/result.txt");
    }

    public static void sequentialVariant(int nrFiles) throws InterruptedException {
        long startTime = System.nanoTime();

        LinkedList l = new LinkedList();
        Thread t = new Thread(() -> {
            for (int i = 0; i < nrFiles; i++) {
                Path path = Paths.get("data/date" + i + ".txt");
                try {
                    List<String> fileContents = Files.readAllLines(path);
                    for (String monomial : fileContents) {
                        String[] m = monomial.split(" ");
                        l.addMonomialSeq(Integer.parseInt(m[1]), Integer.parseInt(m[0]));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();

        long endTime = System.nanoTime();
        System.out.println((double) (endTime - startTime) / 1E6);

        l.writeListToFile("data/result2.txt");
    }
}
