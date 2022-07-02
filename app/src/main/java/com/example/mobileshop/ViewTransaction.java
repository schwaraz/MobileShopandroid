package com.example.mobileshop;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class ViewTransaction extends AppCompatActivity implements AdapterView.OnItemClickListener {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    String dummy;
    ListView listtoko;
    JSONObject arrObj;
    Button submit;
    ArrayList<String> resulttoko,resultalamat;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        Intent i = getIntent();
        ID = i.getStringExtra("pesan");
        Log.d("debug","data id masuk"+ID);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        listtoko = findViewById(R.id.listtoko);
        dummy=null;
        new HTTPReqTask().execute();
        listtoko.setOnItemClickListener(this);
        submit = findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dummy==null){
                    Toast.makeText(ViewTransaction.this,"pilih toko", LENGTH_LONG).show();
                    Log.d("debug","pilih toko");
                }
                else {
                    Intent i = new Intent(ViewTransaction.this,ShoppingPage.class);
                    i.putExtra("pesan",dummy);
                    startActivity(i);                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String getMahasiswa = resulttoko.get(position);
        dummy= getMahasiswa;
    }

    private class HTTPReqTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d("debug","httoreq");
            String[] result = null;
            try {
                Log.d("debug","try");
                getREST();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        private String[] getREST() throws Exception{
            String url = "http://192.168.1.12:8000/listtoko";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
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
            resulttoko = new ArrayList<String>();
            resultalamat = new ArrayList<>();
            String[] test1 = new String[myArray.length()];
            String[] test2 = new String[myArray.length()];
            for (int i=0; i < myArray.length();i++){
                Log.d("debug", "tes loop"+i);
                arrObj = myArray.getJSONObject(i);
                System.out.println("Title : " + arrObj.getString("NAMA_TOKO"));
                resulttoko.add(arrObj.getString("NAMA_TOKO"));
                resultalamat.add(arrObj.getString("ALAMAT_TOKO"));
                test1[i]= resulttoko.get(i);
                test2[i]= resultalamat.get(i);
            }
            Log.d("debug", String.valueOf(test2));
            Log.d("debug", String.valueOf(test1));



            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    CustomAdapter aAdapter = new CustomAdapter(getApplicationContext(),test1,test2);

                    listtoko.setAdapter(aAdapter);


                }
            });
            return null;
        }
    }


}