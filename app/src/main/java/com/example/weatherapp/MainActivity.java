package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
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
TextView tempinfo,locinfo,descinfo;
ImageView iconview;


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
        tempinfo=(TextView)findViewById(R.id.tempinfo);
        descinfo=(TextView)findViewById(R.id.descinfo);
        locinfo=(TextView)findViewById(R.id.locinfo);
        iconview=(ImageView)findViewById(R.id.iconview);
        Geocoder geocoder;
        List<Address> addresses;
        String city = "";
        String main="";
        String desc="";
        geocoder = new Geocoder(this, Locale.getDefault());
        String icon="";
        String iconUrl="https://openweathermap.org/img/wn/";
        double latitude = 21.870640;
        double longitude = 73.504288;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
        locinfo.setText(city);
        int temp = 0;

        String accuweather="https://dataservice.accuweather.com//forecasts/v1/daily/1day/197790?apikey=hwDC6Dttgzg8bmVTYaX4Tg80t1Jb3UG7&details=true&metric=true";
        String openweatherapikey="a9a7d60c7fb542a91b03a3c864b5b11b";
        OkHttpClient client = new OkHttpClient();
        try {Request request = new Request.Builder()
                        .url("https://api.openweathermap.org/data/2.5/weather?lat=21.870640&lon=73.504288&appid=a9a7d60c7fb542a91b03a3c864b5b11b&units=metric")
                        .get()
                        .build();

                    String min="";

                    Response response = client.newCall(request).execute();
                    // Your network activity
                    String jsonData =response.body().string();
                    System.out.println(jsonData);
                    JSONObject Jobj1=new JSONObject(jsonData);
                    JSONObject temperature=Jobj1.getJSONObject("main");
                    temp=temperature.getInt("temp");
                    System.out.println(temp);
            JSONArray jsonArray = Jobj1.getJSONArray("weather");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        icon = jsonArray.getJSONObject(i).getString("icon");
                        main=jsonArray.getJSONObject(i).getString("main");
                        desc=jsonArray.getJSONObject(i).getString("description");
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


            tempinfo.setText(String.valueOf(temp)+" °C");
            descinfo.setText(desc);
        String finalUrl=iconUrl+icon+"@4x.png";
        System.out.println(finalUrl);
            Picasso.with(MainActivity.this).load(finalUrl).into(iconview);
//


    }
}