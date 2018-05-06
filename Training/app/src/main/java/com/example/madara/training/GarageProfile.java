package com.example.madara.training;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GarageProfile extends AppCompatActivity {
    @BindView(R.id.viewpager_garage)
    ViewPager _viewpage_garage;
    @BindView(R.id.tv_garage_profile_name)
    TextView _garagename;
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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls,width,300);
        _viewpage_garage.setAdapter(viewPagerAdapter);

        getSupportActionBar().setTitle(name);
        _garagename.setText(name);
    }
}
