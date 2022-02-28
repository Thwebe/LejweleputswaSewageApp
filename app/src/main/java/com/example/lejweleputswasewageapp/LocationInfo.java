package com.example.lejweleputswasewageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationInfo extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    TextView tvLatitude, tvLongitude;
    private static final int LocationRequestCode = 1;

    int index;
    Double la = (PersonApplication.userLocations.get(index).getLatitude());
    Double lo = 26.7727119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_info);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.Longitude);

        index = getIntent().getIntExtra("index", -1);

        //tvLongitude.setText(PersonApplication.userLocations.get(index).getLongitude());
        //Double lo= 26.7727119;
        // double lo= (PersonApplication.userLocations.get(index).getLongitude());
        //tvLatitude.setText(""+ lo);
        // tvLatitude.setText("" + la);
        // tvLongitude.setText(PersonApplication.userLocations.get(index).getCountryName());

        client = LocationServices.getFusedLocationProviderClient(LocationInfo.this);

        if (ActivityCompat.checkSelfPermission(LocationInfo.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationInfo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationRequestCode);


            //   getCurrentLocation();
        } else {

        }

    }
/*
    private void getCurrentLocation() {
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
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // Double la=27.2565474;

                            LatLng latLng = new LatLng(27.2565474, 26.9994563);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Problem");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.addMarker(options);


                        }
                    });
                }

            }
        });
    }

 */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                {
                    // getCurrentLocation();
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
                    Task<Location> task = client.getLastLocation();

                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location==null)
                        {
                            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    // Double lo=27.2565474;

                                    LatLng latLng= new LatLng(27.2565474,26.9994563);
                                    MarkerOptions options=new MarkerOptions().position(latLng)
                                            .title("Problem");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                    googleMap.addMarker(options);

                                }
                            });
                        }

                    }
                });
                Toast.makeText(LocationInfo.this,"Inside onrequestpermision",Toast.LENGTH_LONG).show();
            }
        }
    }
}
}