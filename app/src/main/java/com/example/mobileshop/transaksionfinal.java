package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class transaksionfinal extends AppCompatActivity {
    Bundle pesan;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView transaksi_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksionfinal);
        Intent i = getIntent();
        pesan = i.getBundleExtra("bundle");
        Log.d("debug", String.valueOf(pesan));
        transaksi_status = findViewById(R.id.textView11);
        new HTTPRxeqTask().execute();
    }

    public void backhome(View view) {
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }

    private class HTTPRxeqTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                postTransaksi();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void postTransaksi() throws Exception{
            String url = "http://192.168.1.12:8000/transaksionlineberubah";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST"); //PUT / DELETE
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            gsc = GoogleSignIn.getClient(transaksionfinal.this,gso);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(transaksionfinal.this);
            String Name = account.getDisplayName();
            String Mail = account.getEmail();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("nama_toko",pesan.getString("namatoko"));
            jsonParam.put("alamat",pesan.getString("alamat"));
            jsonParam.put("username",Mail);
            jsonParam.put("bberas",pesan.getInt("beliberas"));
            jsonParam.put("bgaram",pesan.getInt("beligaram"));
            jsonParam.put("bgula",pesan.getInt("beligula"));
            jsonParam.put("btelur",pesan.getInt("belitelur"));
            jsonParam.put("bminyak",pesan.getInt("beliminyal"));
            jsonParam.put("sminyak",pesan.getInt("stockminyak"));
            jsonParam.put("stelur",pesan.getInt("stocktelur"));
            jsonParam.put("sgula",pesan.getInt("stockgula"));
            jsonParam.put("sgaram",pesan.getInt("stockgaram"));
            jsonParam.put("sberas",pesan.getInt("stockberas"));
            jsonParam.put("total",pesan.getInt("total"));
            jsonParam.put("alamatlanjut",pesan.getString("alamatlanjut"));

            byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
            OutputStream os = con.getOutputStream();
            Log.d("debug", "param");
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
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    transaksi_status.setText("Transaksi berhasil");
                }
            });
        }
    }
}