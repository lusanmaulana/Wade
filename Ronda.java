package com.trois.wade;

import android.content.Intent;
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
import android.widget.Toast;

public class Ronda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ronda);

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

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_warga) {
            Intent beranda = new Intent(this, Beranda.class);
            startActivity(beranda);
        } else if (id == R.id.nav_lokasi) {
            Intent lokasi = new Intent(this, Lokasi.class);
            startActivity(lokasi);
        } else if (id == R.id.nav_ronda) {

        } else if (id == R.id.nav_profil) {

        } else if (id == R.id.nav_logout) {
            Intent logout = new Intent(this, Login.class);
            startActivity(logout);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//                tvHari.setText("Hari: Senin");
                return true;
            case R.id.ronda_selasa:
                Toast.makeText(getApplicationContext(), "Selasa", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Selasa");
                return true;
            case R.id.ronda_rabu:
                Toast.makeText(getApplicationContext(), "Rabu", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Rabu");
                return true;
            case R.id.ronda_kamis:
                Toast.makeText(getApplicationContext(), "Kamis", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Kamis");
                return true;
            case R.id.ronda_jumat:
                Toast.makeText(getApplicationContext(), "Jumat", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Jumat");
                return true;
            case R.id.ronda_sabtu:
                Toast.makeText(getApplicationContext(), "Sabtu", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Sabtu");
                return true;
            case R.id.ronda_minggu:
                Toast.makeText(getApplicationContext(), "Minggu", Toast.LENGTH_SHORT).show();
//                tvHari.setText("Hari: Minggu");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
