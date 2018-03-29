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

import com.example.madara.training.models.LoginResponse;
import com.example.madara.training.models.User;
import com.example.madara.training.utils.Session;
import com.example.madara.training.webservices.WebService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private Call<LoginResponse> mLoginCall;
    @BindView(R.id.et_login_email)
    EditText login_email;
    @BindView(R.id.et_login_pass)
    EditText login_pass;
    @BindView(R.id.btn_login)
    Button _loginButton;
//    @BindView(R.id.btn_login)
//    Button login;
//    @BindView(R.id.tv_dont_have_account)
//    TextView signup;

    //    @BindView(R.id.btn_login)
//    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            String email = getIntent().getStringExtra("email");
            String pass = getIntent().getStringExtra("pass");
            login_email.setText(email);
            login_pass.setText(pass);
        }

    }

    @OnClick({R.id.tv_dont_have_account, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dont_have_account:
                Intent gotToReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotToReg);
                break;
            case R.id.btn_login:
                final User user = new User();
                if (!validate()) {
                    onLoginFailed();
                    return;
                }
                _loginButton.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                user.email = login_email.getText().toString();
                user.password = login_pass.getText().toString();

                mLoginCall = WebService.getInstance().getApi().loginUser(user);
                        mLoginCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        // check for status value comming from server (response of login-user.php file status)
                        try{
                        if (response.body().status == 0) {
                            Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            _loginButton.setEnabled(true);
                            progressDialog.cancel();
                        } else if (response.body().status == 1) {
                            Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            user.username = response.body().user.user_name;
                            user.id = response.body().user.id;
                            user.email = response.body().user.user_email;
                            Session.getInstance().startSession(user);
                            Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(goToMain);
                            progressDialog.cancel();
                            finish();


                        } else {
                            progressDialog.cancel();
                            _loginButton.setEnabled(true);
                            Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        }

                     }
                     catch (Exception e){
                            Toast.makeText(LoginActivity.this, "Failed" + e.toString() , Toast.LENGTH_LONG).show();
                            Log.e(TAG,e.toString());
                            _loginButton.setEnabled(true);
                            progressDialog.cancel();
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e(TAG, t.getLocalizedMessage());
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);

                    }
                });
                break;


        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = login_email.getText().toString();
        String password = login_pass.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email.setError("Enter a valid email address");
            valid = false;
        } else {
            login_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            login_pass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            login_pass.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginCall.cancel();
    }
}

