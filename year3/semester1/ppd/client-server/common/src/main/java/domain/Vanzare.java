package domain;

import java.util.List;

public class Vanzare {
    Integer id_spectacol;
    String data;
    Integer nr_bilete_vandute;
    List<Integer> locuri_vandute;
    Double suma;

    public Vanzare(Integer id_spectacol, String data, Integer nr_bilete_vandute, List<Integer> locuri_vandute, Double suma) {
        this.id_spectacol = id_spectacol;
        this.data = data;
        this.nr_bilete_vandute = nr_bilete_vandute;
        this.locuri_vandute = locuri_vandute;
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "Vanzare{" +
                "id_spectacol=" + id_spectacol +
                ", data='" + data + '\'' +
                ", nr_bilete_vandute=" + nr_bilete_vandute +
                ", locuri_vandute=" + locuri_vandute +
                ", suma=" + suma +
                '}';
    }

    public Integer getId_spectacol() {
        return id_spectacol;
    }

    public void setId_spectacol(Integer id_spectacol) {
        this.id_spectacol = id_spectacol;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getNr_bilete_vandute() {
        return nr_bilete_vandute;
    }

    public void setNr_bilete_vandute(Integer nr_bilete_vandute) {
        this.nr_bilete_vandute = nr_bilete_vandute;
    }

    public List<Integer> getLocuri_vandute() {
        return locuri_vandute;
    }

    public void setLocuri_vandute(List<Integer> locuri_vandute) {
        this.locuri_vandute = locuri_vandute;
    }

    public Double getSuma() {
        return suma;
    }

    public void setSuma(Double suma) {
        this.suma = suma;
    }
}
