package com.andres18160gmail.arduinobluetooth.Clases;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.andres18160gmail.arduinobluetooth.Entidades.EnPinControl;
import com.andres18160gmail.arduinobluetooth.Entidades.Temperatura;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ANDRE on 03/02/2018.
 */

public class MiAsyncTask extends AsyncTask<BluetoothDevice, EnPinControl, Void>
        {
private static final String TAG = "MiAsyncTask";

//Identificador unico universal del puerto bluetooth en android (UUID)
private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";
private EnPinControl temperatura = new EnPinControl();
private BluetoothSocket mSocket = null;
private BufferedReader mBufferedReader = null;
private MiCallback callback;
private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
private boolean recibiendo = false;
private InputStream aStream = null;
private InputStreamReader aReader = null;
private int contadorConexiones = 0;
private  OutputStream mmOutStream;

public interface MiCallback {
    void onTaskCompleted();

    void onCancelled();

    void onPinControlUpdate(EnPinControl p);
}

    public MiAsyncTask(MiCallback CALLBACK) {
        callback = CALLBACK;

    }
    @Override
    protected Void doInBackground(BluetoothDevice... devices) {

        final BluetoothDevice device = devices[0];

//Realizamos la conexion al disp.blueetoth. A veces la conexion falla aunque el dispositivo
//este presente. Asi que si falla, y la tarea no ha sido cancelada, lo reintentamos.
        while (!isCancelled()) {
            if (!recibiendo) {
                recibiendo = conectayRecibeBT(device);
            }
        }

        cierra();
        return null;
    }

    private boolean conectayRecibeBT(BluetoothDevice device) {
//Abrimos la conexión con el dispositivo.
        boolean ok = true;

        try {
            contadorConexiones++;

            mSocket = device.createRfcommSocketToServiceRecord(getSerialPortUUID());
            mSocket.connect();
            aStream = mSocket.getInputStream();
            mmOutStream=mSocket.getOutputStream();
            aReader = new InputStreamReader(aStream);
            mBufferedReader = new BufferedReader(aReader);

            temperatura.setInformacion("Sin datos...");
            publishProgress(temperatura);
/*Mientras no se cancele la tarea asincrona (cuando se destruya la actividad)
se interroga al canal de comunicación por la temperatura*/

            while (!isCancelled()) {

                try {

                    String aString = mBufferedReader.readLine();
                    if ((aString != null) && (!aString.isEmpty())) {
//Instante de tiempo en que recuperamos un dato.
                        temperatura.setInformacion(sdf.format(new Date()));

//Recibimos la información en una cadena de la forma NombreDispositivo,XX
//donde XX es la temperatura.
                        try {
                            Log.e(TAG, "...Data to Receb: " + aString);
                            String s[] = aString.split(",");
                            temperatura.setPin(s[0]);
                            temperatura.setInformacion(s[1]);
                            publishProgress(temperatura);
                        } catch (Exception e) {
//Si falla el formateo de los datos, no hacemos nada. Mostramos la excepción en la consola para
//observar el error.
                            e.printStackTrace();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

//Una vez la tarea se ha cancelado, cerramos la conexión con el dispositivo bluetooth.
            temperatura.setInformacion("Cerrando conexion BT");

        } catch (IOException e) {
            ok = false;
            e.printStackTrace();
            temperatura.setInformacion("Error conectando con dispositivo bt, reintento " + contadorConexiones + "... Si este error se repite, reinicie el arduino.");
            publishProgress(temperatura);
            cierra();

        }
        return ok;
    }

    private void cierra() {
        close(mBufferedReader);
        close(aReader);
        close(aStream);
        close(mSocket);
    }
    public void write(String message) {
        Log.d(TAG, "...Data to send: " + message + "...");
        byte[] msgBuffer = message.getBytes();
        try {
            mmOutStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
        }
    }

    private UUID getSerialPortUUID() {
        return UUID.fromString(UUID_SERIAL_PORT_PROFILE);
    }

    private void close(Closeable aConnectedObject) {
        if (aConnectedObject == null) return;
        try {
            aConnectedObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aConnectedObject = null;
    }

    @Override
    protected void onProgressUpdate(EnPinControl... values) {
        super.onProgressUpdate(values);
        callback.onPinControlUpdate(values[0]);
    }

    @Override
    protected void onCancelled() {
        callback.onCancelled();
    }


}