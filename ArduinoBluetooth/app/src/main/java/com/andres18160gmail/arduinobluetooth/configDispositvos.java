package com.andres18160gmail.arduinobluetooth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andres18160gmail.arduinobluetooth.Adaptadores.DispositivosAdapter;
import com.andres18160gmail.arduinobluetooth.Datos.TablaDispositivos;
import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;

import java.util.ArrayList;


public class configDispositvos extends Fragment implements SearchView.OnQueryTextListener{
    ListView listViewDispostivos;
    private SearchView txtBuscar;
    ArrayList<EnDispositivo> listaDispositivos;
    private EnDispositivo dispositivo;
    private DispositivosAdapter miadaptador;
    private TablaDispositivos cdDispositivo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_config_dispositvos, container, false);
        cdDispositivo=new TablaDispositivos(v.getContext());
        txtBuscar=(SearchView)v.findViewById(R.id.txtBuscar);
        txtBuscar.setOnQueryTextListener(this);

        listViewDispostivos=(ListView)v.findViewById(R.id.listViewDispostivos);
        CargarListaDispostivos();

        listViewDispostivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dispositivo=(EnDispositivo) parent.getItemAtPosition(position);
                Intent i=new Intent(getContext(),ConfigDetalleActivity.class);
                i.putExtra("id",""+dispositivo.getId());
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i=new Intent(getContext(),ConfigDetalleActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

    private void CargarListaDispostivos() {
        listaDispositivos=cdDispositivo.GetListaDispositivos();
        if(listaDispositivos==null)
            return;
        miadaptador=new DispositivosAdapter(getContext(),listaDispositivos);
        listViewDispostivos.setAdapter(miadaptador);
    }

    @Override
    public void onResume() {
        super.onResume();
        CargarListaDispostivos();

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        miadaptador.getFilter().filter(newText);
        return false;
    }

    public void makeInfo(int pos) {
        Log.i("makeInfo", "=" + pos);
    }
}
