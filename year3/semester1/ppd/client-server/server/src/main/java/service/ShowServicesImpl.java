package service;

import domain.Sala;
import domain.Spectacol;
import domain.Vanzare;
import dto.VanzareDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.SalaRepository;
import repository.SpectacolRepository;
import repository.VanzareRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShowServicesImpl implements ShowServices {
    private static final Logger LOGGER = LogManager.getLogger(ShowServicesImpl.class.getName());
    private SpectacolRepository spectacolRepository;
    private SalaRepository salaRepository;
    private VanzareRepository vanzareRepository;


    public ShowServicesImpl(SpectacolRepository spectacolRepository, SalaRepository salaRepository, VanzareRepository vanzareRepository) {
        this.spectacolRepository = spectacolRepository;
        this.salaRepository = salaRepository;
        this.vanzareRepository = vanzareRepository;
    }

    @Override
    public synchronized String buy(VanzareDto vanzare) {
        Sala sala = salaRepository.findOne();
        Spectacol spectacol = spectacolRepository.findOne(vanzare.getId_spectacol());
        if (vanzare.getNr_bilete_vandute() > sala.getNr_locuri()) {
//            LOGGER.warn("Prea multe bilete");
            throw new RuntimeException("Prea multe bilete");
        }
        vanzare.getLocuri_vandute().forEach(loc -> {
            if (loc > sala.getNr_locuri()) {
//                LOGGER.warn("Loc invalid: " + loc);
                throw new RuntimeException("Loc invalid");
            }
            if (spectacol.getLocuri_vandute().contains(loc)) {
//                LOGGER.warn("Loc ocupat: " + loc);
                throw new RuntimeException("Locul e ocupat");
            }
        });
        List<Integer> locuri_vandute = spectacol.getLocuri_vandute();
        locuri_vandute.addAll(vanzare.getLocuri_vandute());
        spectacol.setLocuri_vandute(locuri_vandute);
        double suma = vanzare.getNr_bilete_vandute() * spectacol.getPret();
        spectacol.setSold(spectacol.getSold() + suma);
        spectacolRepository.update(spectacol);
        vanzareRepository.save(new Vanzare(vanzare.getId_spectacol(), vanzare.getData(), vanzare.getNr_bilete_vandute(), vanzare.getLocuri_vandute(), suma));
        return "Vanzare reusita!";
    }


    public synchronized void check() {
        try (FileWriter fw = new FileWriter("Verificare.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            LocalDateTime currentTime = LocalDateTime.now();
            spectacolRepository.findAll().forEach(((id, spectacol) -> {
                List<Integer> locuriOcupate = new ArrayList<>(spectacol.getLocuri_vandute());
                double sold = spectacol.getSold();
                List<Vanzare> vanzari = vanzareRepository.findAll().stream().filter(vanzare -> vanzare.getId_spectacol().equals(id)).collect(Collectors.toList());
                int suma = 0;
                List<Integer> locuriVandute = new ArrayList<>();
                for (Vanzare vanzare : vanzari) {
                    suma += vanzare.getSuma();
                    List<Integer> locuriVanzare = vanzare.getLocuri_vandute();
                    locuriVandute.addAll(locuriVanzare);
                }
                Collections.sort(locuriOcupate);
                Collections.sort(locuriVandute);
                boolean corect = true;
                if (!locuriOcupate.equals(locuriVandute)) {
                    corect = false;
                }
                boolean soldCorect = true;
                if (sold != suma) {
                    soldCorect = false;
                }
                String s = "";
                if (corect && soldCorect) {
                    s = "corect";
                } else {
                    s = "incorect";
                }
                out.write(currentTime + " , " + s + " -- " + "spectacolId: " + id + "\n");
            }));
            out.write("\n");
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
