package it.richkmeli.richkware.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import it.richkmeli.richkware.service.monitor.ScanAppUsageThread;


public class ThreadManager extends Service {
    private ScanAppUsageThread scanningAppThread;
    private final int timeSleepScanningApp = 3000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, final int flags, int startId) {
        scanningAppThread = new ScanAppUsageThread(getApplicationContext(), timeSleepScanningApp);
        scanningAppThread.start();

        // managed by the system, it is recreated if it will be destroyed
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (scanningAppThread != null) {
            scanningAppThread.isEnded = true;
        }
        super.onDestroy();
    }


}



