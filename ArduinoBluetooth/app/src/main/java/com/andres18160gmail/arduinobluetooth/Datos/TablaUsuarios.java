package com.andres18160gmail.arduinobluetooth.Datos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.andres18160gmail.arduinobluetooth.Entidades.EnUsuario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public final class TablaUsuarios {

    private db_Helper helper;

    public TablaUsuarios(Context context) {
        helper = new db_Helper(context);
    }


    public static final String TABLE_NAME="usuarios";
    public static final String _ID ="Id";
    public static final String USUARIO ="Usuario";
    public static final String NOMBRE ="Nombre";
    public static final String APELLIDO ="Apellido";
    public static final String CLAVE ="Clave";
    public static final String FOTO ="Foto";
    public static final String ESTADO ="Estado";


    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_USUARIOS =
            "CREATE TABLE " + TablaUsuarios.TABLE_NAME + " (" +
                    TablaUsuarios._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TablaUsuarios.USUARIO + TEXT_TYPE+" UNIQUE NOT NULL " + COMMA_SEP +
                    TablaUsuarios.NOMBRE + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.APELLIDO + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.CLAVE + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.FOTO + BLOB_TYPE +COMMA_SEP +
                    TablaUsuarios.ESTADO + TEXT_TYPE +" )";

    public static final String SQL_DELETE_USUARIOS ="DROP TABLE IF EXISTS " + TablaUsuarios.TABLE_NAME;



    public boolean GuardarUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getWritableDatabase();

        try{

            ContentValues values = new ContentValues();
            values.put(USUARIO, usuario.getNombreDeUsuario());
            values.put(APELLIDO, usuario.getApellidos());
            values.put(NOMBRE, usuario.getNombres());
            values.put(CLAVE, usuario.getClave());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            usuario.getFoto().compress(Bitmap.CompressFormat.JPEG, 100 , baos);
            byte[] blob = baos.toByteArray();
            values.put(FOTO, blob);
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

    public boolean ActualizarUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getReadableDatabase();
            try{
                // New value for one column
                ContentValues values = new ContentValues();
                values.put(USUARIO, usuario.getNombreDeUsuario());
                values.put(APELLIDO,usuario.getApellidos());
                values.put(NOMBRE, usuario.getNombres());
                values.put(CLAVE, usuario.getClave());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                usuario.getFoto().compress(Bitmap.CompressFormat.PNG, 100 , baos);
                byte[] blob = baos.toByteArray();
                values.put(FOTO, blob);

                // Which row to update, based on the title
                String selection = USUARIO + " LIKE ?";
                String[] selectionArgs = { usuario.getNombreDeUsuario() };

                int count = db.update(
                        TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                return true;
            }catch (Exception e){
                return false;
            }finally {
                db.close();
            }
    }

    public boolean ActualizarEstadoUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getReadableDatabase();
        try{

            // New value for one column
            ContentValues values = new ContentValues();
            values.put(ESTADO, usuario.getEstado());

            // Which row to update, based on the title
            String selection = USUARIO + " LIKE ?";
            String[] selectionArgs = { usuario.getNombreDeUsuario() };

            int count = db.update(
                    TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }
    public boolean EliminarUsuario(String usuario){
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            // Define 'where' part of query.
            String selection = USUARIO + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { usuario };
            // Issue SQL statement.
            db.delete(TABLE_NAME, selection,selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public ArrayList<EnUsuario> GetListaUsuarios(){
        EnUsuario usuario;
        ArrayList< EnUsuario> listaUsuarios=new ArrayList<EnUsuario>();
        SQLiteDatabase db = helper.getReadableDatabase();
        // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                APELLIDO,
                NOMBRE,
                USUARIO,
                CLAVE,
                ESTADO,
                FOTO

        };
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
                usuario=new EnUsuario();
                usuario.set_id(Integer.parseInt(c.getString(0)));
                usuario.setApellidos(c.getString(1));
                usuario.setNombres(c.getString(2));
                usuario.setNombreDeUsuario(c.getString(3));
                usuario.setClave(c.getString(4));
                usuario.setEstado(c.getString(5));
                byte[] blob = c.getBlob(6);
                ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                usuario.setFoto(BitmapFactory.decodeStream(bais));
                listaUsuarios.add(usuario);
            }
            return listaUsuarios;
        }catch(Exception e){
            return null;
        }finally {
            db.close();
        }
    }

    public EnUsuario ValidarUsuario(String Username, String Clave){
    EnUsuario usuario=new EnUsuario();
    SQLiteDatabase db = helper.getReadableDatabase();
    // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
    String[] projection = {
            _ID,
            APELLIDO,
            NOMBRE,
            USUARIO,
            CLAVE,
            ESTADO,
            FOTO

    };
    // Filtrar resultados DONDE "título" = "Mi título"
    String selection = TablaUsuarios.USUARIO + " = ?";
    String[] selectionArgs = { Username };

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
        usuario.set_id(Integer.parseInt(c.getString(0)));
        usuario.setApellidos(c.getString(1));
        usuario.setNombres(c.getString(2));
        usuario.setNombreDeUsuario(c.getString(3));
        usuario.setClave(c.getString(4));
        usuario.setEstado(c.getString(5));
        byte[] blob = c.getBlob(6);
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        usuario.setFoto(BitmapFactory.decodeStream(bais));

        if(usuario.getClave().equals(Clave)){
            return  usuario;
        }else{
            return null;
        }


    }catch(Exception e){
        return null;
    }finally {
        db.close();
    }
}
    public EnUsuario BuscarUsuarioId(String Id){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();

// Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                APELLIDO,
                NOMBRE,
                USUARIO,
                CLAVE,
                ESTADO,
                FOTO

        };

// Filtrar resultados DONDE "título" = "Mi título"
        String selection = TablaUsuarios._ID + " = ?";
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
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setApellidos(c.getString(1));
            usuario.setNombres(c.getString(2));
            usuario.setNombreDeUsuario(c.getString(3));
            usuario.setClave(c.getString(4));
            usuario.setEstado(c.getString(5));
            byte[] blob = c.getBlob(6);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            usuario.setFoto(BitmapFactory.decodeStream(bais));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }

    public EnUsuario BuscarUsuario(String userName){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();

// Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                APELLIDO,
                NOMBRE,
                USUARIO,
                CLAVE,
                ESTADO,
                FOTO

        };

// Filtrar resultados DONDE "título" = "Mi título"
        String selection = USUARIO + " = ?";
        String[] selectionArgs = { userName };

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
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setApellidos(c.getString(1));
            usuario.setNombres(c.getString(2));
            usuario.setNombreDeUsuario(c.getString(3));
            usuario.setClave(c.getString(4));
            usuario.setEstado(c.getString(5));
            byte[] blob = c.getBlob(6);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            usuario.setFoto(BitmapFactory.decodeStream(bais));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }

    public EnUsuario BuscarUsuarioActivo(){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();
        // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                APELLIDO,
                NOMBRE,
                USUARIO,
                CLAVE,
                ESTADO,
                FOTO
        };
// Filtrar resultados DONDE "título" = "Mi título"
        String selection = TablaUsuarios.ESTADO + " = ?";
        String[] selectionArgs = { "Activo" };
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
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setApellidos(c.getString(1));
            usuario.setNombres(c.getString(2));
            usuario.setNombreDeUsuario(c.getString(3));
            usuario.setClave(c.getString(4));
            usuario.setEstado(c.getString(5));
            byte[] blob = c.getBlob(6);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            usuario.setFoto(BitmapFactory.decodeStream(bais));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }


}

