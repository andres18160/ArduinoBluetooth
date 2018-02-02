package com.andres18160gmail.arduinobluetooth.Adaptadores;


import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * Created by ANDRE on 01/02/2018.
 */

public class DispositivosControlAdapter extends RecyclerView.Adapter<DispositivosControlAdapter.ViewHolderDispositivos> {
  private onCheckertoggle onchecktoggle;
    ArrayList<EnDispositivo> listDatos;
    Context contexto;
    public DispositivosControlAdapter(ArrayList<EnDispositivo> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public ViewHolderDispositivos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_control_digital,null,false);
        contexto=parent.getContext();
        return new ViewHolderDispositivos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDispositivos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public EnDispositivo getItem(int position) {
        return listDatos != null ? listDatos.get(position) : null;
    }

    public void setOnchecktoggle(onCheckertoggle onchecktoggle){
        this.onchecktoggle=onchecktoggle;
    }

    public class ViewHolderDispositivos extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView titulo;
        ToggleButton toggle;
        GifImageView imagen;

        public ViewHolderDispositivos(View itemView) {
            super(itemView);
            titulo=(TextView)itemView.findViewById(R.id.txtTitulo);
            toggle=(ToggleButton)itemView.findViewById(R.id.toggleBoton);
            imagen=(GifImageView)itemView.findViewById(R.id.foto);

            toggle.setOnCheckedChangeListener(this);

        }

        public void asignarDatos(EnDispositivo enDispositivo) {
            titulo.setText(enDispositivo.getNombre());
            AssetManager manager = contexto.getAssets();
            GifDrawable gifFromAssets;

            try{
                if(enDispositivo.getFoto().equalsIgnoreCase("Televisor")){
                    gifFromAssets = new GifDrawable(manager.openFd("television.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Ventilador")){
                    gifFromAssets = new GifDrawable(manager.openFd("ventiladorgif.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Bombillo")){
                    gifFromAssets = new GifDrawable(manager.openFd("bombillo.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Puerta")){
                    gifFromAssets = new GifDrawable(manager.openFd("puerta.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Garaje")){
                    gifFromAssets = new GifDrawable(manager.openFd("garaje.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Generico")){
                    gifFromAssets = new GifDrawable(manager.openFd("onof.gif"));
                    gifFromAssets.stop();
                    imagen.setBackground(gifFromAssets);
                }

            }catch (Exception e){
                Log.e("Error Foto",e.getMessage().toString());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onchecktoggle!=null){
                    onchecktoggle.itemCheck(buttonView,getPosition());
                }
        }
    }

    public interface onCheckertoggle{
        public void itemCheck(View view,int position);

    }


}
