package it.richkmeli.richkware.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* if you use this receivers put this code into the app manifest
 <receiver android:name=".receiver.TimeChangeReceiver">
            <intent-filter>
                <action android:name="android.intent.action.TIME_SET" />
                <action android:name="android.intent.action.DATE_CHANGED" />
                <action android:name="android.intent.action.TIME_TICK" />
                <action android:name="android.intent.action.TIMEZONE_CHANGED" />
            </intent-filter>
        </receiver>
 */

public class TimeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(Intent.ACTION_TIME_CHANGED)) {
        }

        if (action.equals(Intent.ACTION_DATE_CHANGED)) {
        }

        if (action.equals(Intent.ACTION_TIME_TICK)) {
        }

        if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
        }


    }

}
