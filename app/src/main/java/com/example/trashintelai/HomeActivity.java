package com.example.trashintelai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.trashintelai.adapter.ItemAdapter;
import com.example.trashintelai.adapter.PointAdapter;
import com.example.trashintelai.model.Points;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {


    private double latitude , longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private  LocationCallback locationCallback;
    private boolean isLocationSend = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.point_recyclerview_top);
        List<Points> list = new ArrayList<>();
       // Points points = new Points(R.drawable.ic_launcher_foreground,"cardborad","36 points");
        Points points2 = new Points(R.drawable.cardboard,"cardborad","36 points ");
        Points points3 = new Points(R.drawable.plastic,"plastic","18 points");
        Points points4 = new Points(R.drawable.paper,"paper","38 points");
        Points points5 = new Points(R.drawable.glass,"glass","3 points");
        Points points6 = new Points(R.drawable.metal,"metal","30 points");
        Points points7 = new Points(R.drawable.clothes,"clothes","35 points");
        Points points8 = new Points(R.drawable.organic_waste,"Organic Waste","45 points");
        Points points9 = new Points(R.drawable.trash,"Trash","35 points");


        list.add(points2);
        list.add(points3);
        list.add(points4);
        list.add(points5);
        list.add(points6);
        list.add(points7);
        list.add(points8);
        list.add(points9);


        PointAdapter adapter = new PointAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);



        RecyclerView recyclerViewitem = findViewById(R.id.item_recyclerview_top);
        List<Points> list2 = new ArrayList<>();
        //Points pointsitem = new Points(R.drawable.ic_launcher_foreground,"cardborad","36 points");
        Points points2item = new Points(R.drawable.cardboard,"cardborad","36 points X 0 points");
        Points points3item = new Points(R.drawable.plastic,"plastic","18 points");
        Points points4item = new Points(R.drawable.paper,"paper","38 points");
        Points points5item = new Points(R.drawable.glass,"glass","3 points");
        Points points6item = new Points(R.drawable.metal,"metal","30 points");
        Points points7item = new Points(R.drawable.clothes,"clothes","35 points");
        Points points8item = new Points(R.drawable.organic_waste,"Organic Waste","25 points");
        Points points9item = new Points(R.drawable.trash,"trash","45 points");



        list2.add(points2item);
        list2.add(points2item);
        list2.add(points3item);
        list2.add(points4item);
        list2.add(points5item);
        list2.add(points6item);
        list2.add(points7item);
        list2.add(points8item);
        list2.add(points9item);


        ItemAdapter itemadapter = new ItemAdapter(list);
        recyclerViewitem.setHasFixedSize(true);
        recyclerViewitem.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerViewitem.setAdapter(itemadapter);

        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
        findViewById(R.id.chatbot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Chatbot.class));
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, login.class));
                HomeActivity.this.finish();
            }
        });
        findViewById(R.id.share_location).setOnClickListener(view -> enableLocation());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }


    private void enableLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(getApplicationContext()).
                        checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                getLocation();
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(HomeActivity.this,
                                    100);
                        } catch (Exception ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(this, "location setting Unavailable", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    public void getLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                android.location.Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if (latitude >0.00 &&longitude > 0.00){
                        if (isLocationSend) {

                            sendLocation();

                        }
                    }
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        }
    }

    private void sendLocation() {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0); // full address
            String city = address.getLocality();
            String state = address.getAdminArea();
            String postalCode = address.getPostalCode();
           String completeAdress = addressLine + " "+ " "+city +" "+state+postalCode;
           Toast.makeText(getApplicationContext(),completeAdress,Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference().child("location")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("location").setValue(completeAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"location send successfully",Toast.LENGTH_SHORT).show();
                        }
                    });

            fusedLocationClient.removeLocationUpdates(locationCallback);
            isLocationSend=false;
        }else {
            Toast.makeText(getApplicationContext(),"No address Found",Toast.LENGTH_SHORT).show();
        }




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                getLocation();
            } else if (resultCode == Activity.RESULT_CANCELED)
            {

                Toast.makeText(this, "Need Location Enable to Start Activity", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        }
    }
}