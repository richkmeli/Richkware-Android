package it.richkmeli.richkware.service.monitor;


import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.permission.PermissionManager;
import it.richkmeli.richkware.system.application.ForegroundApp;
import it.richkmeli.richkware.util.Logger;

public class ScanAppUsageThread extends Thread {
    public Handler handler;
    public boolean isEnded = false;
    private final Context context;
    private int timeSleep;


    public ScanAppUsageThread(Context contextTmp, int timeSleepTmp) {
        context = contextTmp;
        timeSleep = timeSleepTmp;
    }

    @Override
    public void run() {

        handler = new Handler(context.getMainLooper());
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        ForegroundApp foreApp = new ForegroundApp(context);

        while (!isEnded) {
            // if screen is unlocked
            if (!myKM.inKeyguardRestrictedInputMode()) {

                if (PermissionManager.checkOverlayPermission(context)) {
                    final String foregroundApp = foreApp.getForegroundApp();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //NotificationManager.notify(context, NotificationType.TOAST_SHORT, foregroundApp);
                            // TODO save into file?
                        }
                    });
                } else {
                    String s = "Overlay Permission not granted.";
                    Logger.error(s);
                    NotificationManager.notify(context, NotificationType.TOAST_SHORT, s);
                }

            }
            try {
                sleep(timeSleep);
            } catch (InterruptedException e) {
                Logger.error(e);
            }

        }
    }

}
