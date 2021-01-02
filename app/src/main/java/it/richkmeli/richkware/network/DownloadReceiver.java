package it.richkmeli.richkware.network;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            Bundle extras = intent.getExtras();
            // retrieve the reference of that download item
            Long id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id != -1) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(id);
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor c = manager.query(q);

                // get download status
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // download success

                        // Show results
                        //    Toast.makeText(context, context.getResources().getString(R.string.receiver_toast_download_success), Toast.LENGTH_SHORT).show();
                        // Get local file path

                        String path = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                        DownloadAndSaveManager dASM = new DownloadAndSaveManager(context);

                        dASM.moveFromCacheToInternal(path, id.toString());
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        //   Toast.makeText(context, context.getResources().getString(R.string.receiver_toast_download_failed), Toast.LENGTH_LONG).show();
                    }
                }
                c.close();
            }
        }
    }

}
