package com.alogorithms.smart.nofications;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by user on 2016-06-16.
 */
public class Not_Location extends Service implements LocationListener {
    private Location myCurrentBestLocation = null;
    private Context context = null;
    private LocationManager locationManager = null;
    static final int TWO_MINUTES = 1000*60*2;
    public Not_Location(Context inContext){
        this.context = inContext;
        locationManager = (LocationManager)this.context.getSystemService(LOCATION_SERVICE);
    }
    private Location getMyLastBestLocation() {
        try {
            Location GPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location NetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            long NetworkLocationTime = 0;

            if(GPSLocation != null){
                GPSLocationTime = GPSLocation.getTime();
            }

            if(NetworkLocation != null){
                NetworkLocationTime = NetworkLocation.getTime();
            }

            if((GPSLocationTime - NetworkLocationTime) > 0){
                return GPSLocation;
            }
            else{
                return NetworkLocation;
            }

        } catch (SecurityException se) {
            se.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        useNewLocation(location);

        if(myCurrentBestLocation == null){
            myCurrentBestLocation = location;
        }

    }

    public void useNewLocation(Location location){

        //if(isB)
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
}
