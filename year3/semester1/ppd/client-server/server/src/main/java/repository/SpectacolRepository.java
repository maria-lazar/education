package repository;

import domain.Spectacol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectacolRepository {
    String fileName;
    Map<Integer, Spectacol> spectacole = new HashMap<>();

    public SpectacolRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    public Spectacol findOne(Integer id) {
        if (spectacole.containsKey(id)) {
            return spectacole.get(id);
        }
        return null;
    }

    public Map<Integer, Spectacol> findAll() {
        return spectacole;
    }


    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i += 4) {
                String[] line1 = lines.get(i).split(",");
                String[] line2 = lines.get(i + 1).split(",");
                List<Integer> locuri = new ArrayList<>();
                if (!line2[0].equals("")) {
                    for (int j = 0; j < line2.length; j++) {
                        locuri.add(Integer.parseInt(line2[j]));
                    }
                }
                int id = Integer.parseInt(line1[0]);
                Spectacol spectacol = new Spectacol(Integer.parseInt(line1[0]), line1[1], line1[2], Double.parseDouble(line1[3]), locuri, Double.parseDouble(lines.get(i + 2)));
                spectacole.put(id, spectacol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeData() {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            spectacole.forEach(((id, spectacol) -> {
                try {
                    writer.write(id + "," + spectacol.getData() + "," + spectacol.getTitlu() + "," + spectacol.getPret() + "\n");
                    StringBuilder l = new StringBuilder();
                    for (int i = 0; i < spectacol.getLocuri_vandute().size(); i++) {
                        l.append(spectacol.getLocuri_vandute().get(i)).append(",");
                    }
                    if (l.length() > 0) {
                        l.deleteCharAt(l.length() - 1);
                    }
                    writer.write(l.toString() + "\n");
                    writer.write(spectacol.getSold() + "\n");
                    writer.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Spectacol spectacol) {
        if (!spectacole.containsKey(spectacol.getId()))
            return;
        spectacole.replace(spectacol.getId(), spectacol);
        writeData();
    }
}
