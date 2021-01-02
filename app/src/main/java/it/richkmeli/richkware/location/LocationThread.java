package it.richkmeli.richkware.location;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;


public class LocationThread extends Thread {
    public boolean isEnded = false;
    private LocationManager locationManager;
    private Location currentLocation;
    private Context context;
    private int timeSleep = 300000;


    public LocationThread(Context context, LocationManager locationManager, int timeSleep) {
        this.context = context;
        this.locationManager = locationManager;
        this.timeSleep = timeSleep;
    }

    @Override
    public void run() {

        while (!isEnded) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    //updateLocation(lastKnownLocation);
                    // TODO marker
                    currentLocation = lastKnownLocation;
                    timeSleep = 300000;
                } else {

                    Intent locationService = new Intent(context.getApplicationContext(), LocationService.class);
                    ServiceConnection sConn = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {

                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    };
                    context.bindService(locationService, sConn, Context.BIND_AUTO_CREATE);
                    context.unbindService(sConn);
                    timeSleep = 10000;
                }
            }

            try {
                sleep(timeSleep);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }


    private void updateLocation(Location location) {

        SharedPreferences mapSharedPreferences = context.getSharedPreferences("Map", Context.MODE_PRIVATE);
        SharedPreferences settingSharedPreferences = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

        if (settingSharedPreferences.getBoolean("isLocationActive", true)) {

            boolean thereIsMarker = mapSharedPreferences.getBoolean("thereIsMarker", false);
            double latMarker = Double.parseDouble(mapSharedPreferences.getString("latitude", "0"));
            double lonMarker = Double.parseDouble(mapSharedPreferences.getString("longitude", "0"));

            Location targetLocation = new Location("");
            targetLocation.setLatitude(latMarker);
            targetLocation.setLongitude(lonMarker);

            currentLocation = location;
            if (thereIsMarker) {
                if (location.distanceTo(targetLocation) < 1000) {
                    settingSharedPreferences.edit().putBoolean("sleepLocation", true).apply();
                } else {
                    settingSharedPreferences.edit().putBoolean("sleepLocation", false).apply();
                }
            }

        }
    }
}