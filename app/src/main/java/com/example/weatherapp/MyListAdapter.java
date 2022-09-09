package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> date;
    private final ArrayList<String> time;
    private final ArrayList<Integer> temp;
    private final ArrayList<String> icon;
    String iconUrl = "https://openweathermap.org/img/wn/";

    public MyListAdapter(Activity context, ArrayList<String> date, ArrayList<String> time, ArrayList<Integer> temp, ArrayList<String> icon) {
        super(context, R.layout.mylist, date);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.icon = icon;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView date1 = (TextView) rowView.findViewById(R.id.date1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView time1 = (TextView) rowView.findViewById(R.id.time1);
        TextView temp1 = (TextView) rowView.findViewById(R.id.temp1);
        String finalUrl = iconUrl + icon.get(position) + "@4x.png";
        date1.setText(date.get(position));
        temp1.setText(String.valueOf(temp.get(position))+" °C");

        Picasso.with(context).load(finalUrl).into(imageView);
//        imageView.setImageResource(Integer.parseInt(icon.get(position)));
        time1.setText(time.get(position));

        return rowView;

    }


    ;
}