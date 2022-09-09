package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
    TextView tempinfo, locinfo, descinfo, maininfo, pres, humi, maxtemp, mintemp, feels_like, refresh, forecast;
    ImageView iconview;
    ConstraintLayout layout;
    CardView cd1;
    VideoView video;
    LinearLayout ll1;
    SwipeListener swipeListener;

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
        refresh = (TextView) findViewById(R.id.refresh);
        maininfo = (TextView) findViewById(R.id.maininfo);
        descinfo = (TextView) findViewById(R.id.descinfo);
        locinfo = (TextView) findViewById(R.id.locinfo);
        pres = (TextView) findViewById(R.id.pres);
        humi = (TextView) findViewById(R.id.humi);
        maxtemp = (TextView) findViewById(R.id.maxtemp);
        mintemp = (TextView) findViewById(R.id.mintemp);
        forecast = (TextView) findViewById(R.id.forecast);
        feels_like = (TextView) findViewById(R.id.feels_like);
        layout = (ConstraintLayout) findViewById(R.id.layout);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        cd1 = (CardView) findViewById(R.id.cd1);
        iconview = (ImageView) findViewById(R.id.iconview);
        video = (VideoView) findViewById(R.id.video);
        swipeListener = new SwipeListener(layout);
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forecast.startAnimation(animation);
            }
        });

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
        int min_temp = 0, max_temp = 0, humid = 0, feelslike = 0;
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
        if (main.equals("Rain")) {
            layout.setBackgroundColor(Color.parseColor("#401b4965"));
            ll1.setBackgroundColor(Color.parseColor("#40000000"));
            locinfo.setTextColor(Color.parseColor("#FFFFFF"));
            cd1.setCardBackgroundColor(Color.parseColor("#40000000"));
            descinfo.setTextColor(Color.parseColor("#FFFFFF"));
            maininfo.setTextColor(Color.parseColor("#FFFFFF"));
            tempinfo.setTextColor(Color.parseColor("#FFFFFF"));
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rain);
            video.setVideoURI(uri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(0, 0);
                }
            });
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    video.start();

                }
            });
        } else if ((main.equals("Haze") || main.equals("Smoke") || main.equals("Mist")
                || main.equals("Dust") || main.equals("Fog") || main.equals("Sand")||main.equals("Ash")
        ||main.equals("Squall")||main.equals("Tornado"))) {
            layout.setBackgroundColor(Color.parseColor("#401b4965"));
            ll1.setBackgroundColor(Color.parseColor("#40000000"));
            locinfo.setTextColor(Color.parseColor("#FFFFFF"));
            cd1.setCardBackgroundColor(Color.parseColor("#40000000"));
            descinfo.setTextColor(Color.parseColor("#FFFFFF"));
            maininfo.setTextColor(Color.parseColor("#FFFFFF"));
            tempinfo.setTextColor(Color.parseColor("#FFFFFF"));
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.haze);
            video.setVideoURI(uri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(0, 0);
                }
            });
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    video.start();

                }
            });

        } else if (main.equals("Clouds")) {
            layout.setBackgroundColor(Color.parseColor("#62b6cb"));
            ll1.setBackgroundColor(Color.parseColor("#40000000"));
            locinfo.setTextColor(Color.parseColor("#edf6f9"));
            cd1.setCardBackgroundColor(Color.parseColor("#40000000"));
            descinfo.setTextColor(Color.parseColor("#edf6f9"));
            maininfo.setTextColor(Color.parseColor("#edf6f9"));
            tempinfo.setTextColor(Color.parseColor("#edf6f9"));
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cloud);
            video.setVideoURI(uri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(0, 0);
                }
            });
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    video.start();

                }
            });
        } else if (main.equals("Snow")) {
            layout.setBackgroundColor(Color.parseColor("#40cae9ff"));
            ll1.setBackgroundColor(Color.parseColor("#40cae9ff"));
        } else if (main.equals("Clear")) {
            layout.setBackgroundColor(Color.parseColor("#40bee9e8"));
            ll1.setBackgroundColor(Color.parseColor("#40000000"));
            cd1.setCardBackgroundColor(Color.parseColor("#40000000"));
            locinfo.setTextColor(Color.parseColor("#edf6f9"));
            descinfo.setTextColor(Color.parseColor("#edf6f9"));
            maininfo.setTextColor(Color.parseColor("#edf6f9"));
            tempinfo.setTextColor(Color.parseColor("#edf6f9"));
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.clear);
            video.setVideoURI(uri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(0, 0);
                }
            });
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    video.start();

                }
            });

        } else if (main.equals("Thunderstorm")) {
            layout.setBackgroundColor(Color.parseColor("#40003049"));
            ll1.setBackgroundColor(Color.parseColor("#40003049"));
        }

        locinfo.setText("\uD83D\uDCCD " + city);
        tempinfo.setText(String.valueOf(temp) + " °C");
        maininfo.setText(main);
        maxtemp.setText(String.valueOf(max_temp) + " °C");
        mintemp.setText(String.valueOf(min_temp) + " °C");
        feels_like.setText(String.valueOf(feelslike) + " °C");
        pres.setText(String.valueOf(String.format("%.2f", pressure * 0.750062)) + " mmHg");
        humi.setText(String.valueOf(humid) + " %");
        descinfo.setText(desc);
        String finalUrl = iconUrl + icon + "@4x.png";
        System.out.println(finalUrl);
        Picasso.with(MainActivity.this).load(finalUrl).into(iconview);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });


    }

    class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;
            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();
                    try {
                        if (Math.abs(xDiff) > Math.abs(yDiff)) {
                            if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                if (xDiff > 0) {
                                    Toast.makeText(MainActivity.this, "Swiped Right", Toast.LENGTH_SHORT).show();

                                } else {
//                                    Toast.makeText(MainActivity.this, "Swiped Left", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                                    startActivity(intent);
                                }
                                return true;
                            } else {

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
    }
}