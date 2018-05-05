package com.andres18160gmail.arduinobluetooth.Adaptadores;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * Created by ANDRE on 01/02/2018.
 */

public class DispositivosControlAdapter extends RecyclerView.Adapter<DispositivosControlAdapter.ViewHolderDispositivos> {
  private onCheckertoggle onchecktoggle;
  private onSeekBar onSeekBarItem;
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

    public void setOnSeekBar(onSeekBar onSeekBarItem){
        this.onSeekBarItem=onSeekBarItem;
    }

    public class ViewHolderDispositivos extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
        TextView titulo;
        ToggleButton toggle;
        ImageView imagen;
        SeekBar analogo;

        public ViewHolderDispositivos(View itemView) {
            super(itemView);
            titulo=(TextView)itemView.findViewById(R.id.txtTitulo);
            toggle=(ToggleButton)itemView.findViewById(R.id.toggleBoton);
            imagen=(ImageView)itemView.findViewById(R.id.foto);
            analogo=(SeekBar)itemView.findViewById(R.id.seekBarAnalogo);
            toggle.setOnCheckedChangeListener(this);
            analogo.setOnSeekBarChangeListener(this);

        }

        public void asignarDatos(EnDispositivo enDispositivo) {
            titulo.setText(enDispositivo.getNombre());
            AssetManager manager = contexto.getAssets();
            GifDrawable gifFromAssets;

            try{
                if(enDispositivo.getFoto().equalsIgnoreCase("Televisor") || enDispositivo.getFoto().equalsIgnoreCase("TV")){
                    imagen.setImageBitmap(getBitmapFromAssets("televisor.png") );
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Ventilador") || enDispositivo.getFoto().equalsIgnoreCase("Fan")){
                    imagen.setImageBitmap(getBitmapFromAssets("ventilador.png"));
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Bombillo")  || enDispositivo.getFoto().equalsIgnoreCase("Light bulb")){
                    imagen.setImageBitmap(getBitmapFromAssets("bombillo.png"));
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Puerta") || enDispositivo.getFoto().equalsIgnoreCase("Door")){
                    imagen.setImageBitmap(getBitmapFromAssets("puerta.png"));
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Garaje") || enDispositivo.getFoto().equalsIgnoreCase("Garage")){
                    imagen.setImageBitmap(getBitmapFromAssets("garaje.png"));
                }else if(enDispositivo.getFoto().equalsIgnoreCase("Generico") || enDispositivo.getFoto().equalsIgnoreCase("Generic")){
                    imagen.setImageBitmap(getBitmapFromAssets("onof.png"));
                }
                if(enDispositivo.getTipo().equalsIgnoreCase("Digital")){
                    analogo.setVisibility(View.INVISIBLE);
                }else if (enDispositivo.getTipo().equalsIgnoreCase("Analogo")){
                    toggle.setVisibility(View.INVISIBLE);
                }

            }catch (Exception e){
                Log.e("Error Foto",e.getMessage().toString());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onchecktoggle!=null){
                    onchecktoggle.itemCheck(buttonView,getPosition(),isChecked);
                }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(onSeekBarItem!=null){
                onSeekBarItem.itemProgressChanged(seekBar,progress,getPosition());
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(onSeekBarItem!=null){
                onSeekBarItem.itemStartTrackingTouch(seekBar,getPosition());
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(onSeekBarItem!=null){
                onSeekBarItem.itemStopTrackingTouch(seekBar,getPosition());
            }
        }
    }

    public interface onCheckertoggle{
        public void itemCheck(View view,int position,boolean isChecked);

    }

    public interface onSeekBar{
        public void itemProgressChanged(View view,int progress,int position);
        public void itemStartTrackingTouch(View view,int position);
        public void itemStopTrackingTouch(View view,int position);

    }

    public Bitmap getBitmapFromAssets(String fileName) throws IOException {
        AssetManager assetManager =contexto.getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }


}
