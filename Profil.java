package com.trois.wade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class Profil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView tvNama = findViewById(R.id.tvNama);
    TextView tvAlamat = findViewById(R.id.tvAlamat);
    TextView tvKontak = findViewById(R.id.tvKontak);
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Intent intent2 = getIntent();
        id = intent2.getIntExtra(Login.EXTRA_MESG,0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.tbProfil);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_profil);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            Intent ronda = new Intent(this, Ronda.class);
            ronda.putExtra(Login.EXTRA_MESG, id);
            startActivity(ronda);
        } else if (id == R.id.nav_profil) {

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
}
