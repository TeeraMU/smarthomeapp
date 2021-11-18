package com.example.smarthomedemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etCountryCode, etEmail, etPassword, etVerification;
    private Button btnGetOTP, btnSignUp;
    private static final String TAG = "SAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //title bar
        getSupportActionBar().setTitle("Register");

        //init Views
        //textviews
        etCountryCode = findViewById(R.id.etRegisCountryCode);
        etEmail = findViewById(R.id.etRegisEmail);
        etPassword = findViewById(R.id.etRegisPassword);
        etVerification = findViewById(R.id.etRegisVerification);
        //buttons
        btnGetOTP = findViewById(R.id.btnViewGetOTP);
        btnSignUp = findViewById(R.id.btnViewSignUp);

        etVerification.setVisibility(View.INVISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeredCountryCode = etCountryCode.getText().toString();
                String registeredEmail = etEmail.getText().toString();
                getValidate(registeredCountryCode, registeredEmail);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeredCountryCode = etCountryCode.getText().toString();
                String registeredEmail = etEmail.getText().toString();
                String registeredPassword = etPassword.getText().toString();
                String inputVerification = etVerification.getText().toString();

                TuyaHomeSdk.getUserInstance().registerAccountWithEmail(registeredCountryCode, registeredEmail, registeredPassword, inputVerification, registerCallback);
            }
        });


    }

    IRegisterCallback registerCallback = new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
            Log.d(TAG, "Registration Successful!");
            Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        }

        @Override
        public void onError(String code, String error) {
            //TODO: Error check when Registration failed
            Log.d(TAG, "Registration error code : " + error);
            Toast.makeText(RegistrationActivity.this, "Failed registration!", Toast.LENGTH_LONG).show();
        }
    };

    IResultCallback validateCallback = new IResultCallback() {
        @Override
        public void onError(String code, String error) {
            //TODO: Error check when verification failed
            Log.d(TAG, "Verification error code : " + error);
            Toast.makeText(RegistrationActivity.this, "Failed verification code send!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess() {
            Toast.makeText(RegistrationActivity.this, "Verification code sent!", Toast.LENGTH_LONG).show();
            etVerification.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);

        }
    };

    private void getValidate(String countryCode, String email){
        TuyaHomeSdk.getUserInstance().getRegisterEmailValidateCode(countryCode, email, validateCallback);
    }
}