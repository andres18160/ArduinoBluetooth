package com.andres18160gmail.arduinobluetooth;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Adaptadores.DispositivosControlAdapter;
import com.andres18160gmail.arduinobluetooth.Datos.TablaDispositivos;
import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Principal extends Fragment implements DispositivosControlAdapter.onCheckertoggle {


    ArrayList<EnDispositivo> listDatos;
    RecyclerView recycler;
    TablaDispositivos cdDispositivos;
    DispositivosControlAdapter adapter;
    EnDispositivo dispositivo=new EnDispositivo();

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
}
