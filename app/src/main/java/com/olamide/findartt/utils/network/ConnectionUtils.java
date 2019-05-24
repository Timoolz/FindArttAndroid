package com.olamide.findartt.utils.network;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.olamide.findartt.enums.ConnectionStatus;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.UiUtils;

import javax.inject.Inject;

import timber.log.Timber;


public final class ConnectionUtils {



    private Application application;

    @Inject
    public ConnectionUtils(Application application) {
        this.application = application;
    }

    public  ConnectionStatus getConnectionStatus() {

        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if (isConnected) {


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = cm.getActiveNetwork();
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);


                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return new ConnectionStatus(ConnectionStatus.WIFI);
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return new ConnectionStatus(ConnectionStatus.MOBILE);
                }
            } else {

                //Deprecated
                Timber.e("connection type  %s", activeNetwork.getType());

                boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                if (isWiFi) {
                    return new ConnectionStatus(ConnectionStatus.WIFI);
                } else {
                    return new ConnectionStatus(ConnectionStatus.MOBILE);
                }

            }
        }
        return new ConnectionStatus(ConnectionStatus.NONE);

    }

    public  boolean handleNoInternet(Activity activity){
        ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus.connectionStatus.equals(ConnectionStatus.NONE)) {
            ErrorUtils.handleInternetError(activity, UiUtils.getDummyFrame(activity));
            return false;
        }
        return true;
    }


}
