package com.example.madara.training;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.madara.training.adapters.GarageAdapter;
import com.example.madara.training.models.Garage;
import com.example.madara.training.services.GPSService;

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
    private final String TAG ="MainActivity";
    private GPSService locationObj;
    private Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        locationObj = new GPSService(MainActivity.this,MainActivity.this);
//        _btn_getlocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLocation = locationObj.getLocation();
//                if(mLocation == null) {
//                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_LONG).show();
//                    //locationObj.getLastLocation();
//                    //mLastLocation = locationObj.getLocation();
//                    locationObj.startLocationUpdates();
//
//
//                }
//                else {
//                    Toast.makeText(MainActivity.this, mLocation.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        //locationObj.startLocationUpdates();
        //mLocation = locationObj.getLocation();
        _mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyCards.class));
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
        if(mLocation == null){
            locationObj.startLocationUpdates();
        }
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



}
