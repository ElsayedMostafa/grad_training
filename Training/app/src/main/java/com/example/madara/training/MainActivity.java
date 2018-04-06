package com.example.madara.training;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.training.adapters.GarageAdapter;
import com.example.madara.training.models.Garage;
import com.example.madara.training.services.GPSService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// api key AIzaSyBDxBh5UbBe6JjGiuD0bzTI1YmfhDTKq00
public class MainActivity extends AppCompatActivity {
    //@BindView(R.id.tv_username)
    //TextView _Tv_Username;
    //@BindView(R.id.btn_getlocation)
    //Button _btn_getlocation;
    @BindView(R.id.mycard)
    Button _mycard;
    @BindView(R.id.tv_tst) TextView testlocation;
    private final String TAG ="MainActivity";
    private GPSService locationObj;
    private Location mLocation;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        testlocation.setVisibility(View.INVISIBLE);
        locationObj = new GPSService(MainActivity.this,MainActivity.this,mLocationCallback);


        _mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //30.791026,30.999071
                //startActivity(new Intent(MainActivity.this,MyCards.class));
                PackageManager packageManager = getPackageManager();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=30.791026,30.999071");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(packageManager) != null){
                    startActivity(mapIntent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please install Google Maps",Toast.LENGTH_LONG).show();
                }

            }
        });
        List<Garage> garagesList = new ArrayList<>();
        garagesList.add(new Garage(1, "firstgarage", "1k", "12:13"));
//        for (int i = 0; i < 200; i++) {
//            garagesList.add(new Garage(1, "firstgarage", "1k", "12:13"));
//        }
        RecyclerView _recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GarageAdapter adapter = new GarageAdapter(garagesList);
        _recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!locationObj.checkPermissions()) {
            locationObj.requestPermissions();
        } else {
            locationObj.getLastLocation();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        if(mLocation == null){
//            locationObj.startLocationUpdates();
//        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        locationObj.stopLocationUpdates();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationObj.stopLocationUpdates();
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.e(TAG,"null");
                locationObj.startLocationUpdates();
            }
            for (Location location : locationResult.getLocations()) {
                // Update UI with location data
                // ...
                mLocation = location;
                testlocation.setVisibility(View.VISIBLE);
                testlocation.setText(location.toString());
                progressBar.setVisibility(View.GONE);
                locationObj.stopLocationUpdates();

            }
        }
    };

}
