package it.richkmeli.richkware.service.network;


import android.content.Context;

import it.richkmeli.richkware.network.NetworkManager;
import it.richkmeli.richkware.network.RichkwareCallback;
import it.richkmeli.richkware.util.Logger;

public class DiscoverThread extends Thread {
    public boolean isEnded = false;
    private final Context context;
    private int timeSleep;


    public DiscoverThread(Context contextTmp, int timeSleepTmp) {
        context = contextTmp;
        timeSleep = timeSleepTmp;
    }


    public DiscoverThread(Context contextTmp) {
        this(contextTmp,1000*60*60);
    }

    @Override
    public void run() {

        while (!isEnded) {
            NetworkManager.discoverRmsInLocalNetwork(context);
            try {
                sleep(timeSleep);
            } catch (InterruptedException e) {
                Logger.error(e);
            }
        }


    }

}
