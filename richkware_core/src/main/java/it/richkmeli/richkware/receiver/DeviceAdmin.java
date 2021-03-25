package it.richkmeli.richkware.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;

public class DeviceAdmin extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Admin Enabled");
        StorageManager.save(context, StorageKey.DEVICE_ADMIN, "enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Admin disable requested");
        return "Do you want to disable it?";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Admin Disabled");
        StorageManager.save(context, StorageKey.DEVICE_ADMIN, "disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle userHandle) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT, "Password Changed");
    }

}
