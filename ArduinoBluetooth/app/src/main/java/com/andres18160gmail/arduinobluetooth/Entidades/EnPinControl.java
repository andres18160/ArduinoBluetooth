package com.andres18160gmail.arduinobluetooth.Entidades;

/**
 * Created by ANDRE on 03/02/2018.
 */

public class EnPinControl {

    private String pin;
    private String estado;
    private String informacion = "";

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}
