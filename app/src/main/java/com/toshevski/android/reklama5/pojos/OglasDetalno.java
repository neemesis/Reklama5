package com.toshevski.android.reklama5.pojos;

import java.util.ArrayList;

public class OglasDetalno {
    private ArrayList<String> sliki;
    private String ime;
    private String cena;
    private ArrayList<String> vrednosti;
    private String opis;
    private String gazda;
    private String lokacija;
    private String broj;
    private String objaven;

    public OglasDetalno(ArrayList<String> sliki, String ime, String cena, ArrayList<String> vid, String opis, String gazda,
                        String lokacija, String broj, String objaven) {
        super();
        this.sliki = sliki;
        this.ime = ime;
        this.cena = cena;
        this.vrednosti = vid;
        this.opis = opis;
        this.gazda = gazda;
        this.lokacija = lokacija;
        this.broj = broj;
        this.objaven = objaven;
    }

    public ArrayList<String> getSliki() {
        return sliki;
    }

    public void setSliki(ArrayList<String> sliki) {
        this.sliki = sliki;
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

    public ArrayList<String> getVid() {
        return vrednosti;
    }

    public void setVid(ArrayList<String> vid) {
        this.vrednosti = vid;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getGazda() {
        return gazda;
    }

    public void setGazda(String gazda) {
        this.gazda = gazda;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getObjaven() {
        return objaven;
    }

    public void setObjaven(String objaven) {
        this.objaven = objaven;
    }

    @Override
    public String toString() {
        return "OglasDetalno [sliki=" + sliki + ", ime=" + ime + ", cena=" + cena + ", vrednosti=" + vrednosti
                + ", opis=" + opis + ", gazda=" + gazda + ", lokacija=" + lokacija + ", broj=" + broj + ", objaven="
                + objaven + "]";
    }



}
