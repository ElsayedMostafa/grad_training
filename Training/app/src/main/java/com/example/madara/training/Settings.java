package com.example.madara.training;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private ImageView name_setting, email_setting, password_setting, phone_setting;
    private TextView tv_name, tv_email, tv_password, tv_phone;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        tv_name = (TextView)findViewById(R.id.tv_setting_name);
        tv_email = (TextView) findViewById(R.id.tv_setting_email);
        tv_password = (TextView) findViewById(R.id.tv_setting_password);
        tv_phone = (TextView) findViewById(R.id.tv_setting_phone);
        name_setting = (ImageView) findViewById(R.id.img_setting_name);
        email_setting = (ImageView) findViewById(R.id.img_setting_email);
        password_setting = (ImageView) findViewById(R.id.img_setting_password);
        phone_setting = (ImageView) findViewById(R.id.img_setting_phone);
        name_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Settings.this,EditSetting.class);
                setting_intent.putExtra("check",0);
                setting_intent.putExtra("action_value","Enter your name");
                setting_intent.putExtra("Value",tv_name.getText().toString());

                startActivity(setting_intent);
            }
        });
        email_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Settings.this,EditSetting.class);
                setting_intent.putExtra("check",1);
                setting_intent.putExtra("action_value","Enter your email");
                setting_intent.putExtra("Value",tv_email.getText().toString());
                startActivity(setting_intent);
            }
        });
        password_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Settings.this,EditSetting.class);
                setting_intent.putExtra("check",2);
                setting_intent.putExtra("action_value","Enter your password");
                setting_intent.putExtra("Value","");
                startActivity(setting_intent);
            }
        });
        phone_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Settings.this,EditSetting.class);
                setting_intent.putExtra("check",3);
                setting_intent.putExtra("action_value","Enter your phone number");
                setting_intent.putExtra("Value",tv_phone.getText().toString());
                startActivity(setting_intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Settings.this,MainActivity.class));
        finish();
    }
}
