package com.example.mobileshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomAdapter2 extends BaseAdapter {
    Context c;
    String tanggal[];
    String datatoko[];
    String databelanja1[];
    String databelanja2[];
    String databelanja3[];
    String databelanja4[];
    String databelanja5[];
    String total[];
    LayoutInflater inflater;
    public CustomAdapter2(Context c, String[] data, String[] toko, String[] harga, String[] belanja1, String[] belanja2, String[] belanja3, String[] belanja4, String[] belanja5) {
        this.c = c;
        this.tanggal = data;
        this.datatoko = toko;
        this.databelanja1=belanja1;
        this.databelanja2=belanja2;
        this.databelanja3=belanja3;
        this.databelanja4=belanja4;
        this.databelanja5=belanja5;
        this.total=harga;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }
}
