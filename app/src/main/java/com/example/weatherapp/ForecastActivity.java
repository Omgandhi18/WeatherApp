package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastActivity extends AppCompatActivity {
    ListView list;
    private GpsTracker gpsTracker;
//    SwipeListener swipeListener;
    LinearLayout layout1;
    private int REL_SWIPE_MIN_DISTANCE;
    private int REL_SWIPE_MAX_OFF_PATH;
    private int REL_SWIPE_THRESHOLD_VELOCITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        layout1=(LinearLayout)findViewById(R.id.layout1);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        REL_SWIPE_MIN_DISTANCE = (int)(120.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_MAX_OFF_PATH = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
        REL_SWIPE_THRESHOLD_VELOCITY = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);

//        swipeListener = new SwipeListener(layout1);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

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
        String weather = "";
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
        ArrayList<String>icon
                = new ArrayList<String>(20);

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
            JSONObject Jobj3=null;
            JSONObject Jobj1 = new JSONObject(jsonData);


            JSONArray jsonArray = Jobj1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                date.add(jsonArray.getJSONObject(i).getString("dt_txt").substring(5,10));
                time.add(jsonArray.getJSONObject(i).getString("dt_txt").substring(11,16));

                main = jsonArray.getJSONObject(i).getString("main");
                Jobj2= new JSONObject(main);
                temp.add(Jobj2.getInt("temp"));
                weather=jsonArray.getJSONObject(i).getString("weather");
                JSONArray jarr=new JSONArray(weather);
                for (int j = 0; j < jarr.length(); j++){
                    icon.add(jarr.getJSONObject(j).getString("icon"));
                }

            }



            System.out.println(Jobj2);
            System.out.println(weather.getClass().getSimpleName());
            System.out.println(temp);
            System.out.println(icon);
            System.out.println(date);
            System.out.println(time);




        } catch (Exception e) {
            e.printStackTrace();
        }
        MyListAdapter adapter=new MyListAdapter(this, date, time,temp,icon);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }};
        list.setOnTouchListener(gestureListener);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String str = MessageFormat.format("Item long clicked = {0,number}", position);
                Toast.makeText(ForecastActivity.this, str, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
    private void myOnItemClick(int position) {
        String str = MessageFormat.format("Item clicked = {0,number}", position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void onLTRFling() {
        Toast.makeText(this, "Left-to-right fling", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ForecastActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
    }

    private void onRTLFling() {
        Toast.makeText(this, "Right-to-left fling", Toast.LENGTH_SHORT).show();


    }
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        // Detect a single-click and call my own handler.
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            int pos = list.pointToPosition((int)e.getX(), (int)e.getY());
            myOnItemClick(pos);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
                return false;
            if(e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
                onRTLFling();
            }  else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
                onLTRFling();
            }
            return false;
        }

    }


//    private class SwipeListener implements View.OnTouchListener {
//        GestureDetector gestureDetector;
//
//        SwipeListener(View view) {
//            int threshold = 100;
//            int velocity_threshold = 100;
//            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onDown(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                    float xDiff = e2.getX() - e1.getX();
//                    float yDiff = e2.getY() - e1.getY();
//                    try {
//                        if (Math.abs(xDiff) > Math.abs(yDiff)) {
//                            if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
//                                if (xDiff > 0) {
////                                    Toast.makeText(ForecastActivity.this, "Swiped Right", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(ForecastActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
//                                } else {
////                                    Toast.makeText(MainActivity.this, "Swiped Left", Toast.LENGTH_SHORT).show();
//
//                                }
//                                return true;
//                            } else {
//
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return false;
//                }
//            };
//            gestureDetector = new GestureDetector(listener);
//            view.setOnTouchListener(this);
//        }
//
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            return gestureDetector.onTouchEvent(motionEvent);
//        }
//    }
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