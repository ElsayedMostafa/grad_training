package com.example.madara.training;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.madara.training.utils.Session;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);
            Session.getInstance().logoutsecurity(this);
            final ProgressDialog progressDialog = new ProgressDialog(Logout.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logout...");
            progressDialog.show();
            Session.getInstance().logoutAndGoToLogin(Logout.this);
            progressDialog.cancel();
            finish();
    }
}
