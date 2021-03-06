package com.toshevski.android.reklama5.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.toshevski.android.reklama5.R;
import com.toshevski.android.reklama5.adapters.OglasOsnovnoAdapter;
import com.toshevski.android.reklama5.database.Crawler;
import com.toshevski.android.reklama5.database.DBManager;
import com.toshevski.android.reklama5.listeners.EndlessRecyclerViewScrollListener;
import com.toshevski.android.reklama5.listeners.RecyclerViewClickListener;
import com.toshevski.android.reklama5.pojos.OglasOsnovno;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<OglasOsnovno> oglasi = new ArrayList<>();
    private OglasOsnovnoAdapter oglasiAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private String lastURL;
    private SwipeRefreshLayout srl;
    private int contextMenuPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Log.i("FAB", "Gol: " + oglasi.size());
                    oglasiAdapter.notifyDataSetChanged();
                    new GetDetailedAd().execute("http://m.reklama5.mk/AdDetails?ad=1808004");
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    View inputView = LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.prebaraj_dialog, null);
                    AlertDialog.Builder inputBuilder = new AlertDialog.Builder(MainActivity.this);
                    inputBuilder.setView(inputView);
                    final EditText et = (EditText) inputView.findViewById(R.id.vlez);
                    final Spinner sp = (Spinner) inputView.findViewById(R.id.grad);

                    inputBuilder
                            .setCancelable(true)
                            .setPositiveButton("Пребарај", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("ET", ": " + et.getText());
                                    Log.i("SP", ": " + sp.getSelectedItemPosition());
                                    Log.i("Klik", "DA");
                                    String text = et.getText().toString();
                                    int idx = sp.getSelectedItemPosition();
                                    if (idx > 26) ++idx;
                                    if (idx == 0)
                                        new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?q=" +
                                                text + "&city=");
                                    else if (idx == 31) {
                                        new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?q=" + text + "&city=34");
                                    } else {
                                        new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?q=" + text + "&city=" + idx);
                                    }
                                }
                            })
                            .setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog prashalnik = inputBuilder.create();
                    prashalnik.show();

                }
            });
        }

        srl = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        if (srl != null) {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetAllAdsAsync().execute(lastURL);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        oglasiAdapter = new OglasOsnovnoAdapter(this, oglasi);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        assert recycler_view != null;
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (lastURL.equals("favorites")) return;
                new AddDataAsync().execute(lastURL + "&page=" + (page + 1));
            }
        });
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(oglasiAdapter);

        recycler_view.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(),
                recycler_view,new RecyclerViewClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("url", oglasiAdapter.getAd(position).getUrl());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        contextMenuPosition = position;
                        openContextMenu(view);
                    }
                })
        );

        registerForContextMenu(recycler_view);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return lastURL.equals("favorites");
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int pos = viewHolder.getAdapterPosition();
                DBManager dbm = new DBManager(getApplicationContext());
                if (lastURL.equals("favorites")) {
                    OglasOsnovno o = oglasiAdapter.getAd(pos);
                    dbm.delAd(o);
                }
                oglasiAdapter.removeAd(pos);
                oglasiAdapter.notifyItemRemoved(pos);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view);

        progressDialog = ProgressDialog.show(MainActivity.this, "Ве молиме почекајте...",
                "Симнување", true);
        new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_favorites:
                new DBManager(getApplicationContext())
                        .AddAd(oglasiAdapter.getAd(contextMenuPosition));
                break;
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (lastURL.equals("favorites")) return;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_options, menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (lastURL.equals("http://m.reklama5.mk/Search?")) {
            super.onBackPressed();
        } else {
            new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("onOptionsItemSelected", "action_settings");
            DBManager dbm = new DBManager(getApplicationContext());
            dbm.AddAd(oglasiAdapter.getAd(0));

            ArrayList<OglasOsnovno> oo = dbm.GetAllAds();
            Log.i("Od DB", oo.get(0).toString());
            return true;
        } else if (id == R.id.secondActivity) {
            Intent intent = new Intent(this, DetailsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int idx = id - R.id.omileni - 1;
        if (idx > 26) ++idx;
        if (idx == -1) {
            lastURL = "favorites";
            srl.setRefreshing(true);
            DBManager dbm = new DBManager(getApplicationContext());
            oglasiAdapter.setList(dbm.GetAllAds());
            oglasiAdapter.notifyDataSetChanged();
            mLayoutManager.scrollToPosition(0);
            srl.setRefreshing(false);
        } else if (idx == 0)
            new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?");
        else if (idx == 31) {
            new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?city=34?");
        } else {
            new GetAllAdsAsync().execute("http://m.reklama5.mk/Search?city=" + idx);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
/*
    class GetCitiesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Crawler c = new Crawler();
            try {
                Log.i("MM", new Crawler().getCities().get(0).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
*/
    class GetAllAdsAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            srl.setRefreshing(true);
        }

        public ArrayList<OglasOsnovno> oo;
        @Override
        protected Void doInBackground(String... params) {
            try {
                lastURL = params[0];
                oo = new Crawler().getAllAds(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (oo != null)
                oglasiAdapter.setList(oo);
            else oglasiAdapter.setList(new ArrayList<OglasOsnovno>());
            oglasiAdapter.notifyDataSetChanged();
            mLayoutManager.scrollToPosition(0);
            progressDialog.dismiss();
            srl.setRefreshing(false);
        }
    }

    class AddDataAsync extends AsyncTask<String, Void, Void> {

        ArrayList<OglasOsnovno> oo;
        @Override
        protected Void doInBackground(String... params) {
            try {
                oo = new Crawler().getAllAds(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            oglasiAdapter.addItems(oo);
            oglasiAdapter.notifyDataSetChanged();
        }
    }
}