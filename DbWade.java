package com.trois.wade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by A450CC on 12/4/2017.
 */

public class DbWade {

    //URL WEBSERVICES
    //TB_AKUN
    public static final String URL_AKUN_ADD = "http://inquery.hostingmerahputih.id/Wade/akunAdd.php";
    public static final String URL_AKUN_GET = "http://inquery.hostingmerahputih.id/Wade/akunGet.php?id_akun=";
    public static final String URL_AKUN_GET_ALL = "http://inquery.hostingmerahputih.id/Wade/akunGetAll.php";
    public static final String URL_AKUN_UPDATE = "http://inquery.hostingmerahputih.id/Wade/akunUpdate.php";
    public static final String URL_AKUN_DELETE = "http://inquery.hostingmerahputih.id/Wade/akunDelete.php?id_akun=";
    public static final String URL_AKUN_CP = "http://inquery.hostingmerahputih.id/Wade/akunDelete.php?id=";
    public static final String URL_AKUN_LOGIN = "http://inquery.hostingmerahputih.id/Wade/akunLogin.php";

    //TB_WARGA
    public static final String URL_WARGA_ADD = "http://inquery.hostingmerahputih.id/Wade/wargaAdd.php";
    public static final String URL_WARGA_GET = "http://inquery.hostingmerahputih.id/Wade/wargaGet.php?id_warga=";
    public static final String URL_WARGA_GET_ALL = "http://inquery.hostingmerahputih.id/Wade/wargaGetAll.php";
    public static final String URL_WARGA_UPDATE = "http://inquery.hostingmerahputih.id/Wade/wargaUpdate.php";
    public static final String URL_WARGA_DELETE = "http://inquery.hostingmerahputih.id/Wade/wargaDelete.php?id_warga=";
    public static final String URL_WARGA_GET_LAST_ID = "http://inquery.hostingmerahputih.id/Wade/wargaGetLastID.php";

    //TB_RONDA
    public static final String URL_RONDA_ADD = "http://inquery.hostingmerahputih.id/Wade/rondaAdd.php";
    public static final String URL_RONDA_GET = "http://inquery.hostingmerahputih.id/Wade/rondaGet.php?id_ronda=";
    public static final String URL_RONDA_GET_ALL = "http://inquery.hostingmerahputih.id/Wade/rondaGetAll.php";
    public static final String URL_RONDA_UPDATE = "http://inquery.hostingmerahputih.id/Wade/rondaUpdate.php";
    public static final String URL_RONDA_DELETE = "http://inquery.hostingmerahputih.id/Wade/rondaDelete.php?id_ronda=";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    //TB_AKUN
    public static final String KEY_AKUN_ID = "id_akun";
    public static final String KEY_AKUN_USERNAME = "username";
    public static final String KEY_AKUN_PASSWORD = "password";
    public static final String KEY_AKUN_EMAIL = "email";
    public static final String KEY_AKUN_ID_WARGA = "id_warga";

    //TB_WARGA
    public static final String KEY_WARGA_ID = "id_warga";
    public static final String KEY_WARGA_NAMA = "nama";
    public static final String KEY_WARGA_LAT = "lat";
    public static final String KEY_WARGA_LON = "lon";
    public static final String KEY_WARGA_ALAMAT = "alamat";
    public static final String KEY_WARGA_KONTAK = "kontak";
    public static final String KEY_WARGA_STATUS = "status";
    public static final String KEY_WARGA_WILAYAH = "wilayah";
    public static final String KEY_WARGA_FOTO = "foto";

    //TB_RONDA
    public static final String KEY_RONDA_ID = "id_ronda";
    public static final String KEY_RONDA_HARI = "hari";
    public static final String KEY_RONDA_ID_WARGA = "id_warga";

    //JSON Tags
    public static final String TAG_JSON_ARRAY = "result";

    //TB_AKUN
    public static final String TAG_AKUN_ID = "id_akun";
    public static final String TAG_AKUN_USERNAME = "username";
    public static final String TAG_AKUN_PASSWORD = "password";
    public static final String TAG_AKUN_EMAIL = "email";
    public static final String TAG_AKUN_ID_WARGA = "id_warga";

