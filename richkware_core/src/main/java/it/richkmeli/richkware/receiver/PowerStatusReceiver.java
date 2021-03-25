package it.richkmeli.richkware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;

public class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Richkware: PowerConnectionReceiver: " + action);

        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            StorageManager.save(context, StorageKey.POWER_STATUS, "connected");
        }

        if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            StorageManager.save(context, StorageKey.POWER_STATUS, "disconnected");
        }

        if (action.equals(Intent.ACTION_BATTERY_LOW)) {
            StorageManager.save(context, StorageKey.BATTERY_STATUS, "low");
        }

        if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
            StorageManager.save(context, StorageKey.BATTERY_STATUS, "ok");
        }
    }
}
