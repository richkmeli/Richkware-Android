package it.richkmeli.richkware.receiver;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class AlarmServicesManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean serviceAppUsageServiceRun = false;
        boolean serviceLocationServiceRun = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service != null) {
                if (service.service.getClassName().compareTo("pathTOsevice.AppUsageService") == 0) {
                    serviceAppUsageServiceRun = true;
                } else if (service.service.getClassName().compareTo("pathTOsevice.LocationService") == 0) {
                    serviceLocationServiceRun = true;
                }
            }
        }

        if (!serviceAppUsageServiceRun) {
            SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
            if (setting.getBoolean("AppUsageEnabled", false)) {
                //Intent serviceIntent = new Intent(context, AppUsageService.class);
                //context.startService(serviceIntent);
            }
        }

        if (!serviceLocationServiceRun) {
            SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
            if (setting.getBoolean("AppUsageEnabled", false)) {
                //Intent locationIntent = new Intent(context, LocationService.class);
                //context.startService(locationIntent);
            }

        }
    }

    public static void setAlarmFormASM(Context context) {
        int intervalTimeForNextSend = 1000 * 60 * 20;
        Intent alarmServicesManager = new Intent(context, AlarmServicesManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmServicesManager, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), intervalTimeForNextSend, sender);
    }

}
