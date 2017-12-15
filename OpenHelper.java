package com.trois.wade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by A450CC on 12/4/2017.
 */

public class OpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_Wade.db";

    public static final String TABLE_AKUN_CREATE =
            "CREATE TABLE tb_akun (id_akun INTEGER PRIMARY KEY, username TEXT, password TEXT, email TEXT, id_warga INTEGER)";
    public static final String TABLE_WARGA_CREATE =
            "CREATE TABLE tb_warga (id_warga INTEGER PRIMARY KEY, nama TEXT, lat TEXT, lon TEXT, alamat TEXT, kontak TEXT, status TEXT, wilayah TEXT, foto TEXT)";
    public static final String TABLE_RONDA_CREATE =
            "CREATE TABLE tb_ronda (id_ronda INTEGER PRIMARY KEY, hari TEXT, id_warga INTEGER)";
    public static final String TABLE_CHAT_CREATE =
            "CREATE TABLE tb_chat (id_chat INTEGER PRIMARY KEY, wilayah TEXT, pengirim TEXT, pesan TEXT)";
    public static final String TABLE_SURAT_CREATE =
            "CREATE TABLE tb_surat (id_surat INTEGER PRIMARY KEY, penerima TEXT, pengirim TEXT, pesan TEXT, tanggal DATE)";

    public OpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_AKUN_CREATE);
        db.execSQL(TABLE_WARGA_CREATE);
        db.execSQL(TABLE_RONDA_CREATE);
        db.execSQL(TABLE_CHAT_CREATE);
        db.execSQL(TABLE_SURAT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS tb_akun");
        db.execSQL("DROP TABLE IF EXISTS tb_warga");
        db.execSQL("DROP TABLE IF EXISTS tb_keluarga");
        db.execSQL("DROP TABLE IF EXISTS tb_ronda");
        db.execSQL("DROP TABLE IF EXISTS tb_chat");
        db.execSQL("DROP TABLE IF EXISTS tb_surat");
        db.execSQL(TABLE_AKUN_CREATE);
        db.execSQL(TABLE_WARGA_CREATE);
        db.execSQL(TABLE_RONDA_CREATE);
        db.execSQL(TABLE_CHAT_CREATE);
        db.execSQL(TABLE_SURAT_CREATE);
    }

}
