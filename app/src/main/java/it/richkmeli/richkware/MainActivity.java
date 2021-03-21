package it.richkmeli.richkware;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.component.smartNotification.SmartNotification;
import it.richkmeli.richkware.network.NetworkManager;
import it.richkmeli.richkware.network.RichkwareCallback;
import it.richkmeli.richkware.permission.PermissionManager;
import it.richkmeli.richkware.receiver.AlarmServicesManager;
import it.richkmeli.richkware.service.ServiceManager;
import it.richkmeli.richkware.service.network.DiscoverThread;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.system.device.Device;
import it.richkmeli.richkware.system.device.DeviceManager;
import it.richkmeli.richkware.util.Logger;

import static android.net.ConnectivityManager.TYPE_WIFI;

public class MainActivity extends AppCompatActivity {

    private TextView device_id;
    private TextView installation_id;
    // notification
    private Button enableNotificationButton;
    private Button disableNotificationButton;
    // permission
    private Button requestStandardPermissionButton;
    private Button requestCriticalPermissionButton;
    // storage
    private Button writeToFile;
    private Button readFromFile;
    // services
    private Button startForegroundServices;
    private Button stopForegroundServices;
    private Button startBackgroundServices;
    private Button stopBackgroundServices;
    // network
    private Spinner protocolSpinner;
    private EditText serverEditText;
    private EditText serviceEditText;
    private Spinner networkSpinner;
    private Button networkExecute;
    private TextView networkOutput;
    private Button discover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeButtons();
        initializeTextview();

        checkSavedDeviceID();

        // Start Threads
        AlarmServicesManager.setAlarmFormASM(getApplicationContext());
    }

    private void checkSavedDeviceID() {
        String deviceID = Device.getDeviceID(this);
        String savedDeviceID = StorageManager.read(this, StorageKey.DEVICE_ID);
        if (!deviceID.equalsIgnoreCase(savedDeviceID)) {
            Toast.makeText(this, "DeviceID not equal", Toast.LENGTH_LONG).show();
        }
        StorageManager.save(this, StorageKey.DEVICE_ID, deviceID);
    }

    private void initializeTextview() {
        device_id = findViewById(R.id.device_id);
        installation_id = findViewById(R.id.installation_id);

        protocolSpinner = findViewById(R.id.protocol_spinner);
        serverEditText = findViewById(R.id.server_edittext);
        serviceEditText = findViewById(R.id.service_edittext);
        networkSpinner = findViewById(R.id.network_service_spinner);
        networkOutput = findViewById(R.id.network_output);

        // set text view value
        String deviceID = Device.getDeviceID(this);
        device_id.setText(deviceID);

        String installationID = Device.getInstallationID(this);
        installation_id.setText(installationID);

        protocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StorageManager.save(adapterView.getContext(), StorageKey.NETWORK_PROTOCOL, protocolSpinner.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String networkServer = StorageManager.read(getApplicationContext(), StorageKey.NETWORK_SERVER);
        if (networkServer == null) {
            StorageManager.save(getApplicationContext(), StorageKey.NETWORK_SERVER, serverEditText.getText().toString());
        } else {
            serverEditText.setText(networkServer);
        }
        serverEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                StorageManager.save(getApplicationContext(), StorageKey.NETWORK_SERVER, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        String networkService = StorageManager.read(getApplicationContext(), StorageKey.NETWORK_SERVICE);
        if (networkService == null) {
            StorageManager.save(getApplicationContext(), StorageKey.NETWORK_SERVICE, serviceEditText.getText().toString());
        } else {
            serviceEditText.setText(networkService);
        }
        StorageManager.save(getApplicationContext(), StorageKey.NETWORK_SERVICE, serviceEditText.getText().toString());
        serviceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                StorageManager.save(getApplicationContext(), StorageKey.NETWORK_SERVICE, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        networkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StorageManager.save(adapterView.getContext(), StorageKey.NETWORK_APPLICATION_SERVICE, networkSpinner.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initializeButtons() {
        enableNotificationButton = findViewById(R.id.enableNotification);
        disableNotificationButton = findViewById(R.id.disableNotification);
        requestStandardPermissionButton = findViewById(R.id.requestStandardPermission);
        requestCriticalPermissionButton = findViewById(R.id.requestCriticalPermission);
        writeToFile = findViewById(R.id.system_write);
        readFromFile = findViewById(R.id.system_read);
        startForegroundServices = findViewById(R.id.start_fg_services);
        stopForegroundServices = findViewById(R.id.stop_fg_services);
        startBackgroundServices = findViewById(R.id.start_bg_services);
        stopBackgroundServices = findViewById(R.id.stop_bg_services);
        networkExecute = findViewById(R.id.network_execute);
        discover = findViewById(R.id.discover);

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

        requestStandardPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Requesting standard permission");

                Activity host = (Activity) view.getContext();
                try {
                    PermissionManager.checkPermissions(host);
                } catch (PackageManager.NameNotFoundException e) {
                    Logger.error(e);
                }

            }
        });

        requestCriticalPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Requesting critical permission");

                if (!PermissionManager.checkAppUsagePermission(view.getContext())) {
                    PermissionManager.showAppUsageDialogPermission(view.getContext());
                }
                if (!PermissionManager.checkOverlayPermission(view.getContext())) {
                    PermissionManager.showOverlayDialogPermission(view.getContext());
                }

                DeviceManager deviceManager = new DeviceManager(view.getContext());
                deviceManager.requestDeviceAdminPermission((Activity) view.getContext());
            }
        });

        writeToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageManager.save(view.getContext(), StorageKey.RND_TEST, "" + new Random().nextInt());
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "saved to file");
            }
        });

        readFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = StorageManager.read(view.getContext(), StorageKey.RND_TEST);
                NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, ("Read, " + StorageKey.RND_TEST + ": " + s));
            }
        });

        startForegroundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ServiceManager.startForegroundServices(view.getContext());
                        NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Starting fg services");
                    }
                } catch (Throwable t) {
                    NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, t.getMessage());
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

        RichkwareCallback richkwareCallback = new RichkwareCallback() {
            @Override
            public void onSuccess(String response) {
                //NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Success");
                Logger.info(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkOutput.setText(response);
                    }
                });
            }

            @Override
            public void onFailure(String response) {
                //NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Failed");
                Logger.error(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkOutput.setText(response);
                    }
                });
            }
        };

        networkExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String service = StorageManager.read(view.getContext(), StorageKey.NETWORK_APPLICATION_SERVICE);

                NetworkManager networkManager = new NetworkManager();

                switch (service) {
                    case "UploadInfoToRms":
                        networkManager.uploadInfoToRms(view.getContext(), richkwareCallback);
                        break;
                    case "GetEncryptionKeyFromRMS":
                        networkManager.getEncryptionKeyFromRMS(view.getContext(), richkwareCallback);
                        break;
                    default:
                        NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "service " + service + " not present in switch");
                }

            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkManager.getNetworkInfo(getApplicationContext()).getType() == TYPE_WIFI) {
                    DiscoverThread discoverThread = new DiscoverThread(getApplicationContext());
                    discoverThread.start();

                    serverEditText.setText(StorageManager.read(getApplicationContext(), StorageKey.NETWORK_SERVER));
                }else {
                    NotificationManager.notify(view.getContext(), NotificationType.TOAST_SHORT, "Your are not in a WIFI network");
                }
            }
        });

    }

}