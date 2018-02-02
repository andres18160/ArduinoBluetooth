package com.andres18160gmail.arduinobluetooth.Entidades;

import android.graphics.Bitmap;
import java.io.Serializable;
/**
 * Created by ANDRE on 01/02/2018.
 */

public class EnDispositivo implements Serializable {
    private int Id;
    private String Nombre;
    private String Tipo;
    private String Pin;
    private String Foto;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
