package com.example.smarthomedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private CardView cvDevice;
    private Button btnSearch;
    private TextView tvDeviceName, tvDeviceId, tvProductId;
    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;
    private static String TAG = "SAA";

    //Wifi id & password
    private String ssid = "JuiceBoy_2.4G";
    private String password = "telecom#7";

    String homeName ="SmartHome";
    String[] rooms = {"Bedroom", "Kitchen"};
    ArrayList<String> roomList;

    ITuyaActivator tuyaActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        cvDevice.setClickable(false);
        cvDevice.setBackgroundColor(Color.LTGRAY);

        roomList = new ArrayList<>();
        roomList.addAll(Arrays.asList(rooms));

        createHome(homeName, roomList);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = btnSearch.getText().toString();

                if(tuyaActivator == null){
                    Toast.makeText(HomeActivity.this, "Wifi config in progress", Toast.LENGTH_LONG).show();
                }else{
                    if(currentText.equalsIgnoreCase("Search Devices")){
                        tuyaActivator.start();
                        btnSearch.setText("Stop Search");
                        Log.d(TAG, "Searching devices...");
                    }else{
                        btnSearch.setText("Search Devices");
                        Log.d(TAG, "Stop searching devices");
                        tuyaActivator.stop();
                    }
                }
                tuyaActivator.start();
            }
        });

        cvDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("DeviceId", currentDeviceBean.devId);
                bundle.putString("Devicename", currentDeviceBean.name);
                bundle.putString("ProductId", currentDeviceBean.productId);
                Intent intent = new Intent(HomeActivity.this, DeviceBulbController.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void createHome(String homeName, List<String> roomList){
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName, 0, 0, "", roomList, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                currentHomeBean = bean;
                //TODO: Delete when testing successful
                Toast.makeText(HomeActivity.this, "Create Successful!", Toast.LENGTH_LONG).show();
                getRegistrationToken();
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(HomeActivity.this, "Home creation failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO: Delete it after using UIBizBundle
    private void searchDevices(String token){
        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setPassword(password)
                .setContext(this)
                .setActivatorModel(ActivatorModelEnum.TY_EZ)
                .setTimeOut(1000)
                .setToken(token)
                .setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(HomeActivity.this, "Device detection failed!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {
                        Log.d(TAG,"Connect successful");
                        Toast.makeText(HomeActivity.this, "Device detection successful!!", Toast.LENGTH_LONG).show();
                        currentDeviceBean = devResp;
                        cvDevice.setClickable(true);
                        cvDevice.setBackgroundColor(Color.WHITE);
                        tvDeviceId.setText("Device ID: "+ currentDeviceBean.devId);
                        tvDeviceName.setText("Device Name : "+ currentDeviceBean.name);
                        tvProductId.setText("Product ID : "+ currentDeviceBean.productId);
                        btnSearch.setText("Search Devices");
                        tuyaActivator.stop();
                    }

                    @Override
                    public void onStep(String step, Object data) {
                        switch(step){
                            case ActivatorEZStepCode
                                    .DEVICE_BIND_SUCCESS:
                                Toast.makeText(HomeActivity.this, "Device bind successful!!", Toast.LENGTH_LONG).show();
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                Toast.makeText(HomeActivity.this, "New device find!!", Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                })
        );
    }


    private void getRegistrationToken(){
        long homeId = currentHomeBean.getHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                searchDevices(token);
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Log.d(TAG,"Failed send token!!");
            }
        });
    }

    private void initViews(){
        cvDevice = findViewById(R.id.cvDevice);
        btnSearch = findViewById(R.id.btnSearchDevice);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvDeviceId = findViewById(R.id.tvDeviceId);
    }
}