package com.example.smarthomedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class MainActivity extends AppCompatActivity {

    private TextView txtRegis;
    private EditText etCountryCode, etEmail, etPassword;
    private Button btnLogin;
    private static final String TAG = "SAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide titlebar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //init Views
        txtRegis = findViewById(R.id.txtAccRegis);
        etCountryCode = findViewById(R.id.etViewCountryCode);
        etEmail = findViewById(R.id.etViewEmail);
        etPassword = findViewById(R.id.etViewPassword);
        btnLogin = findViewById(R.id.btnViewLogin);


        txtRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO: Delete toast after test */
                Toast.makeText(getApplicationContext(), "Register Checked!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = etCountryCode.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                TuyaHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, loginCallback);
            }
        });

    }

    ILoginCallback loginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            Log.d(TAG, "Login Successful!");
            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        @Override
        public void onError(String code, String error) {
            //TODO: Error check when login failed
            Log.d(TAG, "Login error code : " + error);
            Toast.makeText(MainActivity.this, "Failed Login!", Toast.LENGTH_LONG).show();
        }
    };

}