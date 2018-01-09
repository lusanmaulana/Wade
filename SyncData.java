package com.trois.wade;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Inquery on 07/01/2018.
 */

public class SyncData extends AppCompatActivity{

    private String JSON_STRING_WARGA;
    private String JSON_STRING_RONDA;

    DbWade db = new DbWade(this);

    public void truncateTable(){
        db.open();
        db.dropTable("tb_warga");
        db.dropTable("tb_ronda");
        db.close();
    }

    public void getJSONWarga(){
        class GetJSONWarga extends AsyncTask<Void,Void,String>{

//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(SyncData.this,"Mengambil Data","Mohon Tunggu...",false,false);
//            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                JSON_STRING_WARGA = s;
                JSONObject jsonObject = null;

                Log.i("JSON Result",JSON_STRING_WARGA);
//                try {
//                    jsonObject = new JSONObject(JSON_STRING_WARGA);
//                    JSONArray result = jsonObject.getJSONArray(DbWade.TAG_JSON_ARRAY);
//
//                    for(int i = 0; i<result.length(); i++){
//                        JSONObject jo = result.getJSONObject(i);
//
//                        DbWade.TbWarga tb = new DbWade.TbWarga();
//                        tb.id_warga = jo.getInt("id_warga");
//                        tb.nama = jo.getString("nama");
//                        tb.lat = Double.parseDouble(jo.getString("lat"));
//                        tb.lon = Double.parseDouble(jo.getString("lon"));
//                        tb.alamat = jo.getString("alamat");
//                        tb.kontak = jo.getString("kontak");
//                        tb.status = jo.getString("status");
//                        tb.wilayah = jo.getString("wilayah");
//                        tb.foto = jo.getString("foto");
//
//                        db.open();
//                        db.insertWarga(tb);
//                        db.close();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DbWade.URL_WARGA_GET_ALL);
                return s;
            }
        }

        GetJSONWarga gj = new GetJSONWarga();
        gj.execute();
    }
}
