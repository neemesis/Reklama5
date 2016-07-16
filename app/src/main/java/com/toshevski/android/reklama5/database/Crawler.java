package com.toshevski.android.reklama5.database;

import com.toshevski.android.reklama5.pojos.Grad;
import com.toshevski.android.reklama5.pojos.OglasDetalno;
import com.toshevski.android.reklama5.pojos.OglasOsnovno;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    public ArrayList<Grad> getCities() throws IOException {
        Document doc = Jsoup.connect("http://m.reklama5.mk").get();

        ArrayList<Grad> gradovi = new ArrayList<>();

        Elements cities = doc.select(".city-container");

        for (Element element : cities) {
            Element href = element.select("a").first();
            if (href != null) {
                Grad g = new Grad(element.text(), href.absUrl("href"));
                gradovi.add(g);
            }
        }

        return gradovi;
    }

    public OglasDetalno getDetailedAd(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements slikiWrap = doc.select(".img-responsive");
        ArrayList<String> slLista = new ArrayList<>();
        for (Element e : slikiWrap) {
            String urlSl = e.absUrl("src");
            if (!urlSl.contains("Content")) {
                slLista.add(urlSl);
            }
        }
        String ime = doc.select("h2").first().text();
        String cena = doc.select(".adDetailPrice").first().text();

        Elements vrednosti = doc.select(".adValue");
        ArrayList<String> vredList = new ArrayList<>();
        for (Element element : vrednosti) {
            vredList.add(element.text());
        }

        String opis = doc.select("p").get(2).text();
        String gazda = doc.select(".user-name").first().text();
        String lokacija = doc.select(".showMap").first().text();
        String broj = doc.select(".phone").first().text();

        String allData = doc.select(".adHeading").first().text();
        String objaven = allData.substring(allData.indexOf('О'), allData.indexOf('П'));

        return new OglasDetalno(slLista, ime, cena, vredList, opis, gazda, lokacija, broj, objaven);
    }

    public ArrayList<OglasOsnovno> getAllAds(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements oglasi = doc.select(".OglasResults");

        ArrayList<OglasOsnovno> oglasiLista = new ArrayList<>();

        for (Element e : oglasi) {
            oglasiLista.add(getAdFromElement(e));
        }

        return oglasiLista;
    }

    public OglasOsnovno getAdFromElement(Element e) {
        String slika = e.select("img").first().absUrl("src");
        String ime = e.select(".SearchAdTitle").first().text();
        String cena = e.select(".price-mobile-box").first().text();
        String lokacijaDatum = e.select(".adCitydateInfo").first().text().replace("   ", "&");
        String lokacija = lokacijaDatum.split("&")[0];
        String vreme = lokacijaDatum.split("&")[1];
        String url = e.select("a").first().absUrl("href");

        return new OglasOsnovno(ime, cena, lokacija, vreme, slika, url);
    }
}
