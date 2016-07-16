package com.toshevski.android.reklama5.pojos;

public class Grad {
    private String ime;
    private String url;
    public Grad(String ime, String url) {
        super();
        this.ime = ime;
        this.url = url;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return "Grad [ime=" + ime + ", url=" + url + "]";
    }

}