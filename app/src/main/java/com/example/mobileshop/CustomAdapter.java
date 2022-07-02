package com.example.mobileshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context c;
    String datatoko[];
    String dataalamat[];
    LayoutInflater inflater;


    public CustomAdapter(Context c, String[] data, String[] alamat) {
        this.c = c;
        this.datatoko = data;
        this.dataalamat = alamat;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        Log.d("debug", String.valueOf(datatoko.length));
        return datatoko.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("debug","masukk custom20");
        convertView = inflater.inflate(R.layout.activity_listviewadapter,null);
        TextView toko = (TextView) convertView.findViewById(R.id.tv1);
        TextView alamat = (TextView) convertView.findViewById(R.id.tv2);
        toko.setText(datatoko[position]);
        alamat.setText(dataalamat[position]);
        return convertView;
    }

}
