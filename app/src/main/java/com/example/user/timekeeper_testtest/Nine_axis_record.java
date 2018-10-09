package com.example.user.timekeeper_testtest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Nine_axis_record implements SensorEventListener {
    public SensorManager sensorManager;
    public Sensor magneticSensor;
    public Sensor accelerometerSensor;
    public Sensor gyroscopeSensor;
    // 將納秒轉化為秒
    public static final float NS2S = 1.0f / 1000000000.0f;
    public static boolean state;
    public static float x;
    public static float y;
    public static float z;

    public void start_record(SensorManager sensorManager, Boolean state) {
        this.state = state;
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(this.state == false){
            Log.d("九軸","關閉九軸感應");
            sensorManager.unregisterListener(this);
        }
        if(accelerometerSensor == null){
            Log.d("九軸","註冊失敗");
        }else{
            Log.d("九軸","註冊成功");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && state == true) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //    TODO Auto-generated method stub
    }
}
