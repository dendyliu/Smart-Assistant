package org.kdadev.smartassistant;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;

    private TextView stepCount;

    private TextView stepTime;

    private Button toggle;

    private boolean counting = false;

    //Sensor Manager
    private SensorManager sensorManager;
    private float accel;

    //Steps Counter
    private float prevY;
    private float currY;
    private int numSteps;
    private int threshold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        stepCount = (TextView) findViewById(R.id.stepcount);
        stepTime = (TextView) findViewById(R.id.steptime);
        toggle = (Button) findViewById(R.id.stepbutton);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countStep();
            }
        });
        prevY = 0;
        currY = 0;
        numSteps = 0;

        accel = 0.00f;
        threshold = 8;

        enableAccelListen();
    }

    private void enableAccelListen(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener =
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    Log.v("X Y Z", String.valueOf(x)+" "+String.valueOf(y)+" "+String.valueOf(z));
                    currY = y;

                    if(Math.abs(currY - prevY) > threshold && counting==true){
                        numSteps++;
                        stepCount.setText(String.valueOf(numSteps)+" Steps");
                    }
                    prevY = y;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };

    private void countStep(){
        if(counting==false){
            Format formatter = new SimpleDateFormat("HH:mm");
            String now = formatter.format(new Date());
            stepTime.setText("Since "+ now);
            toggle.setText("STOP");
            counting=true;
            toggle.setBackgroundColor(Color.RED);
        }
        else{
            toggle.setText("START");
            stepCount.setText("0 Step");
            numSteps=0;
            Format formatter = new SimpleDateFormat("HH:mm");
            String now = formatter.format(new Date());
            stepTime.setText(stepTime.getText()+" until "+now);
            toggle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
