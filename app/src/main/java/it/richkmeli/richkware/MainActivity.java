package it.richkmeli.richkware;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.component.smartNotification.SmartNotification;
import it.richkmeli.richkware.permission.PermissionManager;
import it.richkmeli.richkware.service.ServiceManager;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.util.DeviceInfo;
import it.richkmeli.richkware.util.Logger;

public class MainActivity extends AppCompatActivity {

    private TextView device_id;
    private TextView installation_id;
    private Button enableNotificationButton;
    private Button disableNotificationButton;
    private Button permissionButton;
    private Button systemWrite;
    private Button systemRead;
    private Button startForegroundServices;
    private Button stopForegroundServices;
    private Button startBackgroundServices;
    private Button stopBackgroundServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeButtons();
        initializeTextview();

        checkSavedDeviceID();
    }

    private void checkSavedDeviceID() {
        String deviceID = DeviceInfo.getDeviceID(this);
        String savedDeviceID = StorageManager.read(this, StorageKey.DEVICE_ID);
        if (!deviceID.equalsIgnoreCase(savedDeviceID)) {
            Toast.makeText(this, "DeviceID not equal", Toast.LENGTH_LONG).show();
        }
        StorageManager.save(this, StorageKey.DEVICE_ID, deviceID);
    }

    private void initializeTextview() {
        device_id = findViewById(R.id.device_id);
        installation_id = findViewById(R.id.installation_id);

        // set text view value
        String deviceID = DeviceInfo.getDeviceID(this);
        device_id.setText(deviceID);

        String installationID = DeviceInfo.getInstallationID(this);
        installation_id.setText(installationID);

    }

    private void initializeButtons() {
        enableNotificationButton = findViewById(R.id.enableNotification);
        disableNotificationButton = findViewById(R.id.disableNotification);
        permissionButton = findViewById(R.id.requestPermission);
        systemWrite = findViewById(R.id.system_write);
        systemRead = findViewById(R.id.system_read);
        startForegroundServices = findViewById(R.id.start_fg_services);
        stopForegroundServices = findViewById(R.id.stop_fg_services);
        startBackgroundServices = findViewById(R.id.start_bg_services);
        stopBackgroundServices = findViewById(R.id.stop_bg_services);

        // set button listener
        enableNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Enabling Notification");
                SmartNotification.show(getApplicationContext());
            }
        });

        disableNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Disabling Notification");
                SmartNotification.cancel(getApplicationContext());
            }
        });

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Requesting permission");

                Activity host = (Activity) view.getContext();
                try {
                    PermissionManager.checkPermissions(host);
                } catch (PackageManager.NameNotFoundException e) {
                    Logger.error(e);
                }

                PermissionManager notificationPermissionManager = new PermissionManager(view.getContext());
                if (!PermissionManager.checkAppUsagePermission(view.getContext())) {
                    PermissionManager.showAppUsageDialogPermission(view.getContext());
                }
                if (!PermissionManager.checkOverlayPermission(view.getContext())) {
                    PermissionManager.showOverlayDialogPermission(view.getContext());
                }
            }
        });

        systemWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageManager.save(view.getContext(), StorageKey.TEST, "" + new Random().nextInt());
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "saved to file");
            }
        });

        systemRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = StorageManager.read(view.getContext(), StorageKey.TEST);
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, ("Read, " + StorageKey.TEST + ": " + s));
            }
        });

        startForegroundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ServiceManager.startForegroundServices(view.getContext());
                    NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Starting fg services");
                }
            }
        });

        stopForegroundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceManager.stopForegroundServices(view.getContext());
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Stopping fg services");
            }
        });

        startBackgroundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceManager.startBackgroundServices(view.getContext());
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Starting bg services");
            }
        });

        stopBackgroundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceManager.stopBackgroundServices(view.getContext());
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Stopping bg services");
            }
        });

    }

}