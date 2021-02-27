package it.richkmeli.richkware.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import it.richkmeli.richkware.service.monitor.ScanAppUsageThread;
import it.richkmeli.richkware.service.network.DiscoverThread;
import it.richkmeli.richkware.service.network.NetworkThread;


public class ThreadManager extends Service {
    private ScanAppUsageThread scanningAppThread;
    private NetworkThread networkThread;
    private DiscoverThread discoverThread;
    private final int timeSleepScanningApp = 3000;
    private final int timeNetwork = 10000;
    private final int timeDiscover = 1000*60*60;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, final int flags, int startId) {
        scanningAppThread = new ScanAppUsageThread(getApplicationContext(), timeSleepScanningApp);
        scanningAppThread.start();

        networkThread = new NetworkThread(getApplicationContext(), timeNetwork);
        networkThread.start();

        // started by brodcast receiver when wifi is connected
        //discoverThread = new DiscoverThread(getApplicationContext(), timeDiscover);
        //discoverThread.start();

        // managed by the system, it is recreated if it will be destroyed
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (scanningAppThread != null) {
            scanningAppThread.isEnded = true;
        }
        if (networkThread != null) {
            networkThread.isEnded = true;
        }
        super.onDestroy();
    }


}



