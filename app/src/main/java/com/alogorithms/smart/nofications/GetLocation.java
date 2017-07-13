package com.alogorithms.smart.nofications;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2017-05-18.
 */
public class GetLocation {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private Activity activity;
    private double latitude = 0;
    private double longitude = 0;

    protected GetLocation(Context cntxt, Activity act) {
        this.context = cntxt;
        this.activity = act;
    }

    public void setLocation(){
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //textView.append("\n"+ +" "+);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            //configureButton();
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if(location != null){
                this.longitude = location.getLongitude();
                this.latitude = location.getLatitude();
            }
            else{
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                if(location != null){
                    this.longitude = location.getLongitude();
                    this.latitude = location.getLatitude();
                }
            }
        }
    }

    private void configureButton() {
        //button.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            //}
        //});
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        Geocoder gcd = new Geocoder(this.context, Locale.getDefault());
        String cityName = "";
        List<Address> addresses;
        try {
            //if(location != null) {
            addresses = gcd.getFromLocation(this.latitude, this.getLongitude(), 1);
            if (addresses.size() > 0) {
                //System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            //}
            //else{
            //    cityName = "location is null";
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String s = longitude + "\n" + latitude + "\n\nMy Current City is: "                + cityName;
        return cityName;
    }
}
