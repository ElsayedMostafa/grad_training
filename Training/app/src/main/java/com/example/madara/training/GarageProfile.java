package com.example.madara.training;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GarageProfile extends AppCompatActivity {
    @BindView(R.id.img_garage_profile_image)
    ImageView _garageimage;
    @BindView(R.id.tv_garage_profile_name)
    TextView _garagename;
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
        Log.e("here",String.valueOf(width));
        Picasso.get().load(url).resize(width,500)
                .centerCrop()
                .into(_garageimage);
        getSupportActionBar().setTitle(name);
        _garagename.setText(name);
    }
}
