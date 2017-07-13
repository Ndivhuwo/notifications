package com.alogorithms.smart.nofications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2016-06-25.
 */
public class Locator  {

    private Context mContext;
    public Locator(Context context) {
        this.mContext = context;
    }

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
}
