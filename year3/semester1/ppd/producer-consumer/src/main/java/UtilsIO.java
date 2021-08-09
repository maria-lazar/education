import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UtilsIO {
    public static void createFiles(int n, int maxG, int nrMaxMon) {
        for (int i = 0; i < n; i++) {
            File file = new File("data/date" + i + ".txt");
            try {
                file.createNewFile();
                List<Integer> mons = new ArrayList<>();
                FileWriter fw = new FileWriter(file);
                int nrMon = (int) ((Math.random() * (nrMaxMon + 1 - 1)) + 1);
                for (int j = 0; j < nrMon; j++) {
                    int g = (int) ((Math.random() * (maxG + 1)) + 0);
                    while (mons.contains(g)) {
                        g = (int) ((Math.random() * (maxG + 1)) + 0);
                    }
                    mons.add(g);
                    int c = (int) ((Math.random() * 40) - 20);
                    while (c == 0) {
                        c = (int) ((Math.random() * 40) - 20);
                    }
                    fw.write(c + " " + g + "\n");
                }
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean equalFilesValues(String file1, String file2) {
        try {
            Path path1 = Paths.get(file1);
            Path path2 = Paths.get(file2);
            List<String> fileContents1 = Files.readAllLines(path1);
            List<String> fileContents2 = Files.readAllLines(path2);
            if (fileContents1.size() != fileContents2.size()) {
                return false;
            }
            for (int i = 0; i < fileContents1.size(); i++) {
                if (!fileContents1.get(i).equals(fileContents2.get(i))) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
