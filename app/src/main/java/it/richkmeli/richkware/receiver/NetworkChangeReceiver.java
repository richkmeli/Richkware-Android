package it.richkmeli.richkware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.network.NetworkManager;
import it.richkmeli.richkware.service.network.DiscoverThread;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_VPN;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        NetworkInfo networkInfo = NetworkManager.getNetworkInfo(context);
        if (networkInfo.isConnected()) {
            NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Network Available Do operations");
            switch (networkInfo.getType()) {
                case TYPE_WIFI:
                    StorageManager.save(context, StorageKey.INTERNET_CONNECTION, "WIFI");
                    DiscoverThread discoverThread = new DiscoverThread(context);
                    discoverThread.start();
                    break;
                case TYPE_VPN:
                case TYPE_MOBILE:
                default:
                    StorageManager.save(context, StorageKey.INTERNET_CONNECTION, "OTHER");
                    break;
            }
        } else {
            StorageManager.save(context, StorageKey.INTERNET_CONNECTION, "NONE");
        }
    }

}