package com.example.smarthomedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

public class DeviceBulbController extends AppCompatActivity {

    private EditText etDeviceName;
    private SeekBar sbBrightness;
    private Switch powerSwitch;
    private ITuyaLightDevice tuyaLightDevice;
    private Context mContext;

    private static String TAG = "SAA";

    private String Status = "0";

//    String devId, devName, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bulb_controller);
        mContext = this;

        etDeviceName = findViewById(R.id.etDeviceName);
        sbBrightness = findViewById(R.id.sbBrightness);
        powerSwitch = findViewById(R.id.swPower);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String devID = bundle.getString("devID");
        Log.d(TAG,"Check received devID : "+devID);




//        if(bundle != null){
////            devId = bundle.getString("DeviceId");
////            devName = bundle.getString("DeviceName");
////            productId = bundle.getString("ProductId");
//            tvBulbName.setText(devName);
//        }
        String text = etDeviceName.getText().toString();
        if(!TextUtils.isEmpty(text)){
            tuyaLightDevice = new TuyaLightDevice(text);
            tuyaLightDevice.registerLightListener(new LightListener());
        }
//      String devID = TuyaDeviceManager.getInstance().getDeviceID();
        if(!TextUtils.isEmpty(devID)) {
            etDeviceName.setText(devID);
            tuyaLightDevice = new TuyaLightDevice(devID);
            tuyaLightDevice.registerLightListener(new LightListener());
        }

        initBrightness();
        initSwitch();


//        ITuyaLightDevice controlDevice = new TuyaLightDevice(devId);
//        controlDevice.registerLightListener(new ILightListener() {
//            @Override
//            public void onDpUpdate(LightDataPoint lightDataPoint) {
//
//            }
//
//            @Override
//            public void onRemoved() {
//
//            }
//
//            @Override
//            public void onStatusChanged(boolean b) {
//
//            }
//
//            @Override
//            public void onNetworkStatusChanged(boolean b) {
//
//            }
//
//            @Override
//            public void onDevInfoUpdate() {
//
//            }
//        });

//    btnPower.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//                controlDevice.powerSwitch(false, new IResultCallback() {
//                    @Override
//                    public void onError(String code, String error) {
//                        Toast.makeText(DeviceBulbController.this, "Turn on the light failed!", Toast.LENGTH_LONG).show();
//                        //TODO: delete it after testing
//                        Log.d(TAG,"code : "+code+" message : "+error);
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(DeviceBulbController.this, "Turn on the light!", Toast.LENGTH_LONG).show();
//                        //TODO: delete it after testing
//                        Log.d(TAG,"Power off!!");
//                    }
//                });
//            }
//    });

//    sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            controlDevice.brightness(progress, new IResultCallback() {
//                @Override
//                public void onError(String code, String error) {
//                    Toast.makeText(DeviceBulbController.this, "Brightness change failed!", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onSuccess() {
//                    //TODO: delete toast after brightness its worked!
//                    Toast.makeText(DeviceBulbController.this, "Brightness changed!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//
//        }
//    });
    }
    private void initBrightness() {
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(tuyaLightDevice != null) {
                    tuyaLightDevice.brightness(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            //TODO: delete after testing success
                            Log.d(TAG, "Seek bar brightness failed!");
                        }

                        @Override
                        public void onSuccess() {
                            //TODO: delete after testing success
                            Log.d(TAG, "Seek bar brightness worked!");
                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void initSwitch() {
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(tuyaLightDevice != null) {
                    tuyaLightDevice.powerSwitch(isChecked, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            //TODO: delete after testing success
                            Log.d(TAG, "Power switch failed!");
                        }

                        @Override
                        public void onSuccess() {
                            //TODO: delete after testing success
                            Log.d(TAG, "Power switch worked!");

                        }
                    });
                }
            }
        });
    }

    public class LightListener implements ILightListener {

        @Override
        public void onDpUpdate(LightDataPoint lightDataPoint) {

        }

        @Override
        public void onRemoved() {

        }

        @Override
        public void onStatusChanged(boolean b) {

        }

        @Override
        public void onNetworkStatusChanged(boolean b) {

        }

        @Override
        public void onDevInfoUpdate() {

        }
    }


}