package com.example.madara.training;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
    @BindView(R.id.tv_tst) TextView testlocation;
    private final String TAG ="MainActivity";
    private GPSService locationObj;
    private Location mLocation;
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    private BroadcastReceiver broadcastReceiver;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dl = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);
        testlocation.setVisibility(View.INVISIBLE);
        locationObj = new GPSService(MainActivity.this,MainActivity.this,mLocationCallback);

//        _mycard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //30.791026,30.999071
//                //startActivity(new Intent(MainActivity.this,MyCards.class));
//                PackageManager packageManager = getPackageManager();
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=30.791026,30.999071");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if(mapIntent.resolveActivity(packageManager) != null){
//                    startActivity(mapIntent);
//                }
//                else{
//                    Toast.makeText(MainActivity.this,"Please install Google Maps",Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
        List<Garage> garagesList = new ArrayList<>();
        garagesList.add(new Garage(1, "Anwar Al Madinah", "0.6 km from centre", "12:13"));
        garagesList.add(new Garage(1, "Anwar Al Madinah", "0.6 km from centre", "12:13"));
        garagesList.add(new Garage(1, "Anwar Al Madinah", "0.6 km from centre", "12:13"));
//        for (int i = 0; i < 200; i++) {
//            garagesList.add(new Garage(1, "firstgarage", "1k", "12:13"));
//        }
        RecyclerView _recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GarageAdapter adapter = new GarageAdapter(garagesList);
        _recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s)
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
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
        unregisterReceiver(broadcastReceiver);
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
    public void selectItemDrawer(MenuItem menuItem){

    Class destionationClass = MainActivity.class;
    switch (menuItem.getItemId()){
        case R.id.it_profile:
            destionationClass = MyProfile.class;
            break;
        case R.id.it_cards:
            destionationClass = MyCards.class;
            break;
        case R.id.it_garages:
            destionationClass = MyGarages.class;
            break;
        case R.id.it_charge:
            destionationClass = Charge.class;
            break;
        case R.id.it_settings:
            destionationClass = Settings.class;
            break;
        case R.id.it_help:
            destionationClass = Help.class;
            break;
        case R.id.it_feedback:
            destionationClass = Sendfeedback.class;
            break;
        case R.id.it_about:
            destionationClass = About.class;
            break;
        case R.id.it_logout:
            destionationClass = Logout.class;
            break;
    }
    try {
        Intent i = new Intent(MainActivity.this,destionationClass);
        startActivity(i);
//        LayoutInflater inflater = getLayoutInflater();
//        RelativeLayout container = (RelativeLayout) findViewById(R.id.inflate);
//        container.removeAllViews();
//        inflater.inflate(R.layout.activity_my_cards, container,true);
        dl.closeDrawers();

    }
    catch (Exception e){
        e.printStackTrace();
    }
}
    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(MainActivity.this,"here",Toast.LENGTH_LONG).show();
                Log.e("here","here");
                selectItemDrawer(item);
                return true;
            }
        });
    }

}
