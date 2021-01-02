package it.richkmeli.richkware.service;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import java.util.LinkedList;

public class ForegroundApp {
    private static LinkedList<String> linkedListApp = new LinkedList<>();
    private Context context;
    static int i = 0; // TODO: RichkDEBUG

    public ForegroundApp(Context contextTmp) {
        context = contextTmp;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String foregroundApp() {
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

        //////////////////////////////////////////////// // TODO: RichkDEBUG
        i++;
        final LinkedList<String> a = linkedListApp;
        if (i % 3 == 0) {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!linkedListApp.isEmpty())
                        Toast.makeText(context, "appfore : " + a.getLast(), Toast.LENGTH_SHORT).show();
                }

            })
            ;/////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        return foregroundApp;
    }
}
