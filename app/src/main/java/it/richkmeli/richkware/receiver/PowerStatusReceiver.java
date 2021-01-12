package it.richkmeli.richkware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;

public class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        NotificationManager.notify(context, NotificationType.TOAST_SHORT,"Richkware: PowerConnectionReceiver: " + action);

        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
        }

        if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
        }

        if (action.equals(Intent.ACTION_BATTERY_LOW)) {
        }

        if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
        }
    }
}
