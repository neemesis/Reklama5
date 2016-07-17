package com.toshevski.android.reklama5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.toshevski.android.reklama5.pojos.OglasOsnovno;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    private Context ctx;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Reklama5_Klient";

    public DBManager(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"Oglas\" (\n" +
                "  \"id\" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  \"ime\" TEXT NOT NULL,\n" +
                "  \"cena\" TEXT NOT NULL,\n" +
                "  \"lokacija\" TEXT NOT NULL,\n" +
                "  \"vreme\" TEXT NOT NULL,\n" +
                "  \"slika\" TEXT NOT NULL,\n" +
                "  \"url\" TEXT NOT NULL\n" +
                ")");
    }

    public void AddAd(OglasOsnovno o) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ime", o.getIme());
        cv.put("cena", o.getCena());
        cv.put("lokacija", o.getLokacija());
        cv.put("vreme", o.getVreme());
        cv.put("slika", o.getSlika());
        cv.put("url", o.getUrl());

        db.insert("Oglas", null, cv);
        db.close();
    }

    public ArrayList<OglasOsnovno> GetAllAds() {
        ArrayList<OglasOsnovno> oo = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Oglas", null);

        if (cursor.moveToFirst()) {
            do {
                String ime = cursor.getString(1);
                String cena = cursor.getString(2);
                String lokacija = cursor.getString(3);
                String vreme = cursor.getString(4);
                String slika = cursor.getString(5);
                String url = cursor.getString(6);
                oo.add(new OglasOsnovno(ime, cena, lokacija, vreme, slika, url));
            } while (cursor.moveToNext());
        }
        db.close();
        return oo;
    }

    public void delAd(OglasOsnovno o) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Oglas", "ime = ?", new String[] { o.getIme() });
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
