package it.richkmeli.richkware.service.network;


import android.content.Context;

import it.richkmeli.richkware.network.NetworkManager;
import it.richkmeli.richkware.network.RichkwareCallback;
import it.richkmeli.richkware.util.Logger;

public class NetworkThread extends Thread {
    public boolean isEnded = false;
    private final Context context;
    private int timeSleep;


    public NetworkThread(Context contextTmp, int timeSleepTmp) {
        context = contextTmp;
        timeSleep = timeSleepTmp;
    }

    @Override
    public void run() {

        while (!isEnded) {
            NetworkManager networkManager = new NetworkManager();
            networkManager.uploadInfoToRms(context, new RichkwareCallback() {
                @Override
                public void onSuccess(String response) {
                    Logger.info(response);
                }

                @Override
                public void onFailure(String response) {
                    Logger.error(response);
                }
            });
            try {
                sleep(timeSleep);
            } catch (InterruptedException e) {
                Logger.error(e);
            }
        }


    }

}
