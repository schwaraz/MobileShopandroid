package com.example.mobileshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter2 extends BaseAdapter {
    Context c;
    String tanggal[];
    String datatoko[];
    String databelanja1[];
    String databelanja2[];
    String databelanja3[];
    String databelanja4[];
    String databelanja5[];
    String dataalamat[];
    String datatotal[];
    LayoutInflater inflater;
    public CustomAdapter2(Context c, String[] data, String[] toko,String[] alamat, String[] harga, String[] belanja1, String[] belanja2, String[] belanja3, String[] belanja4, String[] belanja5) {
        this.c = c;
        Log.d("debug","masukk custom1");
        this.tanggal = data;
        this.datatoko = toko;
        this.databelanja1=belanja1;
        this.databelanja2=belanja2;
        this.databelanja3=belanja3;
        this.databelanja4=belanja4;
        this.databelanja5=belanja5;
        this.datatotal=harga;
        this.dataalamat=alamat;
        Log.d("debug","masukk custom3");
        inflater = LayoutInflater.from(c);
        Log.d("debug", String.valueOf(datatoko.length));
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
        Log.d("debug","masukk custom2");
        convertView = inflater.inflate(R.layout.activity_displayviewtransaksi,null);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        Log.d("debug","masukk custom2");
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView toko = (TextView) convertView.findViewById(R.id.nama_toko);
        TextView total = (TextView) convertView.findViewById(R.id.total);
        TextView barang1 = (TextView) convertView.findViewById(R.id.produk1);
        TextView barang2 = (TextView) convertView.findViewById(R.id.produk2);
        TextView barang3 = (TextView) convertView.findViewById(R.id.produk3);
        TextView barang4 = (TextView) convertView.findViewById(R.id.produk4);
        TextView barang5 = (TextView) convertView.findViewById(R.id.produk5);
        date.setText(tanggal[position]);
        alamat.setText(dataalamat[position]);
        toko.setText(datatoko[position]);
        total.setText(datatotal[position]);
        barang1.setText(databelanja1[position]);
        barang2.setText(databelanja2[position]);
        barang3.setText(databelanja3[position]);
        barang4.setText(databelanja4[position]);
        barang5.setText(databelanja5[position]);
        Log.d("debug","masukk custom2");
        Log.d("debug",tanggal[position]);
        return convertView;
    }
}
