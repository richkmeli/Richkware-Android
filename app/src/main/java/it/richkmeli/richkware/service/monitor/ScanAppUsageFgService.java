package it.richkmeli.richkware.service.monitor;


import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import it.richkmeli.richkware.BuildConfig;
import it.richkmeli.richkware.R;
import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.permission.PermissionManager;
import it.richkmeli.richkware.system.application.ForegroundApp;
import it.richkmeli.richkware.util.Logger;

public class ScanAppUsageFgService extends IntentService {
    private static String NOTIFY_ID="RICHKWARE";
    private static int FOREGROUND_ID=25;

    public ScanAppUsageFgService() {
        super("RICHKWARE");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.info("ScanAppUsageFgService - onHandleIntent");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FILL_IN_ACTION);

        Notification notification = new Notification.Builder(getApplicationContext(), NOTIFY_ID)
                .setContentTitle("Title")
                .setContentText("text")
                .setSmallIcon(R.drawable.ic_stat_ad)
                .setContentIntent(pendingIntent)
                .setTicker("ticker")
                .setPriority(Notification.PRIORITY_LOW)
                .build();

        // Notification ID cannot be 0.

        startForeground(FOREGROUND_ID, notification);
    }
}
