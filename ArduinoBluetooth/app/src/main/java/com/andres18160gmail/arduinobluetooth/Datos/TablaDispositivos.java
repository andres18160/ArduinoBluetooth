package com.andres18160gmail.arduinobluetooth.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.Entidades.EnUsuario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ANDRE on 01/02/2018.
 */

public final class TablaDispositivos {
    private db_Helper helper;

    public TablaDispositivos(Context context) {
        helper = new db_Helper(context);
    }

    public static final String TABLE_NAME="dispositivos";
    public static final String ID ="Id";
    public static final String NOMBRE ="Nombre";
    public static final String TIPO ="Tipo";
    public static final String PIN ="Pin";
    public static final String FOTO ="Foto";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_DISPOSITIVOS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NOMBRE + TEXT_TYPE +COMMA_SEP +
                    TIPO + TEXT_TYPE +COMMA_SEP +
                    PIN + TEXT_TYPE +COMMA_SEP +
                    FOTO + TEXT_TYPE +" )";

    public static final String SQL_DELETE_DISPOSITIVOS ="DROP TABLE IF EXISTS " + TABLE_NAME;






    public boolean GuardarDispositivo(EnDispositivo dispostiivo){
        SQLiteDatabase db = helper.getWritableDatabase();

        try{

            ContentValues values = new ContentValues();
            values.put(NOMBRE, dispostiivo.getNombre());
            values.put(TIPO, dispostiivo.getTipo());
            values.put(PIN, dispostiivo.getPin());
            values.put(FOTO, dispostiivo.getFoto());
            //inserta los datos y devuelve la clave primaria del registro insertado
            long newRowId = db.insert(TABLE_NAME, null, values);
            if(newRowId==-1){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }



    public boolean ActualizarDispositivo(EnDispositivo dispostivo){
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(NOMBRE, dispostivo.getNombre());
            values.put(TIPO, dispostivo.getTipo());
            values.put(PIN, dispostivo.getPin());
            values.put(FOTO, dispostivo.getFoto());
            // Which row to update, based on the title
            String selection = ID + " LIKE ?";
            String[] selectionArgs = { ""+dispostivo.getId() };
            int count = db.update(TABLE_NAME,values,selection,selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }

    public boolean EliminarDispositivo(String Id){
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            // Define 'where' part of query.
            String selection = ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { Id };
            // Issue SQL statement.
            db.delete(TABLE_NAME, selection,selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public ArrayList<EnDispositivo> GetListaDispositivos(){
        EnDispositivo dispostiivo;
        ArrayList< EnDispositivo> ListaDispositivos=new ArrayList<EnDispositivo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {ID,NOMBRE,TIPO,PIN,FOTO};
// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    null,                                // Las columnas para la cláusula WHERE
                    null,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                dispostiivo=new EnDispositivo();
                dispostiivo.setId(Integer.parseInt(c.getString(0)));
                dispostiivo.setNombre(c.getString(1));
                dispostiivo.setTipo(c.getString(2));
                dispostiivo.setPin(c.getString(3));
                dispostiivo.setFoto(c.getString(4));
                ListaDispositivos.add(dispostiivo);
            }
            return ListaDispositivos;
        }catch(Exception e){
            return null;
        }finally {
            db.close();
        }
    }
    public EnDispositivo BuscarDispositivo(String Id){
        EnDispositivo dispostiivo=new EnDispositivo();
        SQLiteDatabase db = helper.getReadableDatabase();

// Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {ID,NOMBRE,TIPO,PIN,FOTO};

// Filtrar resultados DONDE "título" = "Mi título"
        String selection = ID + " = ?";
        String[] selectionArgs = { Id };

// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    selection,                                // Las columnas para la cláusula WHERE
                    selectionArgs,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );

            c.moveToFirst();//mueve el cursor al primer registro del resultSet
            dispostiivo.setId(Integer.parseInt(c.getString(0)));
            dispostiivo.setNombre(c.getString(1));
            dispostiivo.setTipo(c.getString(2));
            dispostiivo.setPin(c.getString(3));
            dispostiivo.setFoto(c.getString(4));

            return dispostiivo;


        }catch(Exception e){
            return null;
        }
    }

}
