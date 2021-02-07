package it.richkmeli.richkware.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import it.richkmeli.richkware.service.location.LocationService;
import it.richkmeli.richkware.service.monitor.ScanAppUsageFgService;
import it.richkmeli.richkware.util.Logger;

public class ServiceManager {
    public static Intent[] backgroundServiceList;
    public static Intent[] foregroundServiceList;


    private static void init(Context context) {
        backgroundServiceList =
                new Intent[]{new Intent(context, ThreadManager.class),
                        new Intent(context, LocationService.class)};
        foregroundServiceList =
                new Intent[]{new Intent(context, ScanAppUsageFgService.class)};
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

    public static void startServiceIfNotAlive(Context context, Class service) {
        boolean alive = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service != null) {
                if (serviceInfo.service.getClassName().compareTo(service.getName()) == 0) {
                    Logger.info("service is alive");
                    alive = true;
                } else {
                    Logger.info("service is stopped");
                }
            }
        }

        if (!alive) {
            Intent serviceIntent = new Intent(context, service);
            context.startService(serviceIntent);
        }

    }

}