    //TB_WARGA
    public static final String TAG_WARGA_ID = "id_warga";
    public static final String TAG_WARGA_NAMA = "nama";
    public static final String TAG_WARGA_LAT = "lat";
    public static final String TAG_WARGA_LON = "lon";
    public static final String TAG_WARGA_ALAMAT = "alamat";
    public static final String TAG_WARGA_KONTAK = "kontak";
    public static final String TAG_WARGA_STATUS = "status";
    public static final String TAG_WARGA_WILAYAH = "wilayah";
    public static final String TAG_WARGA_FOTO = "foto";

    //TB_RONDA
    public static final String TAG_RONDA_ID = "id_ronda";
    public static final String TAG_RONDA_HARI = "hari";
    public static final String TAG_RONDA_ID_WARGA = "id_warga";

    //ID
    public static final String AKUN_ID = "id_akun";
    public static final String WARGA_ID = "id_warga";
    public static final String RONDA_ID = "id_ronda";

    public static class TbAkun{
        public int id_akun;
        public String username;
        public String password;
        public String email;
        public int id_warga;
    }

    public static class TbWarga{
        public int id_warga;
        public String nama;
        public Double lat;
        public Double lon;
        public String alamat;
        public String kontak;
        public String status;
        public String wilayah;
        public String foto;
    }

    public static class TbRonda{
        public int id_ronda;
        public String hari;
        public int id_warga;
    }

    public static class TbChat{
        public int id_chat;
        public String wilayah;
        public String pengirim;
        public String pesan;
    }

    public static class TbSurat{
        public int id_surat;
        public String penerima;
        public String pengirim;
        public String pesan;
        public String tanggal;
    }

    private SQLiteDatabase db;
    private  final OpenHelper dbHelper;

    public DbWade(Context c){
        dbHelper = new OpenHelper(c);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }
    public void dropTable(String tb){
        db.execSQL("DELETE FROM "+tb);
    }

    public long insertAkun(DbWade.TbAkun tbAkun){
        ContentValues newVal = new ContentValues();
        newVal.put("id_akun",tbAkun.id_akun);
        newVal.put("username",tbAkun.username);
        newVal.put("password",tbAkun.password);
        newVal.put("email",tbAkun.email);
        newVal.put("id_warga",tbAkun.id_warga);
        Log.d("insert","akunin");
        return db.insert("tb_akun",null,newVal);
    }

    public long insertWarga(DbWade.TbWarga tbWarga){
        ContentValues newVal = new ContentValues();

        newVal.put("id_warga",tbWarga.id_warga);
        newVal.put("nama",tbWarga.nama);
        newVal.put("lat",tbWarga.lat);
        newVal.put("lon",tbWarga.lon);
        newVal.put("alamat",tbWarga.alamat);
        newVal.put("kontak",tbWarga.kontak);
        newVal.put("status",tbWarga.status);
        newVal.put("wilayah",tbWarga.wilayah);
        newVal.put("foto",tbWarga.foto);

        Log.d("insert","wargain");

        return db.insert("tb_warga",null,newVal);
    }

    public long insertRonda(DbWade.TbRonda tbRonda){
        ContentValues newVal = new ContentValues();
        newVal.put("id_ronda",tbRonda.id_ronda);
        newVal.put("hari",tbRonda.hari);
        newVal.put("id_warga",tbRonda.id_warga);
        Log.d("insert","rondain");
        return db.insert("tb_ronda",null,newVal);
    }

    public long insertChat(int id_chat, String wilayah, String pengirim, String pesan){
        ContentValues newVal = new ContentValues();
        newVal.put("id_chat",id_chat);
        newVal.put("wilayah",wilayah);
        newVal.put("pengirim",pengirim);
        newVal.put("pesan",pesan);
        return db.insert("tb_chat",null,newVal);
    }

    public long insertSurat(int id_surat, String penerima, String pengirim, String tanggal, String pesan){
        ContentValues newVal = new ContentValues();
        newVal.put("id_surat",id_surat);
        newVal.put("penerima",penerima);
        newVal.put("pengirim",pengirim);
        newVal.put("tanggal",tanggal);
        newVal.put("pesan",pesan);
        return db.insert("tb_surat",null,newVal);
    }

