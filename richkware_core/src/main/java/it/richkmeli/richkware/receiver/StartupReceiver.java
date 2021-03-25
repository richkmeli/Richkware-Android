package it.richkmeli.richkware.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Richkware: StartupReceiver");

        // TODO start services

        // call AlarmServicesManager periodically
        AlarmServicesManager.setAlarmFormASM(context);
    }
}
