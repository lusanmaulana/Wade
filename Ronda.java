package com.trois.wade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ronda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DbWade db = new DbWade(this);

    private String JSON_STRING_RONDA;
    int id;


    private ArrayList<String> itemsRonda = new ArrayList<>();
    ListView daftarRonda;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ronda);

        Intent intent2 = getIntent();
        id = intent2.getIntExtra(Login.EXTRA_MESG,0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.tbRonda);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_ronda);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            syncRonda();
        }else{
            Toast.makeText( getApplicationContext(), "Tidak ada koneksi. Gagal memperbaharui data",Toast.LENGTH_SHORT);
            showAllRonda("1");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences("com.trois.wade",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_warga) {
            Intent beranda = new Intent(this, Beranda.class);
            beranda.putExtra(Login.EXTRA_MESG, id);
            startActivity(beranda);
        } else if (id == R.id.nav_lokasi) {
            Intent lokasi = new Intent(this, Lokasi.class);
            lokasi.putExtra(Login.EXTRA_MESG, id);
            startActivity(lokasi);
        } else if (id == R.id.nav_ronda) {

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ronda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ronda_senin:
                Toast.makeText(getApplicationContext(), "Senin", Toast.LENGTH_SHORT).show();
                showAllRonda("1");
                return true;
            case R.id.ronda_selasa:
                Toast.makeText(getApplicationContext(), "Selasa", Toast.LENGTH_SHORT).show();
                showAllRonda("2");
                return true;
            case R.id.ronda_rabu:
                Toast.makeText(getApplicationContext(), "Rabu", Toast.LENGTH_SHORT).show();
                showAllRonda("3");
                return true;
            case R.id.ronda_kamis:
                Toast.makeText(getApplicationContext(), "Kamis", Toast.LENGTH_SHORT).show();
                showAllRonda("4");
                return true;
            case R.id.ronda_jumat:
                Toast.makeText(getApplicationContext(), "Jumat", Toast.LENGTH_SHORT).show();
                showAllRonda("5");
                return true;
            case R.id.ronda_sabtu:
                Toast.makeText(getApplicationContext(), "Sabtu", Toast.LENGTH_SHORT).show();
                showAllRonda("6");
                return true;
            case R.id.ronda_minggu:
                Toast.makeText(getApplicationContext(), "Minggu", Toast.LENGTH_SHORT).show();
                showAllRonda("7");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void syncRonda(){
        class SyncRonda extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                db.open();
                db.dropTable("tb_ronda");
                db.close();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING_RONDA = s;
                JSONObject jsonObject = null;

                Log.i("JSON Result",JSON_STRING_RONDA);
                try {
                    jsonObject = new JSONObject(JSON_STRING_RONDA);
                    JSONArray result = jsonObject.getJSONArray(DbWade.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);

                        DbWade.TbRonda tb = new DbWade.TbRonda();
                        tb.id_ronda = jo.getInt("id_ronda");
                        tb.hari = jo.getString("hari");
                        tb.id_warga = jo.getInt("id_warga");

                        db.open();
                        db.insertRonda(tb);
                        db.close();
                    }

                    showAllRonda("1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DbWade.URL_RONDA_GET_ALL);
                return s;
            }
        }

        SyncRonda sr = new SyncRonda();
        sr.execute();
    }

    private void showAllRonda(String hari){
        db.open();

        itemsRonda.clear();

        int idWarga;
        String show;
        ArrayList<DbWade.TbRonda> ronda = db.getRondaByHari(hari);
        DbWade.TbWarga warga;
        for(DbWade.TbRonda index : ronda){
            warga = db.getWargaById(index.id_warga);
            Log.i("Warga Ronda",warga.nama);
            itemsRonda.add(""+warga.nama);
        }

        daftarRonda = (ListView) findViewById(R.id.lvRonda);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, itemsRonda);
        daftarRonda.setAdapter(adapter);

        db.close();
    }
}
