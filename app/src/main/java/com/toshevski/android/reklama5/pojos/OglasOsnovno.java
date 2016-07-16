package com.toshevski.android.reklama5.pojos;

public class OglasOsnovno {
    String ime;
    String cena;
    String lokacija;
    String vreme;
    String slika;
    String url;

    public OglasOsnovno(String ime, String cena, String lokacija, String vreme, String slika, String url) {
        super();
        this.ime = ime;
        this.cena = cena;
        this.lokacija = lokacija;
        this.vreme = vreme;
        this.slika = slika;
        this.url = url;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }
    public String getLokacija() {
        return lokacija;
    }
    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    public String getVreme() {
        return vreme;
    }
    public void setVreme(String vreme) {
        this.vreme = vreme;
    }
    public String getSlika() {
        return slika;
    }
    public void setSlika(String slika) {
        this.slika = slika;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return ime + "\n" + cena + "\n" + lokacija + "\n" + vreme + "\n" + url + "\n" + slika + "\n";
    }

}
