package repository;

import domain.Vanzare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VanzareRepository {
    String fileName;
    List<Vanzare> vanzari = new ArrayList<>();

    public VanzareRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    public List<Vanzare> findAll() {
        return vanzari;
    }


    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i += 4) {
                String[] line1 = lines.get(i).split(",");
                String[] line2 = lines.get(i + 1).split(",");
                List<Integer> locuri = new ArrayList<>();
                for (int j = 0; j < line2.length; j++) {
                    locuri.add(Integer.parseInt(line2[j]));
                }
                Vanzare vanzare = new Vanzare(Integer.parseInt(line1[0]), line1[1], Integer.parseInt(line1[2]), locuri, Double.parseDouble(lines.get(i + 2)));
                vanzari.add(vanzare);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeData() {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            vanzari.forEach(vanzare -> {
                try {
                    writer.write(vanzare.getId_spectacol() + "," + vanzare.getData() + "," + vanzare.getNr_bilete_vandute() + "\n");
                    StringBuilder l = new StringBuilder();
                    for (int i = 0; i < vanzare.getLocuri_vandute().size(); i++) {
                        l.append(vanzare.getLocuri_vandute().get(i)).append(",");
                    }
                    l.deleteCharAt(l.length() - 1);
                    writer.write(l.toString() + "\n");
                    writer.write(vanzare.getSuma() + "\n");
                    writer.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void save(Vanzare vanzare) {
        vanzari.add(vanzare);
        writeData();
    }
}
