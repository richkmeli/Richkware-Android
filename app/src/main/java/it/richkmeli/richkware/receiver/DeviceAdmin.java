package it.richkmeli.richkware.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

import androidx.annotation.NonNull;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;

public class DeviceAdmin extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT,"Admin Enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT,"Admin disable requested");
        return "Do you want to disable it?";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT,"Admin Disabled");

    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle userHandle) {
        NotificationManager.notify(context, NotificationType.TOAST_SHORT,"Password Changed");
    }

}
