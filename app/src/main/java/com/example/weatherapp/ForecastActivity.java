package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastActivity extends AppCompatActivity {
TextView text;
    private GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        text=(TextView) findViewById(R.id.text);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Geocoder geocoder;
        List<Address> addresses;
        String city = "";
        String main = "";
        String desc = "";
        double latitude = 0;
        double longitude = 0;
//        21.870640,73.504288
        latitude = getLat(latitude);
        longitude = getLon(longitude);
        System.out.println(latitude);
        System.out.println(longitude);
        geocoder = new Geocoder(this, Locale.getDefault());
//        String icon ="";
        String iconUrl = "https://openweathermap.org/img/wn/";
        String openweatherapikey = "a9a7d60c7fb542a91b03a3c864b5b11b";


        ArrayList<Integer>temp=new ArrayList<Integer>(20);
        int min_temp = 0, max_temp = 0, humid = 0, feelslike = 0;
        float pressure = 0;
        ArrayList<String>date
                = new ArrayList<String>(20);
        ArrayList<String>time
                = new ArrayList<String>(20);
        HashSet<String> forecast_date;
        OkHttpClient client = new OkHttpClient();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            Request request = new Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&cnt=8&appid=a9a7d60c7fb542a91b03a3c864b5b11b&units=metric")
                    .get()
                    .build();



            Response response = client.newCall(request).execute();
            // Your network activity
            System.out.println(response);
            String jsonData = response.body().string();
            System.out.println(jsonData);
            JSONObject Jobj2 = null;
            JSONObject Jobj1 = new JSONObject(jsonData);


            JSONArray jsonArray = Jobj1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                date.add(jsonArray.getJSONObject(i).getString("dt_txt").substring(0,10));
                time.add(jsonArray.getJSONObject(i).getString("dt_txt").substring(11,19));

                main = jsonArray.getJSONObject(i).getString("main");
                Jobj2= new JSONObject(main);
                temp.add(Jobj2.getInt("temp"));

            }

            System.out.println(Jobj2);

            System.out.println(temp);
            System.out.println(date);
            System.out.println(time);
            System.out.println(main);
            System.out.println(desc);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ForecastActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
    }
    public double getLat(double lat) {
        gpsTracker = new GpsTracker(ForecastActivity.this);
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        return lat;
    }

    public double getLon(double lon) {
        gpsTracker = new GpsTracker(ForecastActivity.this);
        if (gpsTracker.canGetLocation()) {
            lon = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        return lon;
    }
}