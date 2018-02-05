package com.andres18160gmail.arduinobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Adaptadores.DispositivosControlAdapter;
import com.andres18160gmail.arduinobluetooth.Clases.MiAsyncTask;
import com.andres18160gmail.arduinobluetooth.Datos.TablaDispositivos;
import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.Entidades.EnPinControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Principal extends Fragment implements DispositivosControlAdapter.onCheckertoggle,MiAsyncTask.MiCallback,DispositivosControlAdapter.onSeekBar {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String NOMBRE_DISPOSITIVO_BT = "HC-06";//Nombre de neustro dispositivo bluetooth.

    private int progressChangedValue = 0;

    ArrayList<EnDispositivo> listDatos;
    RecyclerView recycler;
    TablaDispositivos cdDispositivos;
    DispositivosControlAdapter adapter;
    EnDispositivo dispositivo=new EnDispositivo();
    TextView txtInformacion;
    BluetoothDevice arduino = null;
    private MiAsyncTask tareaAsincrona;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_principal, container, false);
        cdDispositivos=new TablaDispositivos(v.getContext());
        recycler=(RecyclerView)v.findViewById(R.id.RecyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,false));
        txtInformacion=(TextView)v.findViewById(R.id.txtInformacion);
        //recycler.setLayoutManager(new GridLayoutManager(v.getContext(),2));
        listDatos=cdDispositivos.GetListaDispositivos();
        if(listDatos!=null){
            adapter=new DispositivosControlAdapter(listDatos);
            adapter.setOnchecktoggle(this);
            adapter.setOnSeekBar(this);
            recycler.setAdapter(adapter);
        }

        return v;
    }
    private void descubrirDispositivosBT() {
/*
Este método comprueba si nuestro dispositivo dispone de conectividad bluetooh.
En caso afirmativo, si estuviera desctivada, intenta activarla.
En caso negativo presenta un mensaje al usuario y sale de la aplicación.
*/
//Comprobamos que el dispositivo tiene adaptador bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        txtInformacion.setText("Comprobando bluetooth");

        if (mBluetoothAdapter != null) {

//El dispositivo tiene adapatador BT. Ahora comprobamos que bt esta activado.

            if (mBluetoothAdapter.isEnabled()) {
//Esta activado. Obtenemos la lista de dispositivos BT emparejados con nuestro dispositivo android.

                txtInformacion.setText("Obteniendo dispositivos emparejados, espere...");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//Si hay dispositivos emparejados
                if (pairedDevices.size()> 0) {
/*
Recorremos los dispositivos emparejados hasta encontrar el
adaptador BT del arduino, en este caso se llama HC-06
*/
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equalsIgnoreCase(NOMBRE_DISPOSITIVO_BT)) {
                            arduino = device;
                        }
                    }

                    if (arduino != null) {
                        tareaAsincrona = new MiAsyncTask(this);
                        tareaAsincrona.execute(arduino);
                        txtInformacion.setText("Conectado!");
                    } else {
//No hemos encontrado nuestro dispositivo BT, es necesario emparejarlo antes de poder usarlo.
//No hay ningun dispositivo emparejado. Salimos de la app.
                        Toast.makeText(getContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                        txtInformacion.setText("No hay dispositivos emparejados, por favor, empareje el arduino");
                    }
                } else {
//No hay ningun dispositivo emparejado. Salimos de la app.
                    Toast.makeText(getContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                    txtInformacion.setText("No hay dispositivos emparejados, por favor, empareje el arduino");

                }
            } else {
                muestraDialogoConfirmacionActivacion();
            }
        } else {
// El dispositivo no soporta bluetooth. Mensaje al usuario y salimos de la app
            Toast.makeText(getContext(), "El dispositivo no soporta comunicación por Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private void muestraDialogoConfirmacionActivacion() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Activar Bluetooth")
                .setMessage("BT esta desactivado. ¿Desea activarlo?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//Intentamos activarlo con el siguiente intent.
                        MensajeToast("Activando BT");
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//Salimos de la app
                        MensajeToast("El Bluetooth debe estar encendido!");
                    }
                })
                .show();
    }

    @Override
    public void itemCheck(View view, int position,boolean isChecked) {
        dispositivo=adapter.getItem(position);
        if (isChecked) {
            MensajeToast("Activo");
            //tareaAsincrona.write(dispositivo.getPin());
        } else {
            MensajeToast("Inactivo");
            //tareaAsincrona.write(dispositivo.getPin());
        }
    }


    private void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPinControlUpdate(EnPinControl p) {
        txtInformacion.setText(p.getInformacion());

    }

    @Override
    public void itemProgressChanged(View view, int progress, int position) {
        progressChangedValue=progress;
    }

    @Override
    public void itemStartTrackingTouch(View view, int position) {

    }

    @Override
    public void itemStopTrackingTouch(View view, int position) {
        MensajeToast("Stop Progreso="+progressChangedValue);
        tareaAsincrona.write(""+progressChangedValue);
    }
}
