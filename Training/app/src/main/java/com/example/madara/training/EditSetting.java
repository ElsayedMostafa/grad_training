package com.example.madara.training;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madara.training.models.MainResponse;
import com.example.madara.training.models.User;
import com.example.madara.training.utils.Session;
import com.example.madara.training.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSetting extends AppCompatActivity {
    private final String TAG = "EditSetting";
    private BroadcastReceiver broadcastReceiver;
    private EditText setting_first, setting_second, setting_last;
    private Button btn_cancel_setting, btn_ok_setting;
    private String action_value, value;
    private int check, userid;
    private Call<MainResponse> mChangeInfoCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setting);
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
        userid = Session.getInstance().getUser().id;
        btn_cancel_setting =(Button) findViewById(R.id.btn_editsetting_cancel);
        btn_ok_setting = (Button) findViewById(R.id.btn_editsetting_ok);
        setting_first = (EditText) findViewById(R.id.et_setting_first);
        setting_second = (EditText) findViewById(R.id.et_setting_second);
        setting_last = (EditText) findViewById(R.id.et_setting_last);
        if(getIntent() != null ){
            action_value = getIntent().getStringExtra("action_value");
            check = getIntent().getIntExtra("check",0);
            value = getIntent().getStringExtra("Value");
            getSupportActionBar().setTitle(action_value);
            setting_first.append(value);
            if(check == 2){
                //android:inputType="textPassword"
                setting_first.setTransformationMethod(PasswordTransformationMethod.getInstance());
                setting_second.setTransformationMethod(PasswordTransformationMethod.getInstance());
                setting_last.setTransformationMethod(PasswordTransformationMethod.getInstance());
                setting_first.setHint("old password");
                setting_second.setHint("new password");
                setting_last.setHint("repeat password");
                setting_second.setVisibility(View.VISIBLE);
                setting_last.setVisibility(View.VISIBLE);
            }
            else if(check==1){
                setting_second.setVisibility(View.VISIBLE);
                setting_second.setTransformationMethod(PasswordTransformationMethod.getInstance());
                setting_second.setHint("Enter your password");
            }
        }
        btn_cancel_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditSetting.this,Settings.class));
                finish();
            }
        });
        btn_ok_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 0)
                    change_name();
                else if(check==1)
                    change_email();
                else if(check==2)
                    change_password();
                else if(check==3)
                    change_phone();
            }
        });
    }
    private void change_name(){
        String newName = setting_first.getText().toString();
        if (newName.isEmpty() || newName.length() < 3) {
            setting_first.setError("Username should be at least 3 characters");
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(EditSetting.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Name...");
            progressDialog.show();
            final User user = new User();
            user.id = userid;
            user.username = newName;
            mChangeInfoCall = WebService.getInstance().getApi().changeName(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    try{
                        if(response.body().status==0){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                        else if(response.body().status==1){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }catch (Exception e){
                        Toast.makeText(EditSetting.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private void change_email(){
        String newEmail = setting_first.getText().toString();
        String password = setting_second.getText().toString();
        if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            setting_first.setError("Enter a valid email address");
            return;
        }else if(password.isEmpty()){
            setting_second.setError("Enter your password");
            return;
        }
        else {
            final User user = new User();
            user.id = userid;
            user.email = newEmail;
            user.password = password;
            final ProgressDialog progressDialog = new ProgressDialog(EditSetting.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Email...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changeEmail(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    try{
                        if(response.body().status==0){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                        else if(response.body().status==1){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditSetting.this,Logout.class));
                            progressDialog.cancel();
                        }
                    }catch (Exception e){
                        Toast.makeText(EditSetting.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void change_password(){
        String oldPassword = setting_first.getText().toString();
        String newPassword = setting_second.getText().toString();
        String repeatPassword = setting_last.getText().toString();
        if(oldPassword.isEmpty()){
            setting_first.setError("Enter old password");
            return;
        }
        else if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10){
            setting_second.setError("Password between 4 and 10 alphanumeric characters");
            return;
        }
        else if(!newPassword.equals(repeatPassword)){
            setting_last.setError("Passwords not identical");
            return;
        }else {
            final User user = new User();
            user.id = userid;
            user.password = newPassword;
            user.oldpassword = oldPassword;
            final ProgressDialog progressDialog = new ProgressDialog(EditSetting.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Password...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changePassword(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    try{
                        if(response.body().status==0){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                        else if(response.body().status==1){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            startActivity(new Intent(EditSetting.this,Logout.class));
                        }
                    }catch (Exception e){
                        Toast.makeText(EditSetting.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private void change_phone(){
        String phone = setting_first.getText().toString();
        if(phone.isEmpty()||phone.length()!=11){
            setting_first.setError("Invalid phone number");
            return;
        }
        else{
            final User user = new User();
            user.id = userid;
            user.phone_number = phone;
            final ProgressDialog progressDialog = new ProgressDialog(EditSetting.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Phone number...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changePhone(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    try{
                        if(response.body().status==0){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                        else if(response.body().status==1){
                            Toast.makeText(EditSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }catch (Exception e){
                        Toast.makeText(EditSetting.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if(mChangeInfoCall!=null){
            mChangeInfoCall.cancel();
        }
    }
}
