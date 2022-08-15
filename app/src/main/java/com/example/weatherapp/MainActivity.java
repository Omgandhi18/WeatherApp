package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView tempinfo, locinfo, descinfo,maininfo,pres,humi,maxtemp,mintemp,feels_like;
    ImageView iconview;
    ConstraintLayout layout;
    LinearLayout ll1;


    private GpsTracker gpsTracker;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        tempinfo = (TextView) findViewById(R.id.tempinfo);
        maininfo = (TextView) findViewById(R.id.maininfo);
        descinfo = (TextView) findViewById(R.id.descinfo);
        locinfo = (TextView) findViewById(R.id.locinfo);
        pres = (TextView) findViewById(R.id.pres);
        humi = (TextView) findViewById(R.id.humi);
        maxtemp = (TextView) findViewById(R.id.maxtemp);
        mintemp = (TextView) findViewById(R.id.mintemp);
        feels_like = (TextView) findViewById(R.id.feels_like);
        layout=(ConstraintLayout)findViewById(R.id.layout);
        ll1=(LinearLayout)findViewById(R.id.ll1);
        iconview = (ImageView) findViewById(R.id.iconview);

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
        String icon = "";
        String iconUrl = "https://openweathermap.org/img/wn/";


        int temp = 0;
        int min_temp=0,max_temp=0,humid=0,feelslike=0;
        float pressure = 0;
        String accuweather = "https://dataservice.accuweather.com//forecasts/v1/daily/1day/197790?apikey=hwDC6Dttgzg8bmVTYaX4Tg80t1Jb3UG7&details=true&metric=true";
        String openweatherapikey = "a9a7d60c7fb542a91b03a3c864b5b11b";


        OkHttpClient client = new OkHttpClient();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            Request request = new Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=a9a7d60c7fb542a91b03a3c864b5b11b&units=metric")
                    .get()
                    .build();

            String min = "";

            Response response = client.newCall(request).execute();
            // Your network activity
            String jsonData = response.body().string();
            System.out.println(jsonData);
            JSONObject Jobj1 = new JSONObject(jsonData);
            JSONObject temperature = Jobj1.getJSONObject("main");
            temp = temperature.getInt("temp");
            min_temp = temperature.getInt("temp_min");
            max_temp = temperature.getInt("temp_max");
            feelslike = temperature.getInt("feels_like");
            humid = temperature.getInt("humidity");
            pressure = (float) temperature.getDouble("pressure");
            System.out.println(temp);
            JSONArray jsonArray = Jobj1.getJSONArray("weather");
            for (int i = 0; i < jsonArray.length(); i++) {
                icon = jsonArray.getJSONObject(i).getString("icon");
                main = jsonArray.getJSONObject(i).getString("main");
                desc = jsonArray.getJSONObject(i).getString("description");
            }

            System.out.println(icon);


//                    JSONObject Jobject = new JSONObject(jsonData);
//                    JSONArray jsonArray = Jobject.getJSONArray("DailyForecasts");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        temp = jsonArray.getJSONObject(i).getString("Temperature");
//                    }
//                    System.out.println(temp);
//                    JSONObject obj1=new JSONObject(temp);
//                    JSONObject arr1 = obj1.getJSONObject("Minimum");
//                    String value=arr1.getString("Value");
//                    String unit=arr1.getString("Unit");
//                    System.out.println(value);
//                    System.out.println(unit);
////                    System.out.println(Jobject.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(main.equals("Rain"))
        {
            layout.setBackgroundColor(Color.parseColor("#401b4965"));
            ll1.setBackgroundColor(Color.parseColor("#1b4965"));
            locinfo.setTextColor(Color.parseColor("#6c757d"));
            descinfo.setTextColor(Color.parseColor("#6c757d"));
            maininfo.setTextColor(Color.parseColor("#6c757d"));
            tempinfo.setTextColor(Color.parseColor("#6c757d"));
        }
        else if(main.equals("Clouds"))
        {
            layout.setBackgroundColor(Color.parseColor("#62b6cb"));
            ll1.setBackgroundColor(Color.parseColor("#005f73"));
            locinfo.setTextColor(Color.parseColor("#edf6f9"));
            descinfo.setTextColor(Color.parseColor("#edf6f9"));
            maininfo.setTextColor(Color.parseColor("#edf6f9"));
            tempinfo.setTextColor(Color.parseColor("#edf6f9"));
        }
        else if(main.equals("Snow"))
        {
            layout.setBackgroundColor(Color.parseColor("#40cae9ff"));
            ll1.setBackgroundColor(Color.parseColor("#40cae9ff"));
        }
        else if(main.equals("Clear"))
        {
            layout.setBackgroundColor(Color.parseColor("#40bee9e8"));
            ll1.setBackgroundColor(Color.parseColor("#023047"));
            locinfo.setTextColor(Color.parseColor("#023047"));
            descinfo.setTextColor(Color.parseColor("#023047"));
            maininfo.setTextColor(Color.parseColor("#023047"));
            tempinfo.setTextColor(Color.parseColor("#023047"));
        }
        else if(main.equals("Thunderstorm"))
        {
            layout.setBackgroundColor(Color.parseColor("#40003049"));
            ll1.setBackgroundColor(Color.parseColor("#40003049"));
        }

        locinfo.setText("\uD83D\uDCCD "+city);
        tempinfo.setText(String.valueOf(temp) + " °C");
        maininfo.setText(main+"y");
        maxtemp.setText(String.valueOf(max_temp)+" °C");
        mintemp.setText(String.valueOf(min_temp)+" °C");
        feels_like.setText(String.valueOf(feelslike)+" °C");
        pres.setText(String.valueOf(String.format("%.2f",pressure*0.750062))+" mmHg");
        humi.setText(String.valueOf(humid)+" %");
        descinfo.setText(desc);
        String finalUrl = iconUrl + icon + "@4x.png";
        System.out.println(finalUrl);
        Picasso.with(MainActivity.this).load(finalUrl).into(iconview);



    }

    public double getLat(double lat) {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        return lat;
    }

    public double getLon(double lon) {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            lon = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        return lon;
    }


}