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
    private MiAsyncTask tareaAsincrona;

    private int progressChangedValue = 0;

    ArrayList<EnDispositivo> listDatos;
    RecyclerView recycler;
    TablaDispositivos cdDispositivos;
    DispositivosControlAdapter adapter;
    EnDispositivo dispositivo=new EnDispositivo();
    TextView txtInformacion;

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
            recycler.setAdapter(adapter);
        }
        return v;
    }


    @Override
    public void itemCheck(View view, int position) {
        dispositivo=adapter.getItem(position);
        MensajeToast("Click");
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
        MensajeToast("Progreso="+progressChangedValue);
    }

    @Override
    public void itemStartTrackingTouch(View view, int position) {

    }

    @Override
    public void itemStopTrackingTouch(View view, int position) {
        MensajeToast("Stop Progreso="+progressChangedValue);
    }
}
