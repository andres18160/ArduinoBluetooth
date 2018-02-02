package com.andres18160gmail.arduinobluetooth.Entidades;

import java.io.Serializable;

/**
 * Created by ANDRE on 01/02/2018.
 */

public class EnTipo implements Serializable {
    private int id;
    private String Titulo;
    private String Imagen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}
