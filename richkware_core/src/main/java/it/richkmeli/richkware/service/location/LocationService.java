package it.richkmeli.richkware.service.location;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.richkmeli.richkware.storage.StorageKey;
import it.richkmeli.richkware.storage.StorageManager;
import it.richkmeli.richkware.util.Logger;

public class LocationService extends Service implements LocationListener {
    private static LocationManager locationManager;
    private LocationThread locationThread;

    @Override
    public IBinder onBind(Intent intent) {
        forceLocalization();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        startLocalization();

        int timeSleep = 300000;
        locationThread = new LocationThread(getApplicationContext(), locationManager, timeSleep);
        locationThread.start();

        // if the service is destroyed the system will create it again
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (locationThread != null) {
            // allow to the thread to be terminated
            locationThread.isEnded = true;
        }
        stopLocalization();
        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {
        JSONObject locationData = new JSONObject();
        try {
            locationData.put("latitude", location.getLatitude());
            locationData.put("longitude", location.getLongitude());
            locationData.put("accuracy", (int) location.getAccuracy());
            locationData.put("altitude", (int) location.getAltitude());
            locationData.put("provider", location.getProvider());
            locationData.put("isMock", location.isFromMockProvider());
        } catch (JSONException jsonException) {
            Logger.error(jsonException);
        }
        StorageManager.save(getApplicationContext(),
                StorageKey.LOCATION, locationData.toString());
        Logger.info(locationData.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void startLocalization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
            }
        }
    }

    private void stopLocalization() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates((LocationListener) this);
            }
            locationManager = null;
        }
    }

    private void forceLocalization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            }
        }
    }

}



