package repository;

import domain.Sala;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SalaRepository {
    String fileName;
    Sala sala;

    public SalaRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    public Sala findOne() {
        return sala;
    }


    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            int nr_locuri = Integer.parseInt(lines.get(0));
            String[] spectacole = lines.get(1).split(",");
            List<Integer> sp = new ArrayList<>();
            for (int i = 0; i < spectacole.length; i++) {
                sp.add(Integer.parseInt(spectacole[i]));
            }
            Sala sala = new Sala(nr_locuri, sp);
            this.sala = sala;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
