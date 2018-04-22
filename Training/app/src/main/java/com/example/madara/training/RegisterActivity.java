package com.example.madara.training;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madara.training.models.MainResponse;
import com.example.madara.training.models.User;
import com.example.madara.training.webservices.WebService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    private Call<MainResponse> mRegisterCall;
    @BindView(R.id.et_username)
    EditText username;
    @BindView(R.id.et_email)
    EditText _email;
    @BindView(R.id.et_password)
    EditText _password;
    @BindView(R.id.et_re_password)
    EditText re_password;
    @BindView(R.id.btn_register)
    Button _signupButton;
    @BindView(R.id.et_phone)
    EditText _et_phone;

    //    @BindView(R.id.btn_register)
//    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_register, R.id.link_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                final User user = new User();
                if (!validate()) {
                    onSignupFailed();
                    return;
                }
                _signupButton.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                user.username = username.getText().toString();
                user.email = _email.getText().toString();
                user.password = _password.getText().toString();
                user.phone_number = _et_phone.getText().toString();
                mRegisterCall = WebService.getInstance().getApi().registerUser(user);
                mRegisterCall.enqueue(new Callback<MainResponse>() {
                    @Override
                    public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                        try{
                        if (response.body().status == 2) {
                            Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            _signupButton.setEnabled(true);
                            progressDialog.cancel();
                        } else if (response.body().status == 1) {
                            Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            // go to login activity
                            Intent gotToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                            gotToLogin.putExtra("email", user.email);
                            gotToLogin.putExtra("pass", user.password);
                            startActivity(gotToLogin);
                            progressDialog.cancel();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            _signupButton.setEnabled(true);
                        }

                    } catch (Exception e){
                            Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            _signupButton.setEnabled(true);
                            progressDialog.cancel();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainResponse> call, Throwable t) {
                        Log.e(TAG, t.getLocalizedMessage());
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                        _signupButton.setEnabled(true);

                    }
                });
                break;
            case R.id.link_login:
                Intent gotToLog = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(gotToLog);
                break;


        }

    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = username.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        String repeat_password = re_password.getText().toString();
        String phoneNumber = _et_phone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            username.setError("Username should be at least 3 characters");
            valid = false;
        } else {
            username.setError(null);
        }
        if(phoneNumber.isEmpty()||phoneNumber.length()!=11){
            _et_phone.setError("Invalid phone number");
            valid = false;
        }
        else{
            _et_phone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("Enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _password.setError("Password between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }
        if (!password.equals(repeat_password)) {
            re_password.setError("Passwords not identical");
            valid = false;
        } else {
            re_password.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mRegisterCall.cancel();
    }
}
