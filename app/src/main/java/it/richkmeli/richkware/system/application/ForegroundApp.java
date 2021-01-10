package it.richkmeli.richkware.system.application;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import java.util.LinkedList;
import java.util.List;

public class ForegroundApp {
    private static LinkedList<String> linkedListApp = new LinkedList<>();
    private Context context;
    static int i = 0; // TODO: RichkDEBUG

    public ForegroundApp(Context contextTmp) {
        context = contextTmp;
    }

    public String getForegroundApp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return foregroundApp();
        } else {
            return foregroundAppCompat();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String foregroundApp() {
        String foregroundApp = null;

        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long now = System.currentTimeMillis();
        UsageEvents eventList = usm.queryEvents(now - 1000 * 1000, now);

        if (eventList != null) {
            while (eventList.hasNextEvent()) {
                UsageEvents.Event e = new UsageEvents.Event();
                eventList.getNextEvent(e);
                if (e.getEventType() == 1) {
                    linkedListApp.add(e.getPackageName());
                }
            }
        }

        if (!linkedListApp.isEmpty()) {
            foregroundApp = linkedListApp.getLast();
        }

//        //////////////////////////////////////////////// // TODO: RichkDEBUG
//        i++;
//        final LinkedList<String> a = linkedListApp;
//        if (i % 3 == 0) {
//            Handler handler = new Handler(context.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (!linkedListApp.isEmpty()) {
//                        NotificationManager.notify(context, NotificationType.TOAST_SHORT, a.getLast());
//                    }
//                }
//
//            })
//            ;
//        }/////////////////////////////////////////////////////////////////////////////////////////////////////

        return foregroundApp;
    }

    private String foregroundAppCompat() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(5);
        String foregroundApp = null;

        for (ActivityManager.RunningAppProcessInfo currentApp : processes) {
            if (currentApp.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    currentApp.processName.compareTo("com.android.systemui") != 0 &&
                    currentApp.processName.compareTo("system") != 0 &&
                    currentApp.processName.compareTo("com.android.phone") != 0) {
                // compare with alternative method to improve the scan
                for (ActivityManager.RunningTaskInfo currentTask : tasks) {
                    if (currentApp.processName.compareTo(currentTask.topActivity.getPackageName()) == 0) {
                        foregroundApp = currentApp.processName;
                    }
                }
                if (foregroundApp == null)
                    // if comparison with other method was false, write anyway
                    foregroundApp = currentApp.processName;
            }
        }
        return foregroundApp;
    }
}
