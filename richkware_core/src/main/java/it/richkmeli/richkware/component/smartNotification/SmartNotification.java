package it.richkmeli.richkware.component.smartNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import it.richkmeli.richkware.R;


public class SmartNotification {
    private static final String SN_TAG = "SmartNotification";
    private static final int SN_ID = 50;

    private static Notification customNotification;
    private static NotificationManager notificationManager;
    private static NotificationChannel notificationChannel;
    private static String CHANNEL_ID = "RICHKWARE";


    public static void show(Context context, Class clazz) {
        init(context, clazz);


        if (notificationManager != null) {
            notificationManager.notify(SN_TAG, SN_ID, customNotification);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*title2 = findViewById(R.id.title2);

                    if (title2 != null) {

                        title2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "done", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Logger.error("NULL");
                    }*/
                    /*
                    Button test3 = findViewById(R.id.test);
                    if(test3 != null) {
                        test3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "done", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Logger.error("NULL");
                    }*/
                }
            }, 2000);

        }

    }


    public static void cancel(Context context, Class clazz) {
        init(context, clazz);

        notificationManager.cancel(SN_TAG, SN_ID);
    }


    private static void init(Context context, Class clazz) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (notificationChannel == null) {
            createChannelID();
        }
        if (customNotification == null) {
            build(context, clazz);
        }
    }


    private static void build(final Context context, Class clazz) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);


        try {

            // Get the layouts to use in the custom notification
            RemoteViews notificationLayout = createSnRemoteViews(context, clazz);
            RemoteViews notificationLayoutExpanded = createSnExpandedRemoteViews(context, clazz);


            customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    //.setCustomContentView(layout)
                    //.setContentTitle("")
                    //.setContentText("content")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    //.setCustomHeadsUpContentView(remoteViews)
                    .setContent(notificationLayoutExpanded)
                    //.addAction(R.drawable.com_facebook_close, "BUTTON 1", pi)
                    //  .setContentIntent(pi)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();


        } catch (Throwable t) {
            Toast.makeText(context, "Notification test failed ---" + t.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private static void createChannelID() {
        CharSequence name = CHANNEL_ID + " channel AD";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(false);
            // to remove sound decrease relevance/priority
            notificationChannel.setSound(null, null);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private static RemoteViews createSnExpandedRemoteViews(Context context, Class clazz) {

        // link layout
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.smart_notification_large);
        //contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        //contentView.setTextViewText(R.id.title, "Custom notification");
        //contentView.setTextViewText(R.id.text, "This is a custom layout");

        Uri uri = Uri.parse("http://google.com");
        Intent button1 = new Intent(Intent.ACTION_VIEW, uri);
        button1.setPackage("com.chrome.android");
        PendingIntent openUrl = PendingIntent.getActivity(context, 0, button1, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.sn_button1, openUrl);

        Intent openRichkwareMainActivity = new Intent(context, clazz);
        PendingIntent openApp = PendingIntent.getActivity(context, 0, openRichkwareMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.sn_button2, openApp);

        //TODO refresh, send intent to IntentService

        return contentView;
    }

    private static RemoteViews createSnRemoteViews(Context context, Class clazz) {

        // link layout
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.smart_notification);
        //contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        //contentView.setTextViewText(R.id.title, "Custom notification");
        //contentView.setTextViewText(R.id.text, "This is a custom layout");

        Uri uri = Uri.parse("http://google.com");
        Intent button1 = new Intent(Intent.ACTION_VIEW, uri);
        button1.setPackage("com.chrome.android");
        PendingIntent openUrl = PendingIntent.getActivity(context, 0, button1, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.sn_button1, openUrl);

        Intent openRichkwareMainActivity = new Intent(context, clazz);
        PendingIntent openApp = PendingIntent.getActivity(context, 0, openRichkwareMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.sn_button2, openApp);

        //TODO refresh, send intent to IntentService

        return contentView;
    }


}
