package com.andres18160gmail.arduinobluetooth.Datos;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class db_Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dataBaseControl.db";

    public db_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TablaUsuarios.SQL_CREATE_USUARIOS);
        db.execSQL(TablaDispositivos.SQL_CREATE_DISPOSITIVOS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TablaUsuarios.SQL_DELETE_USUARIOS);
        db.execSQL(TablaDispositivos.SQL_DELETE_DISPOSITIVOS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
