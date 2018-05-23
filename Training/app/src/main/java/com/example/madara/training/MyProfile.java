package com.example.madara.training;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.training.models.UserProfileResponse;
import com.example.madara.training.utils.Session;
import com.example.madara.training.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity {
    private final String TAG = "myprfile";
    private BroadcastReceiver broadcastReceiver;
    private Call<UserProfileResponse> getProfileCall;
    private TextView user_name, user_points, user_cards, user_garages, user_email, user_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
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
        user_name = (TextView) findViewById(R.id.txt_profile_username);
        user_points = (TextView) findViewById(R.id.txt_profile_points);
        user_cards = (TextView) findViewById(R.id.txt_profile_cards);
        user_garages = (TextView) findViewById(R.id.txt_profile_garages);
        user_email = (TextView) findViewById(R.id.txt_profile_useremail);
        user_phone = (TextView) findViewById(R.id.txt_profile_userphone);
        Log.e(TAG,"here");
        getUserProfile();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    private void getUserProfile(){
        final ProgressDialog progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getProfileCall = WebService.getInstance().getApi().getUserProfile(Session.getInstance().getUser().id);
        Log.e(TAG,getProfileCall.request().body().toString());
        getProfileCall.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                //Log.e(TAG,response.body().toString());

                    if(response.body().status==1){
                        progressDialog.cancel();
                        user_name.setText(response.body().user.user_name);
                        user_points.setText(response.body().user.user_points);
                        user_cards.setText(response.body().user.user_cards);
                        user_garages.setText(response.body().user.user_garages);
                        user_email.setText(response.body().user.user_email);
                        user_phone.setText(response.body().user.phone_number);
                    }


            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                Log.e(TAG,t.toString());
                progressDialog.cancel();
            }
        });

    }
}
