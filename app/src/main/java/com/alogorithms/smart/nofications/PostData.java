/*
package com.alogorithms.smart.nofications;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Base64;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by user on 2016-01-14.
 *//*

public class PostData extends AsyncTask<String, String, String> {
    String  user_id ="";
    Context context =null;
    String type = "";
    String text = "";
    double latitude = 0;
    double longitude = 0;
    String address = "";
    Bitmap image = null;
    notifications.PostDataCallBacks postDataCallBacks;
    public PostData(Context cntxt, String type, String text, double latitude, double longitude, String address, Bitmap image, String user_id, notifications.PostDataCallBacks postDataCallBacks) {
        this.type = type;
        this.text = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        //this.text = text.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.image = image;
        this.context = cntxt;
        this.user_id = user_id;
        this.postDataCallBacks = postDataCallBacks;
    }

    public PostData(Context cntxt, String type, String text, double latitude, double longitude, String address, String mPhoneNumber, notifications.PostDataCallBacks postDataCallBacks) {
        this.type = type;
        this.text = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        //this.text = text.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.context = cntxt;
        this.user_id = mPhoneNumber;
        this.postDataCallBacks = postDataCallBacks;
    }

    public void send() throws IOException, JSONException {
        Bitmap bitmap = image;
        JSONObject jsonString = new JSONObject();
        jsonString.put("picture", "true");
        jsonString.put("video", "false");
        jsonString.put("type", type);
        JSONObject locate = new JSONObject();
        locate.put("latitude",latitude);
        locate.put("longitude",longitude);
        locate.put("address", address);
        jsonString.put("loc", locate);
        */
/*JSONObject txt = new JSONObject();
        txt.put("message", text);
        txt.put("flag", "empty");*//*

        jsonString.put("message", text);
        jsonString.put("user_id",user_id);


        String jsonStrings = jsonString.toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        HttpClient client = new DefaultHttpClient();
        //HttpPost post = new HttpPost("http:192.168.43.247/WBA_backend/action_handler.php");
        HttpPost post = new HttpPost("http://notifications.smartalg.co.za/action_handler.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        pairs.add(new BasicNameValuePair("picture", image_str));

        pairs.add(new BasicNameValuePair("json", jsonStrings));
            post.setEntity(new UrlEncodedFormEntity(pairs));

            HttpResponse response = client.execute(post);
        HttpEntity resEntity = response.getEntity();
        String Response = EntityUtils.toString(resEntity);
        Toast.makeText(context, "Response:  \n- " + Response, Toast.LENGTH_LONG).show();

    }

    public void send2() throws IOException, JSONException {
        JSONObject jsonString = new JSONObject();
        jsonString.put("picture", "true");
        jsonString.put("video", "false");
        jsonString.put("type", type);
        JSONObject locate = new JSONObject();
        locate.put("latitude",latitude);
        locate.put("longitude",longitude);
        locate.put("address", address);
        jsonString.put("loc", locate);
        */
/*JSONObject txt = new JSONObject();
        txt.put("message", text);
        txt.put("flag", "empty");*//*

        jsonString.put("message", text);
        jsonString.put("user_id", user_id);


        String jsonStrings = jsonString.toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        HttpClient client = new DefaultHttpClient();
        //HttpPost post = new HttpPost("http://192.168.43.247:8000/action_handler.php");
        HttpPost post = new HttpPost("http://notifications.smartalg.co.za/action_handler.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        pairs.add(new BasicNameValuePair("json", jsonStrings));
        post.setEntity(new UrlEncodedFormEntity(pairs));

        HttpResponse response = client.execute(post);
        HttpEntity resEntity = response.getEntity();
        String Response = EntityUtils.toString(resEntity);
        Toast.makeText(context, "Response:  \n- " + Response, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {

        JSONObject jsonString = new JSONObject();
        try {
            if(image != null) {
                jsonString.put("picture", "true");
            }
            else{
                jsonString.put("picture", "false");
            }
            jsonString.put("video", "false");
            jsonString.put("type", type);
            JSONObject locate = new JSONObject();
            locate.put("latitude",latitude);
            locate.put("longitude",longitude);
            locate.put("address", address);
            jsonString.put("loc", locate);
            */
/*JSONObject txt = new JSONObject();
            txt.put("message", text);
            txt.put("flag", "empty");*//*

            jsonString.put("message", text);
            jsonString.put("user_id",user_id);
            jsonString.put("action","alert");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
        HttpClient client = new DefaultHttpClient();
            //TO DO: Add a generic IP address per location.
        //HttpPost post = new HttpPost("http://192.168.43.247/WBA_backend/action_handler.php");
        HttpPost post = new HttpPost("http://notifications.smartalg.co.za/action_handler.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if(image != null) {
            Bitmap bitmap = image;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            pairs.add(new BasicNameValuePair("picture", image_str));
        }
        String jsonStrings = jsonString.toString();
        pairs.add(new BasicNameValuePair("json", jsonStrings));

            HttpResponse response = null;
            post.setEntity(new UrlEncodedFormEntity(pairs));
            response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            String Response = null;
            Response = EntityUtils.toString(resEntity);



            //Toast.makeText(context, "Response:  \n- " + Response, Toast.LENGTH_LONG).show();
            showToast(Response, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void showToast(final String text, final Context ct){
        try {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(ct, text, Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        //postDataCallBacks.dismissDialog();
        if (file_url != null) {
            Toast.makeText(context, file_url, Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //postDataCallBacks.displayDialog();

    }
}
*/
