package it.richkmeli.richkware.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import it.richkmeli.richkware.BuildConfig;
import it.richkmeli.richkware.R;
import it.richkmeli.richkware.service.location.LocationService;
import it.richkmeli.richkware.service.monitor.ScanAppUsageFgService;

public class ServiceManager {
    public static Intent[] backgroundServiceList;
    public static Intent[] foregroundServiceList;


    private static void init(Context context) {
        backgroundServiceList = new Intent[]{new Intent(context, ThreadManager.class),
                new Intent(context, LocationService.class)};
        foregroundServiceList = new Intent[]{new Intent(context, ScanAppUsageFgService.class)};
    }

    public static void startBackgroundServices(Context context) {
        init(context);
        for (Intent service : backgroundServiceList) {
            context.startService(service);
        }
    }
    public static void stopBackgroundServices(Context context) {
        init(context);
        for (Intent service : backgroundServiceList) {
            context.stopService(service);
        }
    }

    // when you need to perform a task that is noticeable by the user even when they're not directly interacting with the app
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startForegroundServices(Context context) {
        init(context);
        for (Intent service : foregroundServiceList) {
            context.startForegroundService(service);
        }
    }
    public static void stopForegroundServices(Context context) {
        init(context);
        for (Intent service : foregroundServiceList) {
            context.stopService(service);
        }
    }
}
