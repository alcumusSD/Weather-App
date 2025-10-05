package com.example.openweatherproject;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<WeatherData>
{
    List list;
    Context context;
    int xmlResource;

    public CustomAdapter(MainActivity context, int resource, ArrayList<WeatherData> objects)
    {
        super(context, resource, objects);
        xmlResource = resource;
        list = objects;
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //return super.getView(position, convertView, parent); // return a view that displays the data at a specified position.
        // We are getting specific so we mute/delete this.
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);
        // root has to do with hierarchy of Views, keep null for our purposes ^
        TextView date = adapterLayout.findViewById(R.id.dateTextView);
        TextView minTemp = adapterLayout.findViewById(R.id.minTextView);
        TextView maxTemp = adapterLayout.findViewById(R.id.maxTextView);
        TextView desc = adapterLayout.findViewById(R.id.descTextView);
        ImageView image = adapterLayout.findViewById(R.id.imageView3);

        minTemp.setText("Min Temp: " + getItem(position).getMinTemp() +"°");
        date.setText(getItem(position).getDate());
        image.setImageResource(getItem(position).getImage());
        maxTemp.setText("Max Temp: "+getItem(position).getMaxTemp()+"°");
        desc.setText(getItem(position).getDesc());


        return adapterLayout;
    }
}
