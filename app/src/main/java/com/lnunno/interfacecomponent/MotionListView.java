package com.lnunno.interfacecomponent;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by Lucas on 2/3/2015.
 */
public class MotionListView extends ListView implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Context context;

    public MotionListView(Context context){
        super(context);
        this.context = context;
        this.initializeSensor();
    }

    public MotionListView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        this.initializeSensor();
    }

    public MotionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        this.context = context;
        this.initializeSensor();
    }

    /*
    * For SensorEventListener.
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    /*
    * For SensorEventListener.
     */
    public void onSensorChanged(SensorEvent event){
        Log.i("Sensor", "Sensor event triggered.");
    }

    /*
    private methods.
     */

    private void initializeSensor(){
        this.sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
