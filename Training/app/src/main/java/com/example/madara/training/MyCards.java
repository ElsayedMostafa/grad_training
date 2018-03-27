package com.example.madara.training;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.madara.training.fragments.GetPassword;
import com.google.android.gms.vision.barcode.Barcode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCards extends AppCompatActivity {
    private final String TAG = "MyCards";
    @BindView(R.id.btn_bindcard)
    Button _btn_bind;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    private String mBarcode;
    private String mUserPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        // show user cards
        ButterKnife.bind(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        _btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCards.this, QRScanner.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
//                _qrresult.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        _qrresult.setText(barcode.displayValue);
//                    }
//                });
                Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                mBarcode = barcode.displayValue;
                GetPassword getPassword = new GetPassword();
                getPassword.show(getFragmentManager(),"GetPassword");

            }
        }
    }
    public void start(String password){
        if(!password.equals("")){
            Toast.makeText(this,"Empty Password!",Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(MyCards.this);
            mUserPassword = password;
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Binding...");
            progressDialog.show();
            Toast.makeText(this, mUserPassword + " " + mBarcode, Toast.LENGTH_LONG).show();
        }
    }


}
