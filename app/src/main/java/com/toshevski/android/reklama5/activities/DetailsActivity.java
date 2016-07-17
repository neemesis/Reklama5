package com.toshevski.android.reklama5.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.toshevski.android.reklama5.R;
import com.toshevski.android.reklama5.adapters.ImageSliderAdapter;
import com.toshevski.android.reklama5.database.Crawler;
import com.toshevski.android.reklama5.pojos.OglasDetalno;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private ImageSliderAdapter isa;
    private OglasDetalno od;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        TextView counter = (TextView) findViewById(R.id.pagesCounter);
        isa = new ImageSliderAdapter(this, new ArrayList<String>(), counter);
        if (vp != null) {
            vp.setAdapter(isa);
        }

        CardView cv = (CardView) findViewById(R.id.brojZaPovik);
        if (cv != null) {
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + od.getBroj()));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            });
        }

        new GetDetailedAd().execute(getIntent().getStringExtra("url"));
    }

    private void loadData(OglasDetalno od) {
        this.od = od;
        isa.setLinks(od.getSliki());
        isa.notifyDataSetChanged();
        this.setTitle(od.getIme());

        TextView det_opis = (TextView) findViewById(R.id.det_opis);
        TextView det_cena = (TextView) findViewById(R.id.det_cena);
        TextView det_lokacija = (TextView) findViewById(R.id.det_lokacija);
        TextView det_objaven = (TextView) findViewById(R.id.det_objaven);
        TextView det_gazda = (TextView) findViewById(R.id.det_gazda);
        TextView det_broj = (TextView) findViewById(R.id.det_broj);


        if (det_opis != null) {
            det_opis.setText(od.getOpis());
        }
        if (det_cena != null) {
            det_cena.setText(od.getCena());
        }
        if (det_lokacija != null) {
            det_lokacija.setText(od.getLokacija());
        }
        if (det_objaven != null) {
            det_objaven.setText(od.getObjaven());
        }
        if (det_gazda != null) {
            det_gazda.setText(od.getGazda());
        }
        if (det_broj != null) {
            det_broj.setText(od.getBroj());
        }
    }

    class GetDetailedAd extends AsyncTask<String, Void, Void> {

        private OglasDetalno od;

        @Override
        protected Void doInBackground(String... params) {
            try {
                od = new Crawler().getDetailedAd(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (od != null) {
                loadData(od);
            }
        }
    }
}
