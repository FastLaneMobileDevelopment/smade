package io.bega.servicebase.service;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.otto.Bus;


/**
 * Created by usuario on 14/08/15.
 */
public class ConnectivityUpdateReceiver  extends BroadcastReceiver {

    Bus bus;



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeWifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isConnectedTelephone = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();

        boolean isConnectedWifi = activeWifiInfo != null && activeWifiInfo.isConnectedOrConnecting();

        if (isConnectedTelephone || isConnectedWifi)
        {

        }

        else{


        }

    }
}
