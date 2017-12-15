package com.trois.wade;

import android.content.Context;
import android.content.Intent;
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
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class Daftar extends AppCompatActivity {

    DbWade.TbAkun tbAkun;
    DbWade.TbWarga tbWarga;
    DbWade.TbRonda tbRonda;
    DbWade db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        db = new DbWade(getApplicationContext());
        tbAkun = new DbWade.TbAkun();
        tbWarga = new DbWade.TbWarga();
        tbRonda = new DbWade.TbRonda();
    }

    public void btnDaftarOnClick(View v){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            db.open();
            db.dropTable("tb_warga");
            db.dropTable("tb_akun");
            db.dropTable("tb_ronda");
            db.close();
            new countData().execute("tb_warga","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_warga/appid/5a26b5891f6d043765fdb618");
            new countData().execute("tb_akun","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_akun/appid/5a26b5891f6d043765fdb618");
            new countData().execute("tb_ronda","http://io.nowdb.net/v2/select_all/token/57e78d451f6d04287da14467/project/db_wade/collection/tb_ronda/appid/5a26b5891f6d043765fdb618");
            Intent login = new Intent(this, Login.class);
            startActivity(login);
            finish();
            Toast t = Toast.makeText(getApplicationContext(), "Anda berhasil mendaftar", Toast.LENGTH_SHORT);
            t.show();
        } else {
            Toast t = Toast.makeText( getApplicationContext(), "Tidak ada koneksi!",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private class countData extends AsyncTask<String, Integer, String[]> {

        protected String[] doInBackground(String... strUrl){
            String[] result = new String[2];
            StringBuilder sb = new StringBuilder();
            InputStream is = null;
            try {
                URL url = new URL(strUrl[1]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /*milliseconds*/);
                conn.setRequestMethod("GET");
                conn.connect();
                try {
                    is = new BufferedInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    result[0] = strUrl[0];
                    result[1] = sb.toString();
                } catch (Exception e) {
                    result = null;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String[] result){
            try{
                JSONArray jArr = new JSONArray(result[1]);
                for (int i=0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.getJSONObject(i);
                    if (result[0].equals("tb_warga")) {
                        tbWarga.id_warga = jObj.getInt("id_warga");
                        tbWarga.nama = jObj.getString("nama");
                        tbWarga.lat = jObj.getString("lat");
                        tbWarga.lon = jObj.getString("lon");
                        tbWarga.alamat = jObj.getString("alamat");
                        tbWarga.kontak = jObj.getString("kontak");
                        tbWarga.wilayah = jObj.getString("wilayah");
                        tbWarga.status = jObj.getString("status");
                        tbWarga.foto = jObj.getString("foto");
                        db.open();
                        db.insertWarga(tbWarga);
                        db.close();
                    } else if (result[0].equals("tb_akun")) {
                        tbAkun.id_akun = jObj.getInt("id_akun");
                        tbAkun.username = jObj.getString("username");
                        tbAkun.password = jObj.getString("password");
                        tbAkun.email = jObj.getString("email");
                        tbAkun.id_warga = jObj.getInt("id_warga");
                        db.open();
                        db.insertAkun(tbAkun);
                        db.close();
                    } else if (result[0].equals("tb_ronda")) {
                        tbRonda.id_ronda = jObj.getInt("id_ronda");
                        tbRonda.hari = jObj.getString("hari");
                        tbRonda.id_warga = jObj.getInt("id_warga");
                        db.open();
                        db.insertRonda(tbRonda);
                        db.close();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            updateData(result[0]);
        }
    }

    private void updateData(String tb){

        String[] param;
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etNama = findViewById(R.id.etNama);
        EditText etAlamat = findViewById(R.id.etAlamat);
        EditText etKontak = findViewById(R.id.etKontak);

        if(tb.equals("tb_warga")){
            db.open();
            tbWarga.id_warga = db.getWargaLastId() + 1;
            Log.d("idwarga",""+tbWarga.id_warga);
            tbWarga.nama = etNama.getText().toString();
            tbWarga.lat = "-6.864396";
            tbWarga.lon = "107.590914";
            tbWarga.alamat = etAlamat.getText().toString();
            tbWarga.kontak = etKontak.getText().toString();
            tbWarga.wilayah = "0";
            tbWarga.status = "0";
            tbWarga.foto = "default.jpg";
            db.insertWarga(tbWarga);
            db.close();
            param = new String[10];
            param[0] = "tb_warga";
            param[1] = ""+tbWarga.id_warga;
            param[2] = tbWarga.nama;
            param[3] = tbWarga.lat;
            param[4] = tbWarga.lon;
            param[5] = tbWarga.alamat;
            param[6] = tbWarga.kontak;
            param[7] = tbWarga.wilayah;
            param[8] = tbWarga.status;
            param[9] = tbWarga.foto;
            new postData().execute(param);
        }else if(tb.equals("tb_akun")){
            db.open();
            tbAkun.id_akun = db.getAkunLastId() + 1;
            Log.d("idakun",""+tbAkun.id_akun);
            tbAkun.username = etUsername.getText().toString();
            tbAkun.password = etPassword.getText().toString();
            tbAkun.password = Login.md5(tbAkun.password);
            tbAkun.email = etEmail.getText().toString();
            tbAkun.id_warga = db.getWargaLastId();
            db.insertAkun(tbAkun);
            db.close();
            param = new String[6];
            param[0] = "tb_akun";
            param[1] = ""+tbAkun.id_akun;
            param[2] = tbAkun.username;
            param[3] = tbAkun.password;
            param[4] = tbAkun.email;
            param[5] = ""+tbAkun.id_warga;
            new postData().execute(param);
        }else if(tb.equals("tb_ronda")){
            db.open();
            tbRonda.id_ronda = db.getRondaLastId()+1;
            Log.d("idronda",""+tbRonda.id_ronda);
            tbRonda.id_warga = db.getWargaLastId();
            if(tbRonda.id_warga % 7 == 1){
                tbRonda.hari = "0";
                db.insertRonda(tbRonda);
            }else if(tbRonda.id_warga % 7 == 2){
                tbRonda.hari = "1";
                db.insertRonda(tbRonda);
            }else if(tbRonda.id_warga % 7 == 3){
                tbRonda.hari = "2";
                db.insertRonda(tbRonda);
            }else if(tbRonda.id_warga % 7 == 4){
                tbRonda.hari = "3";
                db.insertRonda(tbRonda);
            }else if(tbRonda.id_warga % 7 == 5){
                tbRonda.hari = "4";
                db.insertRonda(tbRonda);
            }else if(tbRonda.id_warga % 7 == 6){
                tbRonda.hari = "5";
                db.insertRonda(tbRonda);
            }else {
                tbRonda.hari = "6";
                db.insertRonda(tbRonda);
            }
            db.close();
            param = new String[4];
            param[0] = "tb_ronda";
            param[1] = ""+tbRonda.id_ronda;
            param[2] = ""+tbRonda.hari;
            param[3] = ""+tbRonda.id_warga;
            new postData().execute(param);
        }
    }

    private class postData extends AsyncTask<String, Integer, String[]> {

        protected String[] doInBackground(String... strUrl) {
            String param = "";
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            if (strUrl[0] == "tb_warga") {
                param = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"token\"\r\n\r\n57e78d451f6d04287da14467\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"project\"\r\n\r\ndb_wade\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"collection\"\r\n\r\ntb_warga\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"appid\"\r\n\r\n5a26b5891f6d043765fdb618\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"id_warga\"\r\n\r\n" + strUrl[1] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"nama\"\r\n\r\n" + strUrl[2] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"lat\"\r\n\r\n" + strUrl[3] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"lon\"\r\n\r\n" + strUrl[4] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"alamat\"\r\n\r\n" + strUrl[5] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"kontak\"\r\n\r\n" + strUrl[6] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"status\"\r\n\r\n" + strUrl[7] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"wilayah\"\r\n\r\n" + strUrl[8] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"foto\"\r\n\r\n" + strUrl[9] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
            } else if (strUrl[0] == "tb_akun") {
                param = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"token\"\r\n\r\n57e78d451f6d04287da14467\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"project\"\r\n\r\ndb_wade\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"collection\"\r\n\r\ntb_akun\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"appid\"\r\n\r\n5a26b5891f6d043765fdb618\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"id_akun\"\r\n\r\n" + strUrl[1] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"username\"\r\n\r\n" + strUrl[2] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"password\"\r\n\r\n" + strUrl[3] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"email\"\r\n\r\n" + strUrl[4] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"id_warga\"\r\n\r\n" + strUrl[5] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
            } else if (strUrl[0] == "tb_ronda") {
                param = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"token\"\r\n\r\n57e78d451f6d04287da14467\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"project\"\r\n\r\ndb_wade\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"collection\"\r\n\r\ntb_ronda\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"appid\"\r\n\r\n5a26b5891f6d043765fdb618\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"id_ronda\"\r\n\r\n" + strUrl[1] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"hari\"\r\n\r\n" + strUrl[2] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                        "name=\"id_warga\"\r\n\r\n" + strUrl[3] + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
            }
            RequestBody body = RequestBody.create(mediaType,param);
            Request request = new Request.Builder()
                    .url("http://io.nowdb.net/v2/insert")
                    .post(body)
                    .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "e59bfd09-8340-5214-9869-b119bcc416b4")
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String[] result) {

        }
    }
}
