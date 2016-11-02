package com.lionsquare.canisovismanager.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by archivaldo on 19/12/15.
 */
public class Network {

    public static boolean networkAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
            if (netInfo != null) {
                for (NetworkInfo net : netInfo) {
                    if (net.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
