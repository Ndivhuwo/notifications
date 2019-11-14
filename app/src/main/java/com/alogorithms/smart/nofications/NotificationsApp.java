package com.alogorithms.smart.nofications;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.alogorithms.smart.nofications.service.LocationService;

public class NotificationsApp extends Application {
    private static Intent location_intent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        location_intent = new Intent(this, LocationService.class);
    }

    public static Intent getLocationIntent() {
        return location_intent;
    }

    public static Context getContext() {
        return context;
    }
}
