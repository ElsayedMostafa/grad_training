package com.example.madara.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.madara.training.utils.Session;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e("splash","test");
        final boolean i =  Session.getInstance().isUserLoggedIn();
        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    if(i){
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}
