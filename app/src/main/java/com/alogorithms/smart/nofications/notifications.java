package com.alogorithms.smart.nofications;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.lang.Object.*;

public class notifications extends AppCompatActivity implements View.OnClickListener, LocationListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    private String text = "";
    private String type = "";
    private Bitmap image = null;
    //private Location location = null;
    final int REQUEST_READ_PHONE_STATE = 98;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GPSTracker gps = null;
    double latitude = 0;
    double longitude = 0;
    String address = "";
    final int REQUEST_CHECK_SETTINGS = 0x1;
    final int location_request_count = 2;
    int counter = 0;
    Boolean send_phone_num = false;
    String mPhoneNumber = "-";
    TelephonyManager tMgr;
    Activity myAct = this;
    LocationManager locationManager;
    String provider;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //Locator locator = new Locator(notifications.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("WBA");
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        final Spinner report_type = (Spinner) findViewById(R.id.spinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.report_type, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //report_type.setAdapter(adapter);
        //report_type.setOnItemSelectedListener(new CustomOn);
        ImageButton camera_image = (ImageButton) findViewById(R.id.imageButt);
        camera_image.setOnClickListener(this);


        final ToggleButton toggle_b = (ToggleButton) findViewById(R.id.toggleButton);
        toggle_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gps = new GPSTracker(notifications.this);
                if (toggle_b.isChecked()) {


                    //client.connect();
                    //createLocationRequest();
                    //super.onStart();
                    /*LatLng scn = null;
                    try {
                        scn = getLocation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    longitude = scn.longitude;
                    latitude = scn.latitude;*/
                    GetLocation glct = new GetLocation(getApplicationContext(),myAct);
                    glct.setLocation();
                    longitude = glct.getLongitude();
                    latitude = glct.getLatitude();
                    address = glct.getAddress();
                    if (longitude != 0 && latitude != 0) {
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nAddress: " + address, Toast.LENGTH_LONG).show();
                    } else {
                        if (counter > location_request_count) {
                            Toast.makeText(getApplicationContext(), "Error getting location", Toast.LENGTH_LONG).show();
                            toggle_b.setChecked(false);
                        } else {
                            counter++;
                            //locator.showSettingsAlert();
                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                            //client.connect();
                            //createLocationRequest();
                            /*LatLng scn2 = null;
                            try {
                                scn2 = getLocation();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            longitude = scn2.longitude;
                            latitude = scn2.latitude;*/
                            Toast.makeText(getApplicationContext(), "No location, Please try again", Toast.LENGTH_LONG).show();
                            glct.setLocation();
                            longitude = glct.getLongitude();
                            latitude = glct.getLatitude();
                            address = glct.getAddress();
                            toggle_b.setChecked(false);
                        }

                    }
                }
            }
        });



        tMgr = (TelephonyManager) notifications.this.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber.equalsIgnoreCase("-")) {
                //mPhoneNumber = tMgr.getDeviceId();
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(myAct, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    mPhoneNumber = tMgr.getDeviceId();
                }
            }

        } catch (Exception e) {

            //mPhoneNumber = tMgr.getDeviceId();
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(myAct, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            } else {
                mPhoneNumber = tMgr.getDeviceId();
            }

        }
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //provider = locationManager.getBestProvider(new Criteria(), false);

        //locationManager.requestLocationUpdates(
                /*LocationManager.GPS_PROVIDER,
                0,
                0, this);*/

        FloatingActionButton send_button = (FloatingActionButton) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.editText);
                type = report_type.getSelectedItem().toString();
                text = et.getText().toString();
                boolean sendLoc = false;
                boolean sendPic = false;


                //if(send_phone_num == true) {


                /*}
                else{
                    mPhoneNumber = "-";
                }*/
                if (latitude != 0 && longitude != 0 && !address.equalsIgnoreCase("")) {
                    sendLoc = true;
                }
                if (image != null) {
                    sendPic = true;
                }
                if (sendPic && sendLoc) {
                    PostData pd = new PostData(notifications.this, type, text, latitude, longitude, address, image, mPhoneNumber);


                    pd.execute();

                } else if (!sendPic && sendLoc) {
                    PostData pd = new PostData(notifications.this, type, text, latitude, longitude, address, mPhoneNumber);
                    //PostData pd = new PostData(wba_notifications.this,"", "", 0,0, "", "");

                    pd.execute();

                } else if (sendPic && !sendLoc) {
                    PostData pd = new PostData(notifications.this, type, text, 0, 0, "", image, mPhoneNumber);
                    //PostData pd = new PostData(wba_notifications.this,"", "", 0,0, "", "");

                    pd.execute();

                } else {
                    PostData pd = new PostData(notifications.this, type, text, 0, 0, "", mPhoneNumber);

                    pd.execute();

                }


            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*public LatLng getLocation() throws InterruptedException {
        // Get the location manager
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
       final long MIN_TIME_BW_UPDATES = 25000;
        Criteria criteria = new Criteria();
        //String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED *//*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED *//*) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        if(locationManager ==null){
            Toast.makeText(getApplicationContext(), "manager is null", Toast.LENGTH_LONG).show();
        }
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);;

        if(location == null){
            //wait(5000);
            //location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            Toast.makeText(getApplicationContext(), "location is null", Toast.LENGTH_LONG).show();
        }
        Double lat,lon;
        try {
            if(location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            else {

                GPSTracker lc = new GPSTracker(this);
                lat = lc.getLatitude();
                lon = lc.getLongitude();
            }
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
*/
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mPhoneNumber = tMgr.getDeviceId();
                }
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int PICK_IMAGE_ID = 234;

    @Override
    public void onClick(View v) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                image = bitmap;
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setImageBitmap(bitmap);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
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

    }
}
