package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
import java.util.ArrayList;

public class lihattransaksi extends AppCompatActivity {
    ListView listtransaksi;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ArrayList<String> listdate,listtoko,listberas,listminyak,listgula,listtelur,listgaram,listtotal,listalamat;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihattransaksi);
        listtransaksi = findViewById(R.id.tablelist);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        new HTTPReqTask().execute();
        Intent i = getIntent();
        ID = i.getStringExtra("pesan");
        Log.d("debug","data id masuk"+ID);
    }

    private class HTTPReqTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                getTransaksi();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void getTransaksi() throws Exception{
            String url = "http://192.168.1.12:8000/transaksionline";
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
            Log.d("debug","sebelum kirim id");
            Log.d("debug",ID);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(lihattransaksi.this);

            String Name = account.getDisplayName();
            String Mail = account.getEmail();
            jsonParam.put("username",Mail);
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
            listdate = new ArrayList<String>();
            listberas = new ArrayList<>();
            listgaram = new ArrayList<>();
            listtelur = new ArrayList<String>();
            listminyak = new ArrayList<String>();
            listgula = new ArrayList<String>();
            listtoko = new ArrayList<String>();
            listtotal = new ArrayList<String>();
            String[] date = new String[myArray.length()];
            String[] nama_toko = new String[myArray.length()];
            String[] alamat = new String[myArray.length()];
            String[] total = new String[myArray.length()];
            String[] daftar1 = new String[myArray.length()];
            String[] daftar2 = new String[myArray.length()];
            String[] daftar3 = new String[myArray.length()];
            String[] daftar4 = new String[myArray.length()];
            String[] daftar5 = new String[myArray.length()];
            JSONObject arrObj;
            for (int i=0; i < myArray.length();i++){
                daftar1[i]="";
                daftar2[i]="";
                daftar3[i]="";
                daftar4[i]="";
                daftar5[i]="";
                Log.d("debug", "tes loop"+i);
                arrObj = myArray.getJSONObject(i);
                Log.d("debug", String.valueOf(arrObj));
                System.out.println("Title : " + arrObj.getString("NAMA_TOKO"));
                date[i]= (String) arrObj.get("DATE");
                nama_toko[i]=(String) arrObj.get("NAMA_TOKO");
                alamat[i]=(String) arrObj.get("ALAMAT_PEMBELI");
                Log.d("debug", String.valueOf(listdate));
                Log.d("debug", String.valueOf(listtoko));
                Log.d("debug", String.valueOf(alamat[i]));
                listberas.add(String.valueOf((int) arrObj.get("BERAS")));
                Log.d("debug", String.valueOf(listberas));
                listgaram.add(String.valueOf((int) arrObj.get("GARAM")));
                Log.d("debug", String.valueOf(listgaram));
                listtelur.add(String.valueOf((int) arrObj.get("TELUR")));
                Log.d("debug", String.valueOf(listtelur));
                listminyak.add(String.valueOf((int) arrObj.get("MINYAK")));
                Log.d("debug", String.valueOf(listminyak));
                listgula.add(String.valueOf((int) arrObj.get("GULA")));
                Log.d("debug", String.valueOf(listgula));
                listtotal.add(String.valueOf((int) arrObj.get("TOTAL")));
                Log.d("debug", String.valueOf(listtotal));


                total[i]= listtotal.get(i);
                Log.d("debug", total[i]);
                if (listberas.get(i) != "0"){
                    daftar1[i]="Beras = "+listberas.get(i)+"KG";
                    Log.d("debug", daftar1[i]);
                }
                if (listtelur.get(i) != "0"){
                    daftar2[i]="Telur = "+listtelur.get(i)+"KG";
                    Log.d("debug", daftar2[i]);
                }
                if (listminyak.get(i) != "0"){
                    daftar3[i]="Minyak 100l ="+listminyak.get(i);
                    Log.d("debug", daftar3[i]);
                }

                if (listgula.get(i) != "0"){
                    daftar4[i]="gula ="+listgula.get(i)+"KG";
                }

                if (listgaram.get(i) != "0") {
                    daftar5[i] = "garam =" + listgaram.get(i) + "KG";
                    Log.d("debug", daftar5[i]);
                }
                for(int j=0;j<5;j++){
                    if (daftar1[i] == ""){
                        daftar1[i]=daftar2[i];
                    }
                    if (daftar2[i] == ""){
                        daftar2[i]=daftar3[i];
                    }
                    if (daftar3[i] == ""){
                        daftar3[i]=daftar4[i];
                    }
                    if (daftar4[i] == ""){
                        daftar4[i]=daftar5[i];
                    }
                }
            }
            Log.d("debug","masukfor");
            for (int k=0;k<myArray.length();k++) {
                Log.d("debug", date[k] + nama_toko[k] + alamat[k] + total[k] + daftar1[k] + daftar2[k] + daftar3[k] + daftar4[k] + daftar5[k]);
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    CustomAdapter2 aAdapter = new CustomAdapter2(getApplicationContext(),date,nama_toko,alamat,total,daftar1,daftar2,daftar3,daftar4,daftar5);
                    listtransaksi.setAdapter(aAdapter);

                }
            });



        }
    }
}