package com.lnunno.interfacecomponent;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lucas on 2/3/2015.
 *
 * References:
 *
 * <ul>
 *     <li>http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125</li>
 * </ul>
 */
public class MotionListView extends ListView implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Context context;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 60;
    private static final int UPDATE_INTERVAL_MILLIS = 100;

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

        Sensor sensor = event.sensor; // Get the sensor associated with this event.

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            registerAccelerometerEvent(event);
        }
    }

    /*
    private methods.
     */

    private void registerAccelerometerEvent(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTimeMillis = System.currentTimeMillis();
        long timeDelta = curTimeMillis - lastUpdate;

        if (timeDelta > UPDATE_INTERVAL_MILLIS) {
            // Do an update.
            lastUpdate = curTimeMillis;

            float euclidDistance = FloatMath.sqrt(FloatMath.pow(x - last_x, 2) + FloatMath.pow(y - last_y, 2) + FloatMath.pow(z - last_z, 2));
            float speed = euclidDistance / timeDelta * 1000;

            Log.d("SPEED", "speed=" + speed);

            if (speed > SHAKE_THRESHOLD) {
                // The phone was shook!
                Log.i("SHAKE", "Phone was shook!");
                this.doShakeAction();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        } else {
            return;
        }
    }

    /**
     * This is called when a shake is registered. By default, it shuffles the items in the list.
     */
    private void doShakeAction() {
        List<Object> listItems = new ArrayList<>();
        ArrayAdapter adapter = (ArrayAdapter) this.getAdapter(); // Allows us to do actions on the list.
        for (int i = 0; i < adapter.getCount(); i++) {
            Object item = adapter.getItem(i);
            listItems.add(item);
        }
        Collections.shuffle(listItems); // Shuffle the elements.

        // Add the list as the items in the ListView.
        adapter.clear();
        adapter.addAll(listItems);
    }

    private void initializeSensor(){
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
