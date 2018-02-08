package com.andres18160gmail.arduinobluetooth;

import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andres18160gmail.arduinobluetooth.Adaptadores.DispositivosControlAdapter;
import com.andres18160gmail.arduinobluetooth.Clases.MiAsyncTask;
import com.andres18160gmail.arduinobluetooth.Datos.TablaDispositivos;
import com.andres18160gmail.arduinobluetooth.Entidades.EnDispositivo;
import com.andres18160gmail.arduinobluetooth.Entidades.EnPinControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Principal extends Fragment implements DispositivosControlAdapter.onCheckertoggle,DispositivosControlAdapter.onSeekBar {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String NOMBRE_DISPOSITIVO_BT = "HC-06";//Nombre de neustro dispositivo bluetooth.
    private static final String TAG = "bluetooth2";
    private int progressChangedValue = 0;
    private  View v;
    ArrayList<EnDispositivo> listDatos;
    RecyclerView recycler;
    TablaDispositivos cdDispositivos;
    DispositivosControlAdapter adapter;
    EnDispositivo dispositivo=new EnDispositivo();
    TextView txtInformacion;
    BluetoothDevice arduino = null;


    Handler h;

    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;
    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // MAC-address of Bluetooth module (you must edit this line)
    private ObjectAnimator anim;
    private Handler handler = new Handler();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_principal, container, false);
        cdDispositivos=new TablaDispositivos(v.getContext());
        recycler=(RecyclerView)v.findViewById(R.id.RecyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,false));
        txtInformacion=(TextView)v.findViewById(R.id.txtInformacion);
        listDatos=cdDispositivos.GetListaDispositivos();
        if(listDatos!=null){
            adapter=new DispositivosControlAdapter(listDatos);
            adapter.setOnchecktoggle(this);
            adapter.setOnSeekBar(this);
            recycler.setAdapter(adapter);
        }

        recycler.setVisibility(View.INVISIBLE);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        Log.d(TAG, "Informacion entrante");
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear
                            txtInformacion.setText(sbprint);            // update TextView
                        }
                        Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
        //descubrirDispositivosBT();
        return v;
    }
    private void descubrirDispositivosBT() {

/*
Este método comprueba si nuestro dispositivo dispone de conectividad bluetooh.
En caso afirmativo, si estuviera desctivada, intenta activarla.
En caso negativo presenta un mensaje al usuario y sale de la aplicación.
*/
//Comprobamos que el dispositivo tiene adaptador bluetooth
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                            MensajeToast("Comprobando bluetooth");


                            if (mBluetoothAdapter != null) {

//El dispositivo tiene adapatador BT. Ahora comprobamos que bt esta activado.

                                if (mBluetoothAdapter.isEnabled()) {
//Esta activado. Obtenemos la lista de dispositivos BT emparejados con nuestro dispositivo android.

                                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();//Si hay dispositivos emparejados
                                    if (pairedDevices.size()> 0) {
/*
Recorremos los dispositivos emparejados hasta encontrar el
adaptador BT del arduino, en este caso se llama HC-06
*/
                                        for (BluetoothDevice devices : pairedDevices) {
                                            if (devices.getName().equalsIgnoreCase(NOMBRE_DISPOSITIVO_BT)) {
                                                arduino = devices;
                                            }
                                        }
                                        if (arduino != null) {

                                            try {
                                                btSocket = createBluetoothSocket(arduino);
                                            } catch (IOException e) {
                                                MensajeToast("In onResume() and socket create failed: " + e.getMessage() + ".");
                                            }
                                            // Discovery is resource intensive.  Make sure it isn't going on
                                            // when you attempt to connect and pass your message.
                                            btAdapter.cancelDiscovery();
                                            // Establish the connection.  This will block until it connects.
                                            Log.d(TAG, "...Connecting...");
                                            try {

                                                MensajeToast("...Connecting...");
                                                btSocket.connect();
                                                txtInformacion.setText("Conectado");
                                                Log.d(TAG, "....Connection ok...");
                                                recycler.setVisibility(View.VISIBLE);
                                            } catch (IOException e) {
                                                try {
                                                    MensajeToast("...No se logro realizar comunicación con el dispositivo...");
                                                    btSocket.close();
                                                } catch (IOException e2) {
                                                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                                                }
                                            }

                                            // Create a data stream so we can talk to server.
                                            Log.d(TAG, "...Create Socket...");
                                            mConnectedThread = new ConnectedThread(btSocket);
                                            mConnectedThread.start();
                                        } else {
//No hemos encontrado nuestro dispositivo BT, es necesario emparejarlo antes de poder usarlo.
//No hay ningun dispositivo emparejado. Salimos de la app.
                                            Toast.makeText(getContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                                           // txtInformacion.setText("No hay dispositivos emparejados, por favor, empareje el arduino");
                                        }
                                    } else {
//No hay ningun dispositivo emparejado. Salimos de la app.
                                        Toast.makeText(getContext(), "No hay dispositivos emparejados, por favor, empareje el arduino", Toast.LENGTH_LONG).show();
                                        //txtInformacion.setText("No hay dispositivos emparejados, por favor, empareje el arduino");

                                    }
                                } else {
                                    muestraDialogoConfirmacionActivacion();
                                }
                            } else {
// El dispositivo no soporta bluetooth. Mensaje al usuario y salimos de la app
                                Toast.makeText(getContext(), "El dispositivo no soporta comunicación por Bluetooth", Toast.LENGTH_LONG).show();
                            }
    }


    private void muestraDialogoConfirmacionActivacion() {
        new AlertDialog.Builder(getContext())
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
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("INFO=", "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    @Override
    public void onResume() {
        super.onResume();
          descubrirDispositivosBT();
    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
    private void MensajeToast(final String mensaje){
        Toast toast = Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    }



    @Override
    public void itemProgressChanged(View view, int progress, int position) {
        progressChangedValue=progress;
    }

    @Override
    public void itemStartTrackingTouch(View view, int position) {
        dispositivo=adapter.getItem(position);
        mConnectedThread.write(dispositivo.getPin()+":"+progressChangedValue);
    }

    @Override
    public void itemCheck(View view, int position,boolean isChecked) {
        dispositivo=adapter.getItem(position);
        if (isChecked) {
            mConnectedThread.write(dispositivo.getPin()+":1");
        } else {
            mConnectedThread.write(dispositivo.getPin()+":0");
        }
    }

    @Override
    public void itemStopTrackingTouch(View view, int position) {
        dispositivo=adapter.getItem(position);
        MensajeToast("Stop Progreso="+progressChangedValue);
        mConnectedThread.write(dispositivo.getPin()+":"+progressChangedValue);
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}
