package com.andres18160gmail.arduinobluetooth.Clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Camilo on 15/03/2018.
 */

public class DialogPaired extends DialogFragment {
    private SelectDialog mlistener;
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

                try {
                    Log.d("ERROR_DIALOG", devices.get(i).getName());

                    mlistener.onFinisSelectDialog(devices.get(i).getName());
                    dismiss();
                } catch (Exception e) {
                    Log.d("ERROR_DIALOG", e.getMessage());
                    //e.printStackTrace();
                }

            }
        });

        return buider.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mlistener = (SelectDialog) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement dialogDoneistener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener = null;
    }
    public interface SelectDialog {
        void onFinisSelectDialog(String inputText);
    }
}
