package domain;

import java.util.List;

public class Spectacol {
    Integer id;
    String data;
    String titlu;
    Double pret;
    List<Integer> locuri_vandute;
    Double sold;

    public Spectacol(Integer id, String data, String titlu, Double pret, List<Integer> locuri_vandute, Double sold) {
        this.id = id;
        this.data = data;
        this.titlu = titlu;
        this.pret = pret;
        this.locuri_vandute = locuri_vandute;
        this.sold = sold;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Spectacol{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", titlu='" + titlu + '\'' +
                ", pret=" + pret +
                ", locuri_vandute=" + locuri_vandute +
                ", sold=" + sold +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public List<Integer> getLocuri_vandute() {
        return locuri_vandute;
    }

    public void setLocuri_vandute(List<Integer> locuri_vandute) {
        this.locuri_vandute = locuri_vandute;
    }

    public Double getSold() {
        return sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }
}
