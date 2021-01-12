package it.richkmeli.richkware.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import it.richkmeli.jframework.crypto.algorithm.AES;
import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.crypto.system.SecureFileManager;
import it.richkmeli.richkware.system.device.DeviceInfo;
import it.richkmeli.richkware.util.Logger;

public class StorageManager {
    public static final String FILE = "storage.bin";
    public static String SECRET_KEY = "richktest";
    public static final String STORAGE_MANAGER_SHARED_PREFERENCES = "STORAGE_MANAGER";

    public static void save(Context context, StorageKey key, String value) {
        try {
            String s = readFromInternalFile(context);
            JSONObject jsonObject;
            if ("".equalsIgnoreCase(s)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(s);
            }
            JSONObject newJsonObject = jsonObject.put(key.name(), value);
            saveToInternalFile(context, newJsonObject.toString());
        } catch (JSONException jsonException) {
            Logger.error("", jsonException);
        }
    }

    public static String read(Context context, StorageKey key) {
        String out = null;
        try {
            String s = readFromInternalFile(context);
            JSONObject jsonObject;
            if ("".equalsIgnoreCase(s)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(s);
            }

            if (jsonObject.has(key.name())) {
                out = jsonObject.getString(key.name());
            } else {
                Logger.error("key is not present in secureFile");
            }
        } catch (JSONException jsonException) {
            Logger.error("", jsonException);
        }
        return out;
    }


    private static boolean saveToInternalFile(Context context, String value) {
        SECRET_KEY = DeviceInfo.getDeviceID(context);//getInstallationID(context);
        return SecureFileManager.saveEncryptedDataToFile(new File(context.getFilesDir(), FILE), value, SECRET_KEY);
    }

    private static String readFromInternalFile(Context context) {
        // external: /*getExternalFilesDir("test")*/
        SECRET_KEY = DeviceInfo.getDeviceID(context);//getInstallationID(context);
        String s = SecureFileManager.loadEncryptedDataFromFile(new File(context.getFilesDir(), FILE), SECRET_KEY);
        Logger.info("StorageManager, read: " + s);
        return s;
    }

    private static boolean saveToSharedPreferences(Context context, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STORAGE_MANAGER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String encrypted = "";
        try {
            encrypted = AES.encrypt(value, SECRET_KEY);
        } catch (CryptoException e) {
            Logger.error("Error decrypting SharedPreferences '" + STORAGE_MANAGER_SHARED_PREFERENCES + "'", e);
            return false;
        }
        editor.putString(STORAGE_MANAGER_SHARED_PREFERENCES, encrypted);
        editor.apply();
        return true;
    }

    private static String readFromSharedPreferences(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STORAGE_MANAGER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String encryptedData = sharedPrefs.getString(STORAGE_MANAGER_SHARED_PREFERENCES, null);
        if (encryptedData == null) {
            Logger.error("encryptedData is null");
            return encryptedData;
        } else if ("".equalsIgnoreCase(encryptedData)) {
            Logger.error("SharedPreferences '" + STORAGE_MANAGER_SHARED_PREFERENCES + "' is empty");
            return encryptedData;
        } else {
            String decrypted = "";

            try {
                decrypted = AES.decrypt(encryptedData, SECRET_KEY);
                return decrypted;
            } catch (CryptoException e) {
                Logger.error("Error decrypting SharedPreferences '" + STORAGE_MANAGER_SHARED_PREFERENCES + "'", e);
                return null;
            }
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
