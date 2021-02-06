package it.richkmeli.richkware.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import it.richkmeli.jframework.crypto.algorithm.RC4;
import it.richkmeli.jframework.network.tcp.client.okhttp.Network;
import it.richkmeli.jframework.network.tcp.client.okhttp.NetworkCallback;
import it.richkmeli.jframework.network.tcp.client.okhttp.NetworkException;
import it.richkmeli.jframework.network.tcp.client.okhttp.util.ResponseParser;
import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.system.device.DeviceInfo;

public class NetworkManager {
    private Network network;

    public NetworkManager() {
        network = new Network();
    }

    public void uploadInfoToRms(Context context, RichkwareCallback callback) {
        try {
            getUrlFromSetting(context);

            JSONObject payload = new JSONObject();
            // device ID
            payload.put("data0", RC4.encrypt(DeviceInfo.getDeviceID(context), "richktest"));
            // data
            JSONObject dataPayload = new JSONObject();
            dataPayload.put("serverPort", "none");
            dataPayload.put("associatedUser", "richk@i.it");
            payload.put("data", RC4.encrypt(dataPayload.toString(), "richktest"));
            // channel
            payload.put("channel", "richkware");

            network.putRequest("device", payload.toString(), null, new NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if (ResponseParser.isStatusOK(response))
                            callback.onSuccess(ResponseParser.parseMessage(response));
                        else
                            callback.onFailure(ResponseParser.parseMessage(response));
                    } catch (JSONException e) {
                        NotificationManager.notify(context, NotificationType.TOAST_SHORT, e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e.getMessage());
                }
            });
        } catch (Exception e) {
            NotificationManager.notify(context, NotificationType.TOAST_SHORT, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getEncryptionKeyFromRMS(Context context, RichkwareCallback callback) {
        try {
            getUrlFromSetting(context);

            JSONObject payload = new JSONObject();
            // device ID
            payload.put("id", RC4.encrypt(DeviceInfo.getDeviceID(context), "richktest"));
            payload.put("channel", "richkware");

            network.getRequest("encryptionKey", payload.toString(), "", null, new NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if (ResponseParser.isStatusOK(response))
                            callback.onSuccess(ResponseParser.parseMessage(response));
                        else
                            callback.onFailure(ResponseParser.parseMessage(response));
                    } catch (JSONException e) {
                        NotificationManager.notify(context, NotificationType.TOAST_SHORT, e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e.getMessage());
                }
            });
        } catch (Exception e) {
            NotificationManager.notify(context, NotificationType.TOAST_SHORT, e.getMessage());
            e.printStackTrace();
        }
    }

    private void getUrlFromSetting(Context context) throws NetworkException {
        String protocol = StorageManager.read(context, StorageKey.NETWORK_PROTOCOL);
        String port = protocol.equalsIgnoreCase("HTTPS") ? "443" : "8080";
        String server = StorageManager.read(context, StorageKey.NETWORK_SERVER);
        String service = StorageManager.read(context, StorageKey.NETWORK_SERVICE);
        network.setURL(protocol, server, port, service);
    }
}
