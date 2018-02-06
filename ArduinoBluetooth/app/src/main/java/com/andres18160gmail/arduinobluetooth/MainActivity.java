package com.andres18160gmail.arduinobluetooth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Clases.MiAsyncTask;


import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtInformacion;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String NOMBRE_DISPOSITIVO_BT = "HC-06";//Nombre de neustro dispositivo bluetooth.
    private MiAsyncTask tareaAsincrona;
    BluetoothDevice arduino = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



       // getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,new Principal()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
/* El metodo on resume es el adecuado para inicialzar todos aquellos procesos que actualicen la interfaz de usuario
Por lo tanto invocamos aqui al método que activa el BT y crea la tarea asincrona que recupera los datos*/

        super.onResume();
      //  descubrirDispositivosBT();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Boolean FragmentoSeleccionado=false;
        android.support.v4.app.Fragment fragment=null;

        if (id == R.id.nav_manage) {
            fragment=new configDispositvos();
            FragmentoSeleccionado=true;

        }
        if (id == R.id.nav_control) {
            fragment=new Principal();
            FragmentoSeleccionado=true;

            if(arduino!=null){

            }else{
                MensajeToast("Debes conectarte primero a un dispositivo");
            }

        }
        if (id == R.id.nav_Conection) {
            descubrirDispositivosBT();

        }
        if(FragmentoSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,fragment).commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                MensajeToast("Obteniendo dispositivos emparejados, espere...");
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
                        //tareaAsincrona = new MiAsyncTask(getApplicationContext());
                        //tareaAsincrona.execute(arduino);
                        MensajeToast("Conectado!");
                    } else {
//No hemos encontrado nuestro dispositivo BT, es necesario emparejarlo antes de poder usarlo.
//No hay ningun dispositivo emparejado. Salimos de la app.
                        Toast.makeText(getApplicationContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                        MensajeToast("No hay dispositivos emparejados, por favor, empareje el arduino");
                    }
                } else {
//No hay ningun dispositivo emparejado. Salimos de la app.
                    Toast.makeText(getApplicationContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                    MensajeToast("No hay dispositivos emparejados, por favor, empareje el arduino");

                }
            } else {
                muestraDialogoConfirmacionActivacion();
            }
        } else {
// El dispositivo no soporta bluetooth. Mensaje al usuario y salimos de la app
            Toast.makeText(getApplicationContext(), "El dispositivo no soporta comunicación por Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private void muestraDialogoConfirmacionActivacion() {
        new AlertDialog.Builder(this)
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
    private void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
