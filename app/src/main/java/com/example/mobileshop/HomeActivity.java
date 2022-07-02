package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    TextView name;
    Button logout,transaksi,belanja;
    String myUrl = "http://192.168.1.12";
    String ID;
    String notes;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name = findViewById(R.id.name);
        logout = findViewById(R.id.logout);
        belanja = findViewById(R.id.belanja);
        transaksi = findViewById(R.id.transaksi);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String Name = account.getDisplayName();
            name.setText(Name);

        }
        /////////////////////////////////////////////////////////////////
        new HTTPReqTask().execute();
        ///////////////////////////////////////////////////////////////
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();

            }
        });
        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, lihattransaksi.class);
                i.putExtra("pesan",ID);
                startActivity(i);
            }
        });
        belanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    transaksi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void transaksi() {
        Log.d("debug","MAU MASUK INTENT");
        Log.d("debug",ID);
        Intent transaction = new Intent(this, ViewTransaction.class);
        transaction.putExtra("pesan",ID);
        startActivity(transaction);
    }


    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private class HTTPReqTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d("debug","httoreq");
            String[] result = null;
            try {
                Log.d("debug","try");
                getpost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String[] getpost() throws Exception{
            Log.d("debug","msk post");
            String url = "http://192.168.1.12:8000/createuser";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            JSONObject jsonParam = new JSONObject();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);

            String Name = account.getDisplayName();
            String Mail = account.getEmail();
            Log.d("debug",Name);
            Log.d("debug",Mail);

            jsonParam.put("name", Name);
            jsonParam.put("username", Mail);
            byte[] jsData = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
            OutputStream os = con.getOutputStream();
            Log.d("debug", String.valueOf(jsonParam));
            os.write(jsData);
            Log.d("debug","stop4");
            int responseCode = con.getResponseCode();
            System.out.println("Send Get Request to : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String input;
            StringBuilder response = new StringBuilder();
            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            try {
                getData(response);
                Log.d("debug", "try");
            }catch (Exception e){
                e.printStackTrace();
                Log.d("debug", "excep");
            }
            in.close();
            os.flush();
            os.close();
            return new String[0];
        }

        private JSONObject getData(StringBuilder response) throws JSONException{
            JSONArray myArray = new JSONArray(response.toString());
            JSONObject arrObj = myArray.getJSONObject(0);
            ArrayList resulttoko=new ArrayList<>();;
            Log.d("debug","id" );
            ID= arrObj.getString("ID");
            return arrObj;

        }
    }
}