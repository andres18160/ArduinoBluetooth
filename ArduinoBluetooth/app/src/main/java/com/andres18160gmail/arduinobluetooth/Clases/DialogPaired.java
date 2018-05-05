package com.andres18160gmail.arduinobluetooth.Clases;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Camilo on 15/03/2018.
 */

public class DialogPaired extends DialogFragment {


    public interface SelectDevideDialog {
        void onFinisSelectDevideDialog(String inputText);
    }

    private List<BluetoothDevice> devices;

    public void setDevices(List<BluetoothDevice> devices){
        this.devices=devices;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder buider=new AlertDialog.Builder(getActivity());

        CharSequence[] titles=new CharSequence[devices.size()];
        for (int i=0;i<devices.size();i++){
            titles[i]=devices.get(i).getName();
        }
        buider.setTitle("Dispositivos Vinculados").setItems(titles, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"dispositivo pulsado:"+devices.get(i).getName(),Toast.LENGTH_SHORT).show();
                SelectDevideDialog listener = (SelectDevideDialog) getTargetFragment();
                listener.onFinisSelectDevideDialog(devices.get(i).getName());
                dismiss();
            }
        });

        return buider.create();
    }
}
