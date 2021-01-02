package it.richkmeli.richkware.location;


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
        if (locationThread != null)
            // allow to the thread to be terminated
            locationThread.isEnded = true;
        stopLocalization();
        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {

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



