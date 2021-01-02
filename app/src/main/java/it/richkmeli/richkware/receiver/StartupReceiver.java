package it.richkmeli.richkware.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        if (setting.getBoolean("AppUsageEnabled", false)) {
            /*  Intent serviceIntent = new Intent(context, AppUsageService.class);
            context.startService(serviceIntent);

            Intent locationIntent = new Intent(context, LocationService.class);
            context.startService(locationIntent);*/
        }

        // call AlarmServicesManager periodically
        AlarmServicesManager.setAlarmFormASM(context);
    }
}
