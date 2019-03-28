package com.example.tiltapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer,sensorMagnetometer;

    private float[] accelerometerData = new float[3];
    private float[] magnetoometerData = new float[3];

    private TextView textSensorAzimuth;
    private TextView textSensorPitch;
    private TextView textSensorRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSensorAzimuth = (TextView)findViewById(R.id.label_azimuth);
        textSensorPitch = (TextView)findViewById(R.id.label_pitch);
        textSensorRoll = (TextView)findViewById(R.id.label_roll);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    protected void onStart() {

        super.onStart();
        if(sensorAccelerometer!=null){
            sensorManager.registerListener(this,
                    sensorAccelerometer,sensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorMagnetometer!=null){
            sensorManager.registerListener(this,
                    sensorMagnetometer,sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData=event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetoometerData=event.values.clone();
                break;
        }
        float[] rotatioMatrix = new float[9];
        boolean rotationOk=SensorManager.getRotationMatrix(rotatioMatrix,null,accelerometerData,magnetoometerData);
        float[] orientationValues = new float[3];
        if(rotationOk){
            SensorManager.getOrientation(rotatioMatrix,orientationValues);
        }

        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        textSensorAzimuth.setText("Azimuth (z) :"+String.format("%.2f",azimuth));
        textSensorPitch.setText("Pitch (z) :"+String.format("%.2f",pitch));
        textSensorRoll.setText("Roll (z) :"+String.format("%.2f",roll));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
