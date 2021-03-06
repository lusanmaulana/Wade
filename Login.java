package com.trois.wade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    public static final String EXTRA_MESG = "com.trois.wade.MESG";
    String username;
    String password;
    DbWade db = new DbWade(this);

    private String JSON_STRING_AKUN;

    public static class User{
        public int id_akun;
        public String username;
        public String password;
        public String email;
        public int id_warga;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sp = getSharedPreferences("com.trois.wade",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String status = sp.getString("status","false");
        int id = sp.getInt("id",0);

        if(status.equals("true")){
            Intent beranda = new Intent(getApplicationContext(),Beranda.class);
            beranda.putExtra(EXTRA_MESG, id);
            startActivity(beranda);
        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        if (networkInfo != null && networkInfo.isConnected()) {
//            syncData();
//        }else{
//            Toast t = Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
//            t.show();
//        }
    }

//    public void syncData(){
//        db.open();
//        db.dropTable("tb_warga");
//        db.dropTable("tb_akun");
//        db.dropTable("tb_ronda");
//        db.close();
//        new conDb().execute("tb_warga","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_warga/appid/5a26b5891f6d043765fdb618");
//        new conDb().execute("tb_akun","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_akun/appid/5a26b5891f6d043765fdb618");
//        new conDb().execute("tb_ronda","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_ronda/appid/5a26b5891f6d043765fdb618");
//    }

    public void btnLoginOnClick(View v){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        password = Login.md5(password);

        if (networkInfo != null && networkInfo.isConnected()) {
            loginProcess();
        }else{
            Toast t = Toast.makeText(getApplicationContext(), "Tidak ada koneksi", Toast.LENGTH_SHORT);
        }

//        if (networkInfo != null && networkInfo.isConnected()) {
//            Log.d("login","online");
//            //new conDb().execute("login","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_akun/appid/5a26b5891f6d043765fdb618");
//        }else{
//            Log.d("login","offline");
//            db.open();
//            ArrayList<DbWade.TbAkun> akun = db.getAllAkun();
//            for(DbWade.TbAkun index : akun){
//                if(username.equals(index.username) && password.equals(index.password)){
//                    status = 1;
//                }
//                Log.d("user",username);
//                Log.d("user",index.username);
//                Log.d("pass",password);
//                Log.d("pass",index.password);
//                Log.d("status",""+status);
//            }
//            if(status == 1){
//                Log.d("login","offlinesucc");
//                Intent beranda = new Intent(getApplicationContext(),Beranda.class);
//                startActivity(beranda);
//            }else{
//                Log.d("login","offlinefail");
//                Toast t = Toast.makeText(getApplicationContext(), "Username atau Password salah.", Toast.LENGTH_SHORT);
//                t.show();
//            }
//            db.close();
//        }
    }

//    private class conDb extends AsyncTask<String, Integer, String[]> {
//
//        protected String[] doInBackground(String... strUrl){
//            String result[] = new String[2];
//            StringBuilder sb = new StringBuilder();
//            InputStream is = null;
//            try {
//                URL url = new URL(strUrl[1]);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                //timeout
//                conn.setReadTimeout(10000 /*milliseconds*/);
//                conn.setConnectTimeout(15000 /*milliseconds*/);
//
//                conn.setRequestMethod("GET");
//                conn.connect();
//
//                try {
//                    is = new BufferedInputStream(conn.getInputStream());
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                    String inputLine;
//                    while ((inputLine = br.readLine()) != null) {
//                        sb.append(inputLine);
//                    }
//                    result[0] = strUrl[0];
//                    result[1] = sb.toString();
//                } catch (Exception e) {
//                    result = null;
//                } finally {
//                    if (is != null) {
//                        try {
//                            is.close();
//                        } catch (IOException e) {
//                            result = null;
//                        }
//                    }
//                }
//            }catch (MalformedURLException e){
//                e.printStackTrace();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        protected void onPostExecute(String[] result){
//            try{
//                JSONArray jArr = new JSONArray(result[1]);
//                for (int i=0; i < jArr.length(); i++) {
//                    JSONObject jObj = jArr.getJSONObject(i);
//                    if(result[0].equals("tb_warga")){
//                        DbWade.TbWarga tb = new DbWade.TbWarga();
//                        tb.id_warga = jObj.getInt("id_warga");
//                        tb.nama = jObj.getString("nama");
//                        tb.lat = jObj.getString("lat");
//                        tb.lon = jObj.getString("lon");
//                        tb.alamat = jObj.getString("alamat");
//                        tb.kontak = jObj.getString("kontak");
//                        tb.status = jObj.getString("status");
//                        tb.wilayah = jObj.getString("wilayah");
//                        tb.foto = jObj.getString("foto");
//                        db.open();
//                        db.insertWarga(tb);
//                        db.close();
//                    }else if(result[0].equals("tb_akun")){
//                        DbWade.TbAkun tb = new DbWade.TbAkun();
//                        tb.id_akun = jObj.getInt("id_akun");
//                        tb.username = jObj.getString("username");
//                        tb.password = jObj.getString("password");
//                        tb.email = jObj.getString("email");
//                        tb.id_warga = jObj.getInt("id_warga");
//                        db.open();
//                        db.insertAkun(tb);
//                        db.close();
//                    }else if(result[0].equals("tb_ronda")){
//                        DbWade.TbRonda tb = new DbWade.TbRonda();
//                        tb.id_ronda = jObj.getInt("id_ronda");
//                        tb.hari = jObj.getString("hari");
//                        tb.id_warga = jObj.getInt("id_warga");
//                        db.open();
//                        db.insertRonda(tb);
//                        db.close();
//                    }else if(result[0].equals("login")){
//                        if(username.equals(jObj.getString("username")) && password.equals(jObj.getString("password"))){
//                            status = 1;
//                        }
//                        Log.d("user",username);
//                        Log.d("user",jObj.getString("username"));
//                        Log.d("pass",password);
//                        Log.d("pass",jObj.getString("password"));
//                        Log.d("status",""+status);
//                    }
//                }
//                if(result[0].equals("login")){
//                    if(status == 1){
//                        Log.d("login","onlinesucc");
//                        Intent beranda = new Intent(getApplicationContext(),Beranda.class);
//                        startActivity(beranda);
//                    }else{
//                        Log.d("login","onlinefail");
//                        Toast t = Toast.makeText(getApplicationContext(), "Username atau Password salah.", Toast.LENGTH_SHORT);
//                        t.show();
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }

    public void daftarOnClick(View v){
        Intent daftar = new Intent(this, Daftar.class);
        startActivity(daftar);
    }

    public void loginProcess(){
        final int[] id = new int[1];
        final String[] uname = new String[1];
        final String[] pass = new String[1];
        final int[] status = {0};

        class LoginProcess extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                db.open();
                db.dropTable("tb_warga");
                db.close();

                loading = ProgressDialog.show(Login.this,"Masuk...","Mohon tunggu...",false,false);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DbWade.URL_AKUN_GET_ALL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                JSON_STRING_AKUN = s;
                JSONObject jsonObject = null;
                SharedPreferences sp = getSharedPreferences("com.trois.wade",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();

                try {
                    jsonObject = new JSONObject(JSON_STRING_AKUN);
                    JSONArray result = jsonObject.getJSONArray(DbWade.TAG_JSON_ARRAY);

                    Log.i("Input U", username);
                    Log.i("Input P", password);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);

                        id[0] = jo.getInt("id_akun");
                        uname[0] = jo.getString("username");
                        pass[0] = jo.getString("password");

                        Log.i("DB U", ""+uname[0]);
                        Log.i("DB P", ""+pass[0]);

                        if(uname[0].equals(username) && pass[0].equals(password)){
                            status[0] = 1;
                        }
                    }

                    Log.i("Status", ""+status[0]);

                    loading.dismiss();

                    if(status[0] == 1){
                        ed.putString("status","true");
                        ed.putInt("id",id[0]);
                        ed.commit();
                        Intent beranda = new Intent(getApplicationContext(),Beranda.class);
                        beranda.putExtra(EXTRA_MESG,id[0]);
                        startActivity(beranda);
                    }else{
                        Toast t = Toast.makeText(getApplicationContext(), "Username atau Password salah!", Toast.LENGTH_SHORT);
                        t.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LoginProcess login = new LoginProcess();
        login.execute();
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
