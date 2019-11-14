package com.alogorithms.smart.nofications;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2017-05-18.
 */
public class GetLocation {

    private Context context;

    protected GetLocation(Context cntxt) {
        this.context = cntxt;
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder gcd = new Geocoder(this.context, Locale.getDefault());
        String cityName = "";
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
}
