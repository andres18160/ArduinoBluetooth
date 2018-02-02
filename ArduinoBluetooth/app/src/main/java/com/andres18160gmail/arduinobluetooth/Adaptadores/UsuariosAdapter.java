package com.andres18160gmail.arduinobluetooth.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andres18160gmail.arduinobluetooth.Entidades.EnUsuario;
import com.andres18160gmail.arduinobluetooth.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ANDRE on 29/01/2018.
 */

public class UsuariosAdapter extends BaseAdapter {

     Context contexto;
     List<EnUsuario> ListaObjetos,ListTemp;
     CustomFilter cs;

    public UsuariosAdapter(Context contexto, List<EnUsuario> Objetos){
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
        return ListaObjetos.get(position).get_id();//Retorna el id de la posicion indicada
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista=convertView;
        LayoutInflater inflate= LayoutInflater.from(contexto);
        vista=inflate.inflate(R.layout.itemlistviewusers,null);

        ImageView imagen=(ImageView)vista.findViewById(R.id.foto);
        TextView titulo=(TextView)vista.findViewById(R.id.txtviewTitle);
        TextView descripcion=(TextView)vista.findViewById(R.id.txtviewDecripcion);

        String nombreCompleto=ListaObjetos.get(position).getNombres()+" "+ListaObjetos.get(position).getApellidos();
        titulo.setText(nombreCompleto);
        descripcion.setText(ListaObjetos.get(position).getNombreDeUsuario());
        Bitmap bitmap = null;
        try{
            imagen.setImageBitmap(ListaObjetos.get(position).getFoto());
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
                ArrayList<EnUsuario> filters=new ArrayList<>();

                for (int i=0;i<ListTemp.size();i++){
                    if(ListTemp.get(i).getNombreDeUsuario().toUpperCase().contains(constraint)){
                        EnUsuario usuario=ListTemp.get(i);
                        filters.add(usuario);
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
            ListaObjetos=(ArrayList<EnUsuario>) results.values;
            notifyDataSetChanged();
        }
    }
}
