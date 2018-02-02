package com.andres18160gmail.arduinobluetooth.Adaptadores;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.andres18160gmail.arduinobluetooth.Entidades.EnTipo;
import com.andres18160gmail.arduinobluetooth.R;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by ANDRE on 01/02/2018.
 */

public class TipoDispositivoAdapter extends BaseAdapter {
    Context contexto;
    List<EnTipo> ListaObjetos,ListTemp;
    CustomFilter cs;

    public TipoDispositivoAdapter(Context contexto, List<EnTipo> Objetos){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista=convertView;
        LayoutInflater inflate= LayoutInflater.from(contexto);
        vista=inflate.inflate(R.layout.item_tipe_dispositivo,null);

        GifImageView imagen=(GifImageView)vista.findViewById(R.id.foto);
        TextView titulo=(TextView)vista.findViewById(R.id.txtviewTitle);

        String nombreCompleto=ListaObjetos.get(position).getTitulo();
        titulo.setText(nombreCompleto);
        AssetManager manager = contexto.getAssets();
        try{
            GifDrawable gifFromAssets = new GifDrawable(manager.openFd(ListaObjetos.get(position).getImagen()));
            imagen.setBackground(gifFromAssets);
        }catch (Exception e){
            Log.e("Error Foto",e.getMessage().toString());
        }


        return vista;

    }
    public Filter getFilter( ) {

        if(cs==null){
            cs= new CustomFilter();
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
                ArrayList<EnTipo> filters=new ArrayList<>();

                for (int i=0;i<ListTemp.size();i++){
                    if(ListTemp.get(i).getTitulo().toUpperCase().contains(constraint)){
                        EnTipo tipo=ListTemp.get(i);
                        filters.add(tipo);
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
            ListaObjetos=(ArrayList<EnTipo>) results.values;
            notifyDataSetChanged();
        }
    }
}

