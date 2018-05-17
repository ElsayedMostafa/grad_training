package com.example.madara.training;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import com.example.madara.training.adapters.ProfilePageAdapter;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GarageProfile extends AppCompatActivity {
    @BindView(R.id.viewpager_garage)
    ViewPager _viewpage_garage;
    @BindView(R.id.profile_viewpager) ViewPager _bottom_viewpager;
    @BindView(R.id.profile_tab_layout)
    TabLayout _profile_tab_layout;
    //@BindView(R.id.profile_details) TabItem _profile_details;
    //@BindView(R.id.profile_Reserve) TabItem _profile_reserve;
    //@BindView(R.id.profile_map) TabItem _profile_map;


//    @BindView(R.id.my_toolbar)
//    Toolbar _toolbar;
    private  Handler handler = new Handler();
    private Timer timer;
    int currentimage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_profile);
        ButterKnife.bind(this);
//        _toolbar.setTitleTextAppearance(this,R.style.TitleTextApperance);
//        _toolbar.setSubtitleTextAppearance(this,R.style.SubtitleTextApperance);
//        _toolbar.setTitleMarginBottom(120);
//        setSupportActionBar(_toolbar);
//        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        ProfilePageAdapter profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager(),_profile_tab_layout.getTabCount());
        profilePageAdapter.addFragment(new DetailsFragment(),"Details");
        profilePageAdapter.addFragment(new ReserveFragement(),"Reserve");
        profilePageAdapter.addFragment(new MapFragment(),"Map");
        _bottom_viewpager.setAdapter(profilePageAdapter);
       _profile_tab_layout.setupWithViewPager(_bottom_viewpager);
        getIncomingIntent();

    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("garageimgae")&&getIntent().hasExtra("garagename")){
            String url = getIntent().getStringExtra("garageimgae");
            String name = getIntent().getStringExtra("garagename");
            setContent(url,name);
        }
    }
    private void setContent(String url,String name){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls,width,350);
        _viewpage_garage.setAdapter(viewPagerAdapter);

//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentimage == imageUrls.length-1) {
//                    currentimage = 0;
//                }
//                _viewpage_garage.setCurrentItem(currentimage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer .schedule(new TimerTask() { // task to be scheduled
//
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);

        getSupportActionBar().setTitle(name);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
