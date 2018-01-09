package com.trois.wade;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Lokasi extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    int id;

    boolean camStatus = false;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static int MY_PERMISSION_REQUEST = 99;
    private Marker mPosSekarang;
    private LatLng rumahWarga;

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission diberikan, mulai ambil lokasi
                buildGoogleApiClient();
            } else {
                //ijin tidak diberikan, tampilkan pesan
                android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(this).create();
                ad.setMessage("Tidak mendapat ijin");
                ad.show();
            }
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);

        Intent intent2 = getIntent();
        id = intent2.getIntExtra(Login.EXTRA_MESG,0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.tbLokasi);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_lokasi);
        navigationView.setNavigationItemSelectedListener(this);

        buildGoogleApiClient();
        createLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng posSekarang = new LatLng(-34, 151);
        mPosSekarang = mMap.addMarker(new MarkerOptions().position(posSekarang)
                .title("Posisi sekarang.")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current)));

        DbWade db = new DbWade(getApplicationContext());
        db.open();

        Double latPos;
        Double lonPos;

        ArrayList<DbWade.TbWarga> warga = db.getAllWarga();
        for(DbWade.TbWarga index : warga){
            latPos = index.lat;
            lonPos = index.lon;
            rumahWarga = new LatLng(latPos, lonPos);

            mMap.addMarker(new MarkerOptions().position(rumahWarga)
                    .title(index.nama)
                    .snippet(index.kontak)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.home_marker)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posSekarang,18));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng posSekarang = new LatLng(location.getLatitude(),location.getLongitude());
        mPosSekarang.setPosition(posSekarang);
        if(!camStatus){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posSekarang,18));
            camStatus = !camStatus;
        }
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

        } else if (id == R.id.nav_ronda) {
            Intent lokasi = new Intent(this, Ronda.class);
            lokasi.putExtra(Login.EXTRA_MESG, id);
            startActivity(lokasi);
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
}
