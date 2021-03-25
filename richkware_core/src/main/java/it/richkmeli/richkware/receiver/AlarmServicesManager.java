package it.richkmeli.richkware.receiver;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import it.richkmeli.richkware.service.ServiceManager;
import it.richkmeli.richkware.service.ThreadManager;
import it.richkmeli.richkware.service.location.LocationService;
import it.richkmeli.richkware.util.Logger;

public class AlarmServicesManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.info("AlarmServicesManager: onReceive");
        //ServiceManager.startServiceIfNotAlive(context, ScanAppUsageFgService.class);
        ServiceManager.startServiceIfNotAlive(context, LocationService.class);
        ServiceManager.startServiceIfNotAlive(context, ThreadManager.class);

    }


    public static void setAlarmFormASM(Context context) {
        int intervalTimeForNextSend = 1000 * 60 * 20;
        Intent alarmServicesManager = new Intent(context, AlarmServicesManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmServicesManager, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), intervalTimeForNextSend, sender);
    }

}
