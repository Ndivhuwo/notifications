package com.alogorithms.smart.nofications;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.security.Provider;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2016-01-14.
 */
public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    // Flag for GPS status
    boolean isGPSEnabled = false;
    // Flag for network status
    boolean isNetworkEnabled = false;
    // Flag for GPS status
    boolean canGetLocation = false;
    private Location location;//= null; // Location
    double latitude; // Latitude
    double longitude; // Longitude
    String address = "";
    String locDetails = "";

    public GPSTracker() {
    }

    protected GPSTracker(Context context) {

        this.mContext = context;
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //this.locationListener = new
        this.locDetails = getLocation();
    }

    public String getLocDetails(){
        return this.locDetails;
    }
    public String getLocation() {
        //Location temp_location = new LocationManager();
        String GPSLocation = "NO";
        try {
            // Getting GPS status

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            //isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            /*if (isGPSEnabled) {
                this.canGetLocation = true;
            }*/

            //    if (!isGPSEnabled) {
                    // No network provider is enabled
                //} else {
                    //this.canGetLocation = true;
                    /*if (isNetworkEnabled && !isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //Log.d("Network", "Network");
                        if (locationManager != null) {
                            this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (this.location != null) {
                                this.latitude = this.location.getLatitude();
                                this.longitude = this.location.getLongitude();
                                GPSLocation = "YES";
                            }
                        }
                    }*/
                    //If GPS enabled, get latitude/longitude using GPS Services
                    if (isGPSEnabled ) {
                        //if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                //onLocationChanged(location);
                                if (location != null) {
                                    //onLocationChanged(location);
                                    this.latitude = location.getLatitude();
                                    this.longitude = location.getLongitude();
                                    //latitude = location.getLatitude();
                                    //longitude = location.getLongitude();
                                    GPSLocation = "YES";

                                }
                                else{
                                    GPSLocation = "Location is null";
                                }
                            }
                        else{
                                GPSLocation = "LocationManager is null";
                            }
                        //}
                    }
            else {
                        GPSLocation = "Turn gps on";
                    }

            }catch(SecurityException e){
                e.printStackTrace();
                GPSLocation = "ERROR";
            }

            return GPSLocation ;
        }



    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     */
    public void stopUsingGPS() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        } catch (SecurityException se) {

        }
    }


    public String getAddress(double lat, double longi) {
        Geocoder gcd = new Geocoder(this.mContext, Locale.getDefault());
        String cityName = "";
        List<Address> addresses;
        try {
            //if(location != null) {
                addresses = gcd.getFromLocation(lat, longi, 1);
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


    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not active, Do you want to activate it?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        //address = this.getAddress(latitude, longitude);
        // \n is for new line
        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nAddress: " + address, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    public IBinder onBind(Intent arg0) {
        return null;
    }
}
