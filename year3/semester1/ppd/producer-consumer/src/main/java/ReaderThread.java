import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReaderThread implements Runnable {
    private int begin;
    private int end;
    private BlockingQueue queue;

    public ReaderThread(BlockingQueue queue, int begin, int end) {
        this.queue = queue;
        this.begin = begin;
        this.end = end;
    }

    public void run() {
        for (int i = begin; i < end; i++) {
            Path path = Paths.get("data/date" + i + ".txt");
            try {
                List<String> fileContents = Files.readAllLines(path);
                for (String monomial : fileContents) {
                    String[] m = monomial.split(" ");
                    queue.enqueue(new Monomial(Integer.parseInt(m[0]), Integer.parseInt(m[1])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        queue.enqueue(null);
    }
}
