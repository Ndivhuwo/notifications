package com.alogorithms.smart.nofications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.alogorithms.smart.nofications.helper.GeneralHelper;
import com.alogorithms.smart.nofications.model.Alert;
import com.alogorithms.smart.nofications.network.NetworkManagerImpl;
import com.alogorithms.smart.nofications.network.contract.NetworkManager;
import com.alogorithms.smart.nofications.network.service.NetworkService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notifications extends AppCompatActivity implements LocationListener {

    private static final String TAG = notifications.class.getSimpleName();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    private String text = "";
    private String type = "";
    private Bitmap image = null;
    private String imagePath;
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
    final RxPermissions rxPermissions = new RxPermissions(this);
    ProgressDialog pDialog = null;
    Gson gson = new GsonBuilder()
            //.disableHtmlEscaping()
            .registerTypeAdapter(Date.class, new GeneralHelper.MyDateTypeAdapter2())
            .create();
    private NetworkManager networkManager;
    ToggleButton toggle_b;
    //Spinner report_type;
    ImageButton camera_image;
    RelativeLayout send_button;
    ImageView img;
    ListView lv_alert_type_filter;
    String[] alertTypes;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //Locator locator = new Locator(notifications.this);
    private NetworkService getRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClientBuilder().build())
                .baseUrl(getString(R.string.text_effortless_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(NetworkService.class);
    }

    private OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        //Global Header
        Interceptor headerContentTypeInterceptor = chain -> {
            okhttp3.Request request = chain.request();
            Headers headers = request.headers().newBuilder().add("Content-Type", "application/json").build();
            request = request.newBuilder().headers(headers).build();
            return chain.proceed(request);
        };
        //Add the interceptor to the client builder.
        clientBuilder.addInterceptor(headerContentTypeInterceptor);
        return clientBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Issues");

        img = findViewById(R.id.imageView);

        camera_image = findViewById(R.id.imageButt);
        camera_image.setOnClickListener(onClickListener);

        networkManager = new NetworkManagerImpl(getRetrofitService());


        toggle_b = findViewById(R.id.toggleButton);
        toggle_b.setOnClickListener(onClickListener);

        tMgr = (TelephonyManager) notifications.this.getSystemService(Context.TELEPHONY_SERVICE);

        if (mPhoneNumber.equalsIgnoreCase("-")) {
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(myAct, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            } else {
                mPhoneNumber = tMgr.getDeviceId();
            }
        }

        send_button = findViewById(R.id.send_button);
        send_button.setOnClickListener(onClickListener);
        lv_alert_type_filter = findViewById(R.id.lv_alert_type_filter);

        alertTypes = getResources().getStringArray(R.array.report_type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, alertTypes);
        lv_alert_type_filter.setAdapter(arrayAdapter);

        lv_alert_type_filter.setOnItemClickListener(onItemClickListener);
        lv_alert_type_filter.setItemChecked(0, true);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            type = alertTypes[i];
            Log.i(TAG, "Selected type: " + type);
        }
    };

    private PostDataCallBacks postDataCallBacks = new PostDataCallBacks() {
        @Override
        public void displayDialog() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog = new ProgressDialog(notifications.this);
                    if (image != null) {
                        pDialog.setMessage("Uploading Picture");
                    } else {
                        pDialog.setMessage("Uploading Information");
                    }
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            });

        }

        @Override
        public void dismissDialog() {
            if (pDialog != null) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                    }
                });

            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButt:
                    if (!rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) || !rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .doOnDispose(() -> Log.i(TAG, "Disposing requestPhonePermissions Single"))
                                .subscribe(granted -> {
                                    if (granted) {
                                        Log.d(TAG, "Permissions set");
                                        startImagePicker();
                                    } else {
                                        Log.d(TAG, "Permissions not set");
                                        Toast.makeText(getApplicationContext(), getString(R.string.sentence_permissions_required), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.d(TAG, "Permissions Already set");
                        startImagePicker();
                    }
                    break;
                case R.id.toggleButton:
                    if (toggle_b.isChecked()) {

                        GetLocation glct = new GetLocation(getApplicationContext(), myAct);
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

                                Toast.makeText(getApplicationContext(), "No location, Please try again", Toast.LENGTH_LONG).show();
                                glct.setLocation();
                                longitude = glct.getLongitude();
                                latitude = glct.getLatitude();
                                address = glct.getAddress();
                                toggle_b.setChecked(false);
                            }

                        }
                    }
                    break;
                case R.id.send_button:
                    EditText et = findViewById(R.id.editText);
                    text = et.getText().toString();
                    postDataCallBacks.displayDialog();
                    try {
                        if (imagePath != null) {
                            Observable.defer(()-> Observable.just(networkManager.uploadImage(new File(imagePath))))
                                    .flatMap(image -> {
                                        Alert alert = new Alert(GeneralHelper.alertTypeFromString(type),
                                                latitude, longitude, address, text, mPhoneNumber, image.getPath());
                                        return Observable.just(networkManager.postData(alert));
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(alert -> {
                                        Log.i(TAG, "postData Success " + alert.getId());
                                        postDataCallBacks.dismissDialog();
                                        Toast.makeText(getApplicationContext(), getString(R.string.text_success), Toast.LENGTH_LONG).show();
                                    }, e -> {
                                        //Throwable error = processError(e);
                                        e.printStackTrace();
                                        Log.e(TAG, "postData Error: " + e.getMessage());
                                        postDataCallBacks.dismissDialog();
                                        Toast.makeText(getApplicationContext(), getString(R.string.text_failed), Toast.LENGTH_LONG).show();

                                    });

                        } else {
                            Alert alert = new Alert(GeneralHelper.alertTypeFromString(type),
                                    latitude, longitude, address, text, mPhoneNumber);
                            Observable.defer(() -> Observable.just(networkManager.postData(alert)))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(alertResponse -> {
                                        Log.i(TAG, "postData Success " + alertResponse.getId());
                                        postDataCallBacks.dismissDialog();
                                        Toast.makeText(getApplicationContext(), getString(R.string.text_success), Toast.LENGTH_LONG).show();
                                    }, e -> {
                                        //Throwable error = processError(e);
                                        e.printStackTrace();
                                        Log.e(TAG, "postData Error: " + e.getMessage());
                                        postDataCallBacks.dismissDialog();
                                        Toast.makeText(getApplicationContext(), getString(R.string.text_failed), Toast.LENGTH_LONG).show();

                                    });
                        }
                    } catch (Exception e) {
                        postDataCallBacks.dismissDialog();
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    private void startImagePicker() {
        ImagePicker.with(this)
                .setStatusBarColor("#f6dc23")
                .setCameraOnly(false)
                .setMultipleMode(false)
                .setFolderMode(true)
                .setShowCamera(true)
                .setFolderTitle(getString(R.string.text_gallery_title))
                .setImageTitle(getString(R.string.text_gallery_select))
                .setDoneTitle(getString(R.string.text_done))
                .start();
    }

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
                            Manifest.permission.ACCESS_FINE_LOCATION)
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private static final int PICK_IMAGE_ID = 234;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.RC_PICK_IMAGES: {
                if (resultCode == RESULT_OK && data != null) {
                    List<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                    for (Image image : images) {
                        Log.i(TAG, "Selected Image: " + image.getPath());

                        imagePath = image.getPath();
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                    img.setImageBitmap(bitmap);
                }
                break;
            }

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

    interface PostDataCallBacks {
        void displayDialog();

        void dismissDialog();
    }
}
