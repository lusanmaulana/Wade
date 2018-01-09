package com.trois.wade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Beranda extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String JSON_STRING_WARGA;
    int id;

    DbWade db = new DbWade(this);

    private ArrayList<String> itemsWarga = new ArrayList<>();
    ListView daftarWarga;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        Intent intent2 = getIntent();
        id = intent2.getIntExtra(Login.EXTRA_MESG,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_warga);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            syncWarga();
        }else{
            Toast.makeText( getApplicationContext(), "Tidak ada koneksi. Gagal memperbaharui data",Toast.LENGTH_SHORT);
            showAllWarga();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beranda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            syncWarga();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences("com.trois.wade",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_warga) {

        } else if (id == R.id.nav_lokasi) {
            Intent lokasi = new Intent(this, Lokasi.class);
            lokasi.putExtra(Login.EXTRA_MESG, id);
            startActivity(lokasi);
        } else if (id == R.id.nav_ronda) {
            Intent ronda = new Intent(this, Ronda.class);
            ronda.putExtra(Login.EXTRA_MESG, id);
            startActivity(ronda);
        } else if (id == R.id.nav_profil) {
            Intent profil = new Intent(this, Profil.class);
            profil.putExtra(Login.EXTRA_MESG, id);
            startActivity(profil);
        } else if (id == R.id.nav_logout) {
            ed.putString("status","false");
            ed.putInt("id",0);
            ed.commit();
            Intent logout = new Intent(this, Login.class);
            startActivity(logout);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void syncWarga(){
        class SyncWarga extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                db.open();
                db.dropTable("tb_warga");
                db.close();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                JSON_STRING_WARGA = s;
                JSONObject jsonObject = null;

                Log.i("JSON Result",JSON_STRING_WARGA);
                try {
                    jsonObject = new JSONObject(JSON_STRING_WARGA);
                    JSONArray result = jsonObject.getJSONArray(DbWade.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);

                        DbWade.TbWarga tb = new DbWade.TbWarga();
                        tb.id_warga = jo.getInt("id_warga");
                        tb.nama = jo.getString("nama");
                        tb.lat = Double.parseDouble(jo.getString("lat"));
                        tb.lon = Double.parseDouble(jo.getString("lon"));
                        tb.alamat = jo.getString("alamat");
                        tb.kontak = jo.getString("kontak");
                        tb.status = jo.getString("status");
                        tb.wilayah = jo.getString("wilayah");
                        tb.foto = jo.getString("foto");

                        db.open();
                        db.insertWarga(tb);
                        db.close();

                        showAllWarga();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DbWade.URL_WARGA_GET_ALL);
                return s;
            }
        }

        SyncWarga sw = new SyncWarga();
        sw.execute();
    }

    private void showAllWarga(){
        db.open();

        itemsWarga.clear();

        ArrayList<DbWade.TbWarga> warga = db.getAllWarga();
        for(DbWade.TbWarga index : warga){
            itemsWarga.add(index.nama);
        }

        daftarWarga = (ListView) findViewById(R.id.lvWarga);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, itemsWarga);
        daftarWarga.setAdapter(adapter);

        db.close();
    }
}
