package com.andres18160gmail.arduinobluetooth.Entidades;

/**
 * Created by ANDRE on 03/02/2018.
 */

public class Temperatura {
    private String informacion = "";
    private String temperatura = "";
    private String nombreDispositivo = "";

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nodo) {
        this.nombreDispositivo = nodo;
    }
}
