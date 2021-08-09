package util;

import domain.Block;
import domain.P3Image;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IOUtils {
    public static void printMatrix(double[][] v) {
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[0].length; j++) {
                System.out.print(((int)v[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public static void printList(List<Integer> v) {
        for (int i = 0; i < v.size(); i++) {
            System.out.print(v.get(i) + ",");
        }
    }

    public static boolean equalLists(List<Integer> l1, List<Integer> l2) {
        if (l1.size() != l2.size()){
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (!l1.get(i).equals(l2.get(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean equalListsOfBlocks(List<Block> l1, List<Block> l2) {
        if (l1.size() != l2.size()){
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (!equalMatrix(l1.get(i).getValues(), l2.get(i).getValues())){
                return false;
            }
        }
        return true;
    }

    public static void printListOfBlocks(List<?> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = (Block) blocks.get(i);
            System.out.println(block);
        }
    }

    public static P3Image readP3File(String file) throws IOException {
        Path path = Paths.get(file);
        List<String> fileContents = Files.readAllLines(path);

        int width = -1, height = -1;
        double maxRange = -1;
        String format = null;
        int i = 0;
        // read format
        while (i < fileContents.size()) {
            String line = fileContents.get(i);
            i++;
            if (line.charAt(0) != '#') {
                format = line;
                break;
            }
        }
        // read image size
        while (i < fileContents.size()) {
            String line = fileContents.get(i);
            i++;
            if (line.charAt(0) != '#') {
                String[] imageSize = line.split("\\s");
                width = Integer.parseInt(imageSize[0]);
                height = Integer.parseInt(imageSize[1]);
                break;
            }
        }
        // read max range
        while (i < fileContents.size()) {
            String line = fileContents.get(i);
            i++;
            if (line.charAt(0) != '#') {
                maxRange = Double.parseDouble(line);
                break;
            }
        }
        for (int j = 0; j < i; j++) {
            fileContents.remove(0);
        }
        // read rgb values
        double[][] r = new double[height][width];
        double[][] g = new double[height][width];
        double[][] b = new double[height][width];
        int k = 0;
        for (i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                r[i][j] = Double.parseDouble(fileContents.get(k));
                g[i][j] = Double.parseDouble(fileContents.get(k + 1));
                b[i][j] = Double.parseDouble(fileContents.get(k + 2));
                k += 3;
            }
        }
        return new P3Image(height, width, maxRange, r, g, b);
    }

    public static void writeImageToFile(String fileName, double[][] r, double[][] g, double[][] b, double maxRange) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("P3\n");
        writer.write(r[0].length + " " + r.length + "\n");
        writer.write((int) maxRange + "\n");
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                writer.write(Math.round(r[i][j]) + "\n");
                writer.write(Math.round(g[i][j]) + "\n");
                writer.write(Math.round(b[i][j]) + "\n");
            }
        }
        writer.close();
    }

    public static boolean equalMatrix(double[][] m1, double[][] m2) {
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                if (m1[i][j] != m2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
