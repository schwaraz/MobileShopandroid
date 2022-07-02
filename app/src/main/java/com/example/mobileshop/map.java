package com.example.mobileshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class map extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    boolean permisiterima =true;
    String dummy;
    GoogleMap mGooglemap;
    FloatingActionButton curlock;
    EditText search;
    ImageView icon;
    Bundle pesan;
    TextView alamat;
    private FusedLocationProviderClient client;
    private int GPS_REQUEST_CODE = 9001;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    Geocoder geocoder;
    List<Address> addresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        pesan = new Bundle();
        Intent map = getIntent();
        pesan = map.getBundleExtra("bundle");
        Log.d("debug", String.valueOf(pesan));
        Log.d("debug", pesan.getString("alamat"));
        curlock = findViewById(R.id.curlock);
        icon = findViewById(R.id.search_mag_icon);
        search = findViewById(R.id.search);
        alamat = findViewById(R.id.textView8);
        initMap();
        client = new FusedLocationProviderClient(this);
        curlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcurrentlock();
            }
        });
        icon.setOnClickListener(this::geolocate);
        search.setText(pesan.getString("alamat"));
//        gotoalamat(pesan.getString("alamat"));

    }

    private void gotoalamat(String alamat) {
        String locationname = alamat;
        Geocoder geocoder1 = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> addressList = geocoder1.getFromLocationName(locationname,1);
            if (addressList.size()>0){
                Address address = addressList.get(0);
                gotoLocation(address.getLatitude(),address.getLongitude());
                mGooglemap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));
                getCompleteAddressString(address.getLatitude(),address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void geolocate(View view) {
        String locationname = search.getText().toString();
        Geocoder geocoder1 = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> addressList = geocoder1.getFromLocationName(locationname,1);
            if (addressList.size()>0){
                Address address = addressList.get(0);
                gotoLocation(address.getLatitude(),address.getLongitude());
                mGooglemap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));
                getCompleteAddressString(address.getLatitude(),address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap() {
        if (permisiterima) {
            if (isGPSenable()){
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
                supportMapFragment.getMapAsync(this);
            }

        }
    }
    private boolean isGPSenable(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnable){
            return true;
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS PERMISION REQURED")
                    .setMessage("GPS REQURED TO GET YOUR LOCATION")
                    .setPositiveButton("YES", ((dialogInterface, i) ->{
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }))
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    private void getcurrentlock() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Location location = task.getResult();
                getCompleteAddressString(location.getLatitude(),location.getLongitude());
                gotoLocation(location.getLatitude(),location.getLongitude());
//                mGooglemap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));

            }
        });
    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    Log.d("debug",returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                Log.w("debug", strReturnedAddress.toString());
                Log.w("debug", String.valueOf(strReturnedAddress.getClass()));
                alamat.setText(strReturnedAddress.toString());
                dummy = strReturnedAddress.toString();
            } else {
                Log.w("debug", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("debug", "Canont get Address!");
        }
        return strAdd;
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mGooglemap.moveCamera(cameraUpdate);
        mGooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGooglemap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("debug","masuk if");
            Log.d("debug", String.valueOf(mGooglemap));
            return;
        }
        mGooglemap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(providerEnable) {
                Toast.makeText(this,"GPS ENABLE",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"GPS not ENABLE",Toast.LENGTH_SHORT).show();
            }
            }
        }

    public void checkout(View view) {
        pesan.putString("alamatlanjut",dummy);
        Intent gonext = new Intent(this,transaksionfinal.class);
        gonext.putExtra("bundle",pesan);
        startActivity(gonext);
    }
}