package com.andres18160gmail.arduinobluetooth.Adaptadores;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.Entidades.EnTipo;
import com.andres18160gmail.arduinobluetooth.R;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by ANDRE on 01/02/2018.
 */

public class DispositivosAdapter extends BaseAdapter {
    Context contexto;
    List<EnDispositivo> ListaObjetos,ListTemp;
    DispositivosAdapter.CustomFilter cs;
    View vista;



    public DispositivosAdapter(Context contexto, List<EnDispositivo> Objetos){
        this.contexto=contexto;
        ListaObjetos=Objetos;
        ListTemp=Objetos;
    }

    @Override
    public int getCount() {
        return ListaObjetos.size();//Retorna la cantidad de elementos de la lista.
    }

    @Override
    public Object getItem(int position) {

        return ListaObjetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ListaObjetos.get(position).getId();//Retorna el id de la posicion indicada
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         vista=convertView;

        LayoutInflater inflate= LayoutInflater.from(contexto);
        vista=inflate.inflate(R.layout.itemlist_dispositivos,null);

        GifImageView imagen=(GifImageView)vista.findViewById(R.id.foto);


        TextView titulo=(TextView)vista.findViewById(R.id.txtviewTitle);
        TextView descripcion=(TextView)vista.findViewById(R.id.txtviewDecripcion);

        String nombreCompleto=ListaObjetos.get(position).getNombre();
        titulo.setText(nombreCompleto);
        descripcion.setText("Control "+ ListaObjetos.get(position).getTipo() +" PIN: "+ListaObjetos.get(position).getPin());
        AssetManager manager = contexto.getAssets();
        GifDrawable gifFromAssets;

        try{
            if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Televisor")){
                 gifFromAssets = new GifDrawable(manager.openFd("television.gif"));
                gifFromAssets.stop();
                 imagen.setBackground(gifFromAssets);
            }else if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Ventilador")){
                 gifFromAssets = new GifDrawable(manager.openFd("ventiladorgif.gif"));
                gifFromAssets.stop();
                 imagen.setBackground(gifFromAssets);
            }else if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Bombillo")){
                 gifFromAssets = new GifDrawable(manager.openFd("bombillo.gif"));
                gifFromAssets.stop();
                 imagen.setBackground(gifFromAssets);
            }else if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Puerta")){
                gifFromAssets = new GifDrawable(manager.openFd("puerta.gif"));
                gifFromAssets.stop();
                imagen.setBackground(gifFromAssets);
            }else if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Garaje")){
                gifFromAssets = new GifDrawable(manager.openFd("garaje.gif"));
                gifFromAssets.stop();
                imagen.setBackground(gifFromAssets);
            }else if(ListaObjetos.get(position).getFoto().equalsIgnoreCase("Generico")){
                gifFromAssets = new GifDrawable(manager.openFd("onof.gif"));
                gifFromAssets.stop();
                imagen.setBackground(gifFromAssets);
            }



        }catch (Exception e){
            Log.e("Error Foto",e.getMessage().toString());
        }




        return vista;

    }
    public Filter getFilter( ) {

        if(cs==null){
            cs=new CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result=new FilterResults();

            if(constraint!=null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<EnDispositivo> filters=new ArrayList<>();

                for (int i=0;i<ListTemp.size();i++){
                    if(ListTemp.get(i).getNombre().toUpperCase().contains(constraint)){
                        EnDispositivo dispositivo=ListTemp.get(i);
                        filters.add(dispositivo);
                    }
                }
                result.count=filters.size();
                result.values=filters;
            }else{
                result.count=ListTemp.size();
                result.values=ListTemp;
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ListaObjetos=(ArrayList<EnDispositivo>) results.values;
            notifyDataSetChanged();
        }
    }
}
