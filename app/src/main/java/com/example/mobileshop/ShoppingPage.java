package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ShoppingPage extends AppCompatActivity {
String nama_toko,alamat;
EditText bberas,bminyak,bgula,btelur,bgaram,alamatpenerima;
TextView sberas,sminyak,sgula,stelur,sgaram,hberas,hminyak,hgula,htelur,hgaram,nm_toko,total;
    int a,b,c,d,e,sa,sb,sc,sd,se,ha,hb,hc,hd,he;
    int totalbelanja;
    boolean check= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_page);
        Intent i = getIntent();
        nama_toko = i.getStringExtra("pesan");
        Log.d("debug",nama_toko);
        bberas = findViewById(R.id.bberas);
        btelur = findViewById(R.id.btelur);
        bminyak = findViewById(R.id.bminyak);
        bgaram = findViewById(R.id.bgaram);
        bgula = findViewById(R.id.bgula);
        sberas = findViewById(R.id.Sberas);
        stelur = findViewById(R.id.Stelur);
        sminyak = findViewById(R.id.Sminyak);
        sgaram = findViewById(R.id.Sgaram);
        sgula = findViewById(R.id.Sgula);
        hberas = findViewById(R.id.hberas);
        htelur = findViewById(R.id.htelur);
        hgaram = findViewById(R.id.hgaram);
        hgula =findViewById(R.id.hgula);
        hminyak = findViewById(R.id.hminyak);
        nm_toko = findViewById(R.id.nama);
        total = findViewById(R.id.totalbeli);
        alamatpenerima = findViewById(R.id.alamatpenerima);
        new HTTPReqTask().execute();

    }

    public Object belanja(View view) {
    if (bberas.getText().toString().trim().length() > 0){
        a = Integer.parseInt(String.valueOf(bberas.getText())) ;    }
    else{
    a =0;
    }
    if (bgula.getText().toString().trim().length() > 0){
        b = Integer.parseInt(String.valueOf(bgula.getText())) ;    }
    else{
        b =0;
    }
    if (bminyak.getText().toString().trim().length() > 0){
        c = Integer.parseInt(String.valueOf(bminyak.getText())) ;    }
    else{
        c =0;
    }
    if (bgaram.getText().toString().trim().length() > 0){
        d = Integer.parseInt(String.valueOf(bgaram.getText())) ;    }
    else{
        d =0;
    }
    if (btelur.getText().toString().trim().length() > 0){
        e = Integer.parseInt(String.valueOf(btelur.getText())) ;    }
    else{
        e =0;
    }
    sa = Integer.parseInt(String.valueOf(sberas.getText()));
    sb = Integer.parseInt(String.valueOf(sgula.getText())) ;
    sc = Integer.parseInt(String.valueOf(sminyak.getText())) ;
    sd = Integer.parseInt(String.valueOf(sgaram.getText())) ;
    se = Integer.parseInt(String.valueOf(stelur.getText())) ;
    ha = Integer.parseInt(String.valueOf(hberas.getText()));
    hb = Integer.parseInt(String.valueOf(hgula.getText())) ;
    hc = Integer.parseInt(String.valueOf(hminyak.getText())) ;
    hd = Integer.parseInt(String.valueOf(hgaram.getText())) ;
    he = Integer.parseInt(String.valueOf(htelur.getText())) ;
    if (a>sa){
        Toast.makeText(this,"Stok beras tidak mencukupi",Toast.LENGTH_LONG).show();
        return null;
    }
    if (b>sb){
        Toast.makeText(this,"Stok gula tidak mencukupi",Toast.LENGTH_LONG).show();
        return null;
    }
        if (c>sc){
            Toast.makeText(this,"Stok minyak tidak mencukupi",Toast.LENGTH_LONG).show();
            return null;
        }
        if (d>sd){
            Toast.makeText(this,"Stok garam, tidak mencukupi",Toast.LENGTH_LONG).show();
            return null;
        }
        if (e>se){
            Toast.makeText(this,"Stok telur tidak mencukupi",Toast.LENGTH_LONG).show();
            return null;
        }
        if (a==0&b==0&c==0&d==0&e==0){
            Toast.makeText(this,"input barang belanja",Toast.LENGTH_LONG).show();
            return null;
        }
        totalbelanja = (a*ha)+(b*hb)+(c*hc)+(d*hd)+(e*he);
        total.setText(String.valueOf(totalbelanja));
        check=true;
        return null;
    }

    public void checkout(View view) {
        if (check=false)
        {
            Toast.makeText(this,"input barang belanja",Toast.LENGTH_LONG).show();

            return;
        }
        if (alamatpenerima.getText().toString().trim().length() > 0){
            alamat = alamatpenerima.getText().toString();
        }
        else{
            Toast.makeText(this,"input alamat penerima",Toast.LENGTH_LONG).show();
            return;
        }
        sa-=a;
        sb-=b;
        sc-=c;
        sd-=d;
        se-=e;
        Bundle pesan = new Bundle();
        pesan.putInt("beliberas",a);
        pesan.putInt("beligula",b);
        pesan.putInt("beliminyak",c);
        pesan.putInt("beligaram",d);
        pesan.putInt("belitelur",e);
        pesan.putInt("stockberas",sa);
        pesan.putInt("stockgula",sb);
        pesan.putInt("stockminyak",sc);
        pesan.putInt("stockgaram",sd);
        pesan.putInt("stocktelur",se);
        pesan.putInt("total",totalbelanja);
        pesan.putString("namatoko",nama_toko);
        pesan.putString("alamat",alamat);
        Log.d("debug", String.valueOf(pesan));
        Intent map = new Intent(this, com.example.mobileshop.map.class);
        map.putExtra("bundle",pesan);
        startActivity(map,pesan);
    }
    private void getstock() throws Exception{
        String url = "http://192.168.1.12:8000/restock";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        JSONObject jsonParam = new JSONObject();

        jsonParam.put("nama_toko",nama_toko);
        byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
        OutputStream os = con.getOutputStream();
        Log.d("debug", String.valueOf(jsonParam));
        os.write(jsData);
        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuffer response = new StringBuffer();
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();
        System.out.println("Data : \n" +response.toString());
        Log.d("debug", "data masuk");
        JSONArray myArray = new JSONArray(response.toString());
        JSONObject arrObj = myArray.getJSONObject(0);
        String a,b,c,d,e,f,g,h,i,k,l;
        a = String.valueOf((Integer) arrObj.get("BERAS"));
        b = String.valueOf((Integer) arrObj.get("GARAM"));
        c = String.valueOf((Integer) arrObj.get("GULA"));
        d = String.valueOf((Integer) arrObj.get("MINYAK"));
        e = String.valueOf((Integer) arrObj.get("TELUR"));
        f = String.valueOf((Integer) arrObj.get("HARGA_GARAM"));
        g = String.valueOf((Integer) arrObj.get("HARGA_GULA"));
        h = String.valueOf((Integer) arrObj.get("HARGA_MINYAK"));
        i = String.valueOf((Integer) arrObj.get("HARGA_TELUR"));
        k = String.valueOf((Integer) arrObj.get("HARGA_BERAS"));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    sberas.setText(a);
                    sgaram.setText(b);
                    sgula.setText(c);
                    sminyak.setText(d);
                    stelur.setText(e);
                    hgaram.setText(f);
                    hgula.setText(g);
                    hminyak.setText(h);
                    htelur.setText(i);
                    hberas.setText(k);
                    nm_toko.setText(nama_toko);

                }
            });

    }


    private class HTTPReqTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                getstock();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



    }
}
