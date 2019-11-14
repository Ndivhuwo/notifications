package com.alogorithms.smart.nofications.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alogorithms.smart.nofications.helper.GeneralHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {


    final String TAG = LocationService.class.getSimpleName();

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if (GeneralHelper.isLocationEnabled(LocationService.this)) {
                    unregisterReceiver(this);
                    createLocationRequest();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent p0) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //f(intent.getAction() != null) {
            Log.i(TAG, "onStartCommand Start Service");
            createLocationRequest();
    //}

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    private void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (checkPermissionLocation() && GeneralHelper.isLocationEnabled(this)) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult != null) {
                for (Location location : locationResult.getLocations()) {
                    Intent intent = new Intent("com.alogorithms.smart.nofications.location");
                    intent.putExtra("lat", location.getLatitude());
                    intent.putExtra("lng", location.getLongitude());
                    LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
                }
            }
        }
    };

    private boolean checkPermissionLocation() {
        boolean granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        Log.i(TAG, "checkPermissionLocation Granted: $granted");
        return granted;
    }

}
