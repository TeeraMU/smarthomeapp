package com.example.smarthomedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.bluetooth.BluetoothClass;
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
    private EditText etWifiSSD, etWifiPassword;
    private static String TAG = "SAA";

    //Wifi id & password
//    private String ssid = "JuiceBoy_2.4G";
//    private String password = "telecom#7";

    String homeName ="SmartHome";
    String[] rooms = {"Bedroom", "Kitchen", "Study"};
    ArrayList<String> roomList;

//    ITuyaActivator tuyaActivator;

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
                String strSsid = etWifiSSD.getText().toString();
                String strPassword = etWifiPassword.getText().toString();
                String currentText = btnSearch.getText().toString();
                long homeId = currentHomeBean.getHomeId();

                //TODO: delete this after testing
                Log.d(TAG,"Home id checked : "+homeId);


                if(currentText.equalsIgnoreCase("Search Devices")){
                    btnSearch.setText("Stop Search");
                    TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
                        @Override
                        public void onSuccess(String token) {
                            // Start network configuration -- EZ mode
                            ActivatorBuilder builder= new ActivatorBuilder()
                                    .setSsid(strSsid)
                                    .setContext(v.getContext())
                                    .setPassword(strPassword)
                                    .setActivatorModel(ActivatorModelEnum.TY_EZ)
                                    .setTimeOut(100)
                                    .setToken(token)
                                    .setListener(new ITuyaSmartActivatorListener() {
                                        @Override
                                        public void onError(String errorCode, String errorMsg) {
                                            Log.d(TAG,"failed error : "+errorCode+" message : "+errorMsg);
                                            Toast.makeText(HomeActivity.this, "Device detection failed!", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onActiveSuccess(DeviceBean devResp) {
                                            Log.d(TAG,"Connect successful");
                                            Toast.makeText(HomeActivity.this, "Device detection successful!!", Toast.LENGTH_LONG).show();
                                            currentDeviceBean = devResp;
                                            cvDevice.setClickable(true);
                                            cvDevice.setBackgroundColor(Color.BLUE);
//                                            setBroadcasts(devResp);
                                            //TODO: delete log after test
                                            Log.d(TAG,"Device bean ID : "+devResp);
                                            tvDeviceId.setText("Device ID: "+ currentDeviceBean.devId);
                                            tvDeviceName.setText("Device Name : "+ currentDeviceBean.name);
                                            tvProductId.setText("Product ID : "+ currentDeviceBean.productId);

                                            Bundle bundle = new Bundle();
                                            bundle.putString("devID", currentDeviceBean.devId);
                                            bundle.putString("Devicename", currentDeviceBean.name);
                                            bundle.putString("ProductId", currentDeviceBean.productId);
                                            Intent intent = new Intent(HomeActivity.this, DeviceBulbController.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            btnSearch.setText("Search Devices");
                                        }

                                        @Override
                                        public void onStep(String step, Object data) {
                                            Log.i(TAG, step + " --> " + data);
                                        }
                                    });

                            ITuyaActivator tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(builder);
                            //Start configuration
                            tuyaActivator.start();

                        }

                        @Override
                        public void onFailure(String errorCode, String errorMsg) {

                        }
                    });

                }else{
                    btnSearch.setText("Search Devices");

                }


//                if(tuyaActivator == null){
//                    Toast.makeText(HomeActivity.this, "Wifi config in progress", Toast.LENGTH_LONG).show();
//                }else{
//                    if(currentText.equalsIgnoreCase("Search Devices")){
//                        tuyaActivator.start();
//                        btnSearch.setText("Stop Search");
//                        Log.d(TAG, "Searching devices...");
//                    }else{
//                        btnSearch.setText("Search Devices");
//                        Log.d(TAG, "Stop searching devices");
//                        tuyaActivator.stop();
//                    }
//                }
//                tuyaActivator.start();
            }
        });

        cvDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, DeviceBulbController.class));

            }
        });


    }

    private void createHome(String homeName, List<String> roomList){
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName, 0, 0, "", roomList, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                currentHomeBean = bean;
                //TODO: delete it after testing
                Toast.makeText(HomeActivity.this, "Create Successful!", Toast.LENGTH_LONG).show();
                Log.d(TAG,"Home & Room list create!");
                Log.i(TAG, "home bean check : "+currentHomeBean);
//                getRegistrationToken();
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(HomeActivity.this, "Home creation failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void setBroadcasts(DeviceBean devResp) {
//        String devID = devResp.getDevId();
//        Intent intent = new Intent(HomeActivity.this, DeviceBulbController.class);
//        intent.putExtra("devID", devID);
//        sendBroadcast(intent);
//        //TODO: delete log when all worked!
//        Log.d(TAG, "Broadcast Sent!! devID : "+devID);
//                        Bundle bundle = new Bundle();
//                bundle.putString("DeviceId", currentDeviceBean.devId);
//                bundle.putString("Devicename", currentDeviceBean.name);
//                bundle.putString("ProductId", currentDeviceBean.productId);
//                Intent intent = new Intent(HomeActivity.this, DeviceBulbController.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//    }

    //TODO: Delete it after using UIBizBundle
//    private void searchDevices(String token){
//
//        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
//                .setSsid(strSsid)
//                .setContext(this)
//                .setPassword(strPassword)
//                .setActivatorModel(ActivatorModelEnum.TY_EZ)
//                .setTimeOut(100)
//                .setToken(token)
//                .setListener(new ITuyaSmartActivatorListener() {
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        Log.d(TAG,"failed error : "+errorCode+" message : "+errorMsg);
//                        Toast.makeText(HomeActivity.this, "Device detection failed!", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onActiveSuccess(DeviceBean devResp) {
//                        Log.d(TAG,"Connect successful");
//                        Toast.makeText(HomeActivity.this, "Device detection successful!!", Toast.LENGTH_LONG).show();
//                        currentDeviceBean = devResp;
//                        cvDevice.setClickable(true);
//                        cvDevice.setBackgroundColor(Color.WHITE);
//                        tvDeviceId.setText("Device ID: "+ currentDeviceBean.devId);
//                        tvDeviceName.setText("Device Name : "+ currentDeviceBean.name);
//                        tvProductId.setText("Product ID : "+ currentDeviceBean.productId);
//                        btnSearch.setText("Search Devices");
//                        tuyaActivator.stop();
//                    }
//
//                    @Override
//                    public void onStep(String step, Object data) {
//                        Log.d(TAG, step + " --> " + data);
//                    }
//                })
//        );
//    }


//    private void getRegistrationToken(){
//        long homeId = currentHomeBean.getHomeId();
//        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
//            @Override
//            public void onSuccess(String token) {
////                searchDevices(token);
//                Log.d(TAG,"Token Sent!");
//            }
//
//            @Override
//            public void onFailure(String errorCode, String errorMsg) {
//                Log.d(TAG,"Failed send token!!");
//            }
//        });
//    }

    private void initViews(){
        cvDevice = findViewById(R.id.cvDevice);
        btnSearch = findViewById(R.id.btnSearchDevice);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvDeviceId = findViewById(R.id.tvDeviceId);
        tvProductId = findViewById(R.id.tvProductId);
        etWifiSSD = findViewById(R.id.etWifiId);
        etWifiPassword = findViewById(R.id.etWifiPass);
    }
}