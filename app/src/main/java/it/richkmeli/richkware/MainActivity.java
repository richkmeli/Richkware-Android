package it.richkmeli.richkware;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import it.richkmeli.jframework.util.TypeConverter;
import it.richkmeli.richkware.component.smartNotification.SmartNotification;
import it.richkmeli.richkware.permission.NotificationPermissionManager;
import it.richkmeli.richkware.service.DeviceInfo;
import it.richkmeli.richkware.service.ForegroundApp;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.util.Logger;

public class MainActivity extends AppCompatActivity {

    private Button enableNotificationButton;
    private Button disableNotificationButton;
    private Button permissionButton;
    private Button systemWrite;
    private Button systemRead;
    private Button foregroundApp;
    private TextView device_id;
    private TextView installation_id;

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
        if(!deviceID.equalsIgnoreCase(savedDeviceID)){
            Toast.makeText(this, "DeviceID not equal!", Toast.LENGTH_LONG).show();
        }
        StorageManager.save(this,StorageKey.DEVICE_ID,deviceID);
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
        foregroundApp = findViewById(R.id.foreground_app);

        // set button listener
        enableNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("Enabling Notification");
                Toast.makeText(view.getContext(), "Enabling Notification", Toast.LENGTH_LONG).show();

                SmartNotification.show(getApplicationContext());
            }
        });

        disableNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("Disabling Notification");
                Toast.makeText(view.getContext(), "Disabling Notification", Toast.LENGTH_LONG).show();

                SmartNotification.cancel(getApplicationContext());
            }
        });

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("Requesting permission");
                Toast.makeText(view.getContext(), "Requesting permission", Toast.LENGTH_LONG).show();

                NotificationPermissionManager notificationPermissionManager = new NotificationPermissionManager(view.getContext());
                if (!notificationPermissionManager.checkAppUsagePermission()) {
                    notificationPermissionManager.showAppUsageDialogPermission();
                }
                if (!notificationPermissionManager.checkOverlayPermission()) {
                    notificationPermissionManager.showOverlayDialogPermission();
                }
            }
        });

        systemWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageManager.save(view.getContext(), StorageKey.TEST, "" + new Random().nextInt());
                Logger.i("saved");
                Toast.makeText(view.getContext(), "saved", Toast.LENGTH_SHORT).show();

            }
        });

        systemRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = StorageManager.read(view.getContext(), StorageKey.TEST);
                Logger.i("Read, " + StorageKey.TEST + ": " + s);
                Toast.makeText(view.getContext(), "Read, " + StorageKey.TEST + ": " + s, Toast.LENGTH_SHORT).show();
            }
        });

        foregroundApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundApp foregroundApp = new ForegroundApp(view.getContext());
                String app = foregroundApp.foregroundApp();
                Logger.i("ForegroundApp: " + app);
                Toast.makeText(view.getContext(), "ForegroundApp: " + app, Toast.LENGTH_SHORT).show();
            }
        });

    }

}