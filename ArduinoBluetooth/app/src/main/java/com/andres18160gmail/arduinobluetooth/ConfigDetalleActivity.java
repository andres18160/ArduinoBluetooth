package com.andres18160gmail.arduinobluetooth;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Adaptadores.TipoDispositivoAdapter;
import com.andres18160gmail.arduinobluetooth.Datos.TablaDispositivos;
import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.Entidades.EnTipo;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class ConfigDetalleActivity extends AppCompatActivity {
    EnDispositivo dispositivo=new EnDispositivo();
    EnTipo tipo=new EnTipo();
    Spinner spinnerPins,spinnerTipos;
    String[] itemsPin,itemsTipo;
    ArrayList<EnTipo> ListaTipos =new ArrayList<EnTipo>();
    private TipoDispositivoAdapter adapterTipos;
    private TablaDispositivos cdDispositivo=new TablaDispositivos(this);
    private TextInputLayout input_nombre;
    CircularProgressButton btnGuardar,btnActualizar,btnEliminar;
    EditText txtNombre;
    RadioButton checkDigital,checkAnalogo;

    private Vibrator vib;
    Animation animShake;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_detalle);
        btnGuardar=(CircularProgressButton)findViewById(R.id.btnGuardar);
        btnActualizar=(CircularProgressButton)findViewById(R.id.btnActualizar);
        btnEliminar=(CircularProgressButton)findViewById(R.id.btnEliminar);
        checkDigital=(RadioButton)findViewById(R.id.digital);
        checkAnalogo=(RadioButton)findViewById(R.id.analogo);
        spinnerPins=(Spinner)findViewById(R.id.spinnerPins);
        spinnerTipos=(Spinner)findViewById(R.id.spinnerTipos);
        txtNombre=(EditText)findViewById(R.id.txtNombre);
        input_nombre=(TextInputLayout)findViewById(R.id.input_nombre);
        CargarLista();
        animShake= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        if(extras!=null){
            if(extras.containsKey("id")){
                String Id=extras.getString("id");
                dispositivo=cdDispositivo.BuscarDispositivo(Id);
                if(dispositivo!=null){
                    btnGuardar.setVisibility(View.INVISIBLE);
                    txtNombre.setText(dispositivo.getNombre());
                   spinnerTipos.setSelection(getIndex(spinnerTipos,dispositivo.getFoto()));
                    spinnerPins.setSelection(getIndex(spinnerPins,dispositivo.getPin()));
                    if(dispositivo.getTipo().equalsIgnoreCase("Digital")){
                        checkDigital.setChecked(true);
                    }else{
                        checkAnalogo.setChecked(true);
                    }

                }
            }
        }else{
            btnActualizar.setVisibility(View.INVISIBLE);
            btnEliminar.setVisibility(View.INVISIBLE);
        }


        spinnerTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    dispositivo.setFoto(parent.getItemAtPosition(position).toString());
                }catch (Exception e){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    dispositivo.setPin(parent.getItemAtPosition(position).toString());
                }catch (Exception e){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void Guardar(View view){
        if(ValidarControles()){
            AsyncTask<String,String,String> demoCargando=new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... strings) {
                    try{
                        Thread.sleep(1500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    return "ok";

                }

                @Override
                protected void onPostExecute(String s) {
                    if(s.equals("ok")){
                        btnGuardar.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                        if(cdDispositivo.GuardarDispositivo(dispositivo)){
                            MensajeToast("Dispositivo Registrado");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            onBackPressed();
                        }else{
                            MensajeToast("Ocurrio un error insertando el registro");
                        }
                    }
                }
            };
            btnGuardar.startAnimation();
            demoCargando.execute();

        }
    }
    public void Eliminar(View view){
        if(dispositivo!=null){
            AsyncTask<String,String,String> demoCargando=new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... strings) {
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    return "ok";

                }

                @Override
                protected void onPostExecute(String s) {
                    if(s.equals("ok")){
                        btnEliminar.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                        if(cdDispositivo.EliminarDispositivo(""+dispositivo.getId())){
                            MensajeToast("Dispositivo Eliminado");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            onBackPressed();
                        }else{
                            MensajeToast("Ocurrio un error realizando la operación");
                        }
                    }
                }
            };
            btnEliminar.startAnimation();
            demoCargando.execute();
        }
    }
    public void Actualizar(View view){
        if(ValidarControles()){
            AsyncTask<String,String,String> demoCargando=new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... strings) {
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    return "ok";

                }

                @Override
                protected void onPostExecute(String s) {
                    if(s.equals("ok")){
                        if(cdDispositivo.ActualizarDispositivo(dispositivo)){
                            btnActualizar.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                            MensajeToast("Dispositivo Actualizado");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            onBackPressed();
                        }else{
                            MensajeToast("Ocurrio un error realizando la operación");
                        }
                    }
                }
            };
            btnActualizar.startAnimation();
            demoCargando.execute();
        }
    }
    private void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    private boolean ValidarControles(){

        try{

            if(dispositivo.getTipo()==null){
                dispositivo.setTipo("Digital");
            }
            if(txtNombre.getText().toString().trim().isEmpty()){
                input_nombre.setErrorEnabled(true);
                input_nombre.setError(getResources().getText(R.string.err_msg_nombre));
                txtNombre.setError(getResources().getText(R.string.err_msg_requerido));
                input_nombre.setAnimation(animShake);
                input_nombre.startAnimation(animShake);
                vib.vibrate(120);
                requestFocus(txtNombre);
                return false;
            }
            input_nombre.setErrorEnabled(false);
            dispositivo.setNombre(txtNombre.getText().toString());

            return true;
        }catch (Exception e){
            Log.e("Error",e.getMessage().toString());
            return false;
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.digital:
                if (checked)
                    dispositivo.setTipo("Digital");
                    break;
            case R.id.analogo:
                if (checked)
                    dispositivo.setTipo("Analogo");
                    break;
        }
    }

    private void CargarLista() {
        itemsPin=getResources().getStringArray(R.array.Pins);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsPin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPins.setAdapter(adapter);

        itemsTipo=getResources().getStringArray(R.array.TiposImagenes);
        ArrayAdapter<String> adapterTipos=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsTipo);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipos.setAdapter(adapterTipos);



    }
    private void requestFocus(View view){
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private int getIndex(Spinner spinner, String id)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(id)){
                index = i;
                break;
            }
        }
        return index;
    }


    @Override
    public void onBackPressed(){
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }
}
