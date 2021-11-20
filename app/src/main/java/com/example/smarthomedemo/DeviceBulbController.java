package com.example.smarthomedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;

public class DeviceBulbController extends AppCompatActivity {

    private TextView tvBulbName;
    private SeekBar sbBrightness;
    private Button btnPower;

    private String Status = "0";

    String devId, devName, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bulb_controller);

        Bundle bundle = getIntent().getExtras();

        tvBulbName = findViewById(R.id.tvDeviceName);
        sbBrightness = findViewById(R.id.sbBrightness);
        btnPower = findViewById(R.id.btnPower);

        if(bundle != null){
            devId = bundle.getString("DeviceId");
            devName = bundle.getString("DeviceName");
            productId = bundle.getString("ProductId");
            tvBulbName.setText(devName);
        }

        ITuyaLightDevice controlDevice = new TuyaLightDevice(devId);
        controlDevice.registerLightListener(new ILightListener() {
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
        });

    btnPower.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Status == "0") {
                controlDevice.powerSwitch(true, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DeviceBulbController.this, "Turn on the light failed!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceBulbController.this, "Turn on the light!", Toast.LENGTH_LONG).show();
                        Status = "1";
                    }
                });
            }else if(Status == "1") {
                controlDevice.powerSwitch(false, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DeviceBulbController.this, "Turn off the light failed!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceBulbController.this, "Turn off the light!", Toast.LENGTH_LONG).show();
                        Status = "0";
                    }
                });
            }
        }
    });

    sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            controlDevice.brightness(progress, new IResultCallback() {
                @Override
                public void onError(String code, String error) {
                    Toast.makeText(DeviceBulbController.this, "Brightness change failed!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess() {
                    //TODO: delete toast after brightness its worked!
                    Toast.makeText(DeviceBulbController.this, "Brightness changed!", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });

    }
}