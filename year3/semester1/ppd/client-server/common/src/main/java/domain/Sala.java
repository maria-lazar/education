package domain;

import java.util.List;

public class Sala {
    Integer nr_locuri;
    List<Integer> spectacol;

    public Integer getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(Integer nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    public List<Integer> getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(List<Integer> spectacol) {
        this.spectacol = spectacol;
    }

    public Sala(Integer nr_locuri, List<Integer> spectacol) {
        this.nr_locuri = nr_locuri;
        this.spectacol = spectacol;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "nr_locuri=" + nr_locuri +
                ", spectacol=" + spectacol +
                '}';
    }
}