    public ArrayList<TbAkun> getAllAkun(){
        Cursor cur = null;
        ArrayList<TbAkun> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_akun",null);
        if(cur.moveToFirst()){
            do{
                TbAkun tb = new TbAkun();
                tb.id_akun = cur.getInt(0);
                tb.username = cur.getString(1);
                tb.password = cur.getString(2);
                tb.email = cur.getString(3);
                tb.id_warga = cur.getInt(4);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public ArrayList<TbWarga> getAllWarga(){
        Cursor cur = null;
        ArrayList<TbWarga> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_warga",null);
        if(cur.moveToFirst()){
            do{
                TbWarga tb = new TbWarga();
                tb.id_warga = cur.getInt(0);
                tb.nama = cur.getString(1);
                tb.lat = Double.parseDouble(cur.getString(2));
                tb.lon = Double.parseDouble(cur.getString(3));
                tb.alamat = cur.getString(4);
                tb.kontak = cur.getString(5);
                tb.status = cur.getString(6);
                tb.wilayah = cur.getString(7);
                tb.foto = cur.getString(8);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public ArrayList<TbRonda> getAllRonda(){
        Cursor cur = null;
        ArrayList<TbRonda> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_ronda",null);
        if(cur.moveToFirst()){
            do{
                TbRonda tb = new TbRonda();
                tb.id_ronda = cur.getInt(0);
                tb.hari = cur.getString(1);
                tb.id_warga = cur.getInt(2);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public ArrayList<TbSurat> getAllSurat(){
        Cursor cur = null;
        ArrayList<TbSurat> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_surat",null);
        if(cur.moveToFirst()){
            do{
                TbSurat tb = new TbSurat();
                tb.id_surat = cur.getInt(0);
                tb.penerima = cur.getString(1);
                tb.pengirim = cur.getString(2);
                tb.pesan = cur.getString(3);
                tb.tanggal = cur.getString(4);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public ArrayList<TbChat> getAllChat(){
        Cursor cur = null;
        ArrayList<TbChat> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_chat",null);
        if(cur.moveToFirst()){
            do{
                TbChat tb = new TbChat();
                tb.id_chat= cur.getInt(0);
                tb.wilayah = cur.getString(1);
                tb.pengirim = cur.getString(2);
                tb.pesan = cur.getString(3);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public TbWarga getWargaById(int id_warga){
        Cursor cur = null;
        TbWarga tb = new TbWarga();
        cur = db.rawQuery("SELECT * FROM tb_warga WHERE id_warga="+id_warga, null);

        if(cur.moveToFirst()){
            tb.id_warga = cur.getInt(0);
            tb.nama = cur.getString(1);
            tb.lat = Double.parseDouble(cur.getString(2));
            tb.lon = Double.parseDouble(cur.getString(3));
            tb.alamat = cur.getString(4);
            tb.kontak = cur.getString(5);
            tb.status = cur.getString(6);
            tb.wilayah = cur.getString(7);
            tb.foto = cur.getString(8);
        }
        cur.close();
        return tb;
    }

    public int getWargaLastId(){
        Cursor cur = null;
        int result = -1;
        cur = db.rawQuery("SELECT MAX(id_warga) FROM tb_warga", null);

        if(cur.getCount()>0){   //ada data? ambil
            cur.moveToFirst();
            result = cur.getInt(0);
        }
        cur.close();
        return result;
    }

    public int getAkunLastId(){
        Cursor cur = null;
        int result = -1;
        cur = db.rawQuery("SELECT MAX(id_akun) FROM tb_akun", null);

        if(cur.getCount()>0){   //ada data? ambil
            cur.moveToFirst();
            result = cur.getInt(0);
        }
        cur.close();
        return result;
    }

    public int getRondaLastId(){
        Cursor cur = null;
        int result = -1;
        cur = db.rawQuery("SELECT MAX(id_ronda) FROM tb_ronda", null);

        if(cur.getCount()>0){   //ada data? ambil
            cur.moveToFirst();
            result = cur.getInt(0);
        }
        cur.close();
        return result;
    }

    public ArrayList<TbRonda> getRondaByHari(String hari){
        Cursor cur = null;
        ArrayList<TbRonda> out = new ArrayList<>();
        cur = db.rawQuery("SELECT * FROM tb_ronda WHERE hari="+hari,null);
        if(cur.moveToFirst()){
            do{
                TbRonda tb = new TbRonda();
                tb.id_warga = cur.getInt(0);
                tb.hari = cur.getString(1);
                tb.id_ronda = cur.getInt(2);

                out.add(tb);
            }while(cur.moveToNext());
        }
        cur.close();
        return out;
    }
}
