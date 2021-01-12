package it.richkmeli.richkware.system.device;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import it.richkmeli.jframework.crypto.algorithm.SHA256;
import it.richkmeli.jframework.util.TypeConverter;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.util.Logger;

public class DeviceInfo {
    private static String installationID = null;

    public static String getDeviceID(Context context) {

        //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String hashedDeviceID = SHA256.hash(android_id);
        return hashedDeviceID;
    }

    public static String getPhoneID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // getDeviceId() returns the unique device ID.
        String deviceId = "";
        if (telephonyManager != null) {
            try {
                switch (telephonyManager.getPhoneCount()) {
                    case 0:
                        // voice, sms, data not supported
                        Logger.error("DeviceInfo: voice, sms, data not supported");
                        break;
                    case 1:
                        // single sim
                        deviceId = telephonyManager.getDeviceId();
                        break;
                    case 2:
                        // dual sim
                        deviceId = telephonyManager.getDeviceId(0) + telephonyManager.getDeviceId(1);
                        break;
                    default:
                        Logger.error("DeviceInfo: getPhoneCount return an unexpected value");

                }
            } catch (SecurityException e) {
                Logger.error("DeviceInfo", e);
            }
        }

        return SHA256.hash(deviceId);
    }

    /**
     * identify a specific installation (not a physical device)
     *
     * @param context
     * @return
     */
    public synchronized static String getInstallationID(Context context) {
        if (installationID == null) {
            installationID = StorageManager.read(context, StorageKey.INSTALLATION_ID);
            if (installationID == null) {
                String uuid = UUID.randomUUID().toString();
                String hashedUuid = SHA256.hash(uuid);
                StorageManager.save(context, StorageKey.INSTALLATION_ID, hashedUuid);
            }
        }
        return installationID;
    }

    public static String getNetworkID() {
        String networkID = "";
        try {
            Enumeration<NetworkInterface> hardwareAddress = NetworkInterface.getNetworkInterfaces();
            StringBuilder hardwareAddressList = new StringBuilder();
            while (hardwareAddress.hasMoreElements()) {
                NetworkInterface networkInterface = hardwareAddress.nextElement();
                if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && !networkInterface.isPointToPoint()) {
                    if (networkInterface.getHardwareAddress() != null) {
                        hardwareAddressList.append(TypeConverter.bytesToHex(networkInterface.getHardwareAddress()));
                        //Logger.info(networkInterface.getDisplayName() + ": " + TypeConverter.bytesToHex(networkInterface.getHardwareAddress()));
                    }
                }
            }
            networkID = hardwareAddressList.toString();
        } catch (SocketException e) {
            Logger.error("DeviceInfo", e);
        }
        return SHA256.hash(networkID);
    }
}
