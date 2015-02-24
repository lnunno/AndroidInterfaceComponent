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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Caveats:
 * <p/>
 * <ul>
 * <li>The items in the Adapter of the MotionListView <b>must</b> be Comparable.
 * Some runtime casts are made internally.</li>
 * </ul>
 * Created by Lucas on 2/3/2015.
 * <p/>
 * References:
 * <p/>
 * <ul>
 * <li>http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125</li>
 * </ul>
 */
public class MotionListView extends ListView implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Context context;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    /**
     * Threshold for "up" sort detection.
     */
    private static final int RESET_Y_THRESHOLD = 5;

    /**
     * Threshold to detect a forward tilt for scrolling.
     */
    private static final float POS_Z_TILT_THRESHOLD = 0.75f;

    /**
     * Threshold to detect an away tilt for scrolling.
     */
    private static final float NEG_Z_TILT_THRESHOLD = -POS_Z_TILT_THRESHOLD;

    /**
     * Force needed for a shake.
     */
    private static final int SHAKE_THRESHOLD = 60;

    /**
     * How often movement detection is refreshed.
     * Need a delay to detect bigger movements.
     */
    private static final int UPDATE_INTERVAL_MILLIS = 100;

    /**
     * How much scrolling is done on tilt.
     */
    private static final int SCROLL_SENSITIVITY = 3;

    public MotionListView(Context context) {
        super(context);
        this.context = context;
        this.initializeSensor();
    }

    public MotionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initializeSensor();
    }

    public MotionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.initializeSensor();
    }

    /*
    * For SensorEventListener.
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
    * For SensorEventListener.
     */
    public void onSensorChanged(SensorEvent event) {

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
            float y_delta = last_y - y;
            float z_delta = last_z - z;
            float tiltAmount = Math.abs(z_delta);

            Log.d("SPEED", "speed=" + speed);
            Log.d("Y_DELTA", "y_delta=" + y_delta);
            Log.d("Z_DELTA", "z_delta=" + z_delta);

            if (y_delta > RESET_Y_THRESHOLD) {
                this.doVerticalAction();
            } else if (speed > SHAKE_THRESHOLD) {
                // The phone was shook!
                Log.i("SHAKE", "Phone was shook!");
                this.doShakeAction();
            } else if (z_delta < NEG_Z_TILT_THRESHOLD) {
                // Tilt away.
                Log.d("TILT", "Tilted back");
                int scrollAmount = (int) (-tiltAmount * SCROLL_SENSITIVITY);
                this.smoothScrollByOffset(scrollAmount);
            } else if (z_delta > POS_Z_TILT_THRESHOLD) {
                // Tilt forward.
                Log.d("TILT", "Tilted forward");
                int scrollAmount = (int) (tiltAmount * SCROLL_SENSITIVITY);
                this.smoothScrollByOffset(scrollAmount);
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    /**
     * @param adapter The adapter to get the items from.
     * @return A list of all items in the adapter.
     */
    private List<Comparable> getAllAdapterItems(ArrayAdapter adapter) {
        List<Comparable> listItems = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Comparable item = (Comparable) adapter.getItem(i);
            listItems.add(item);
        }
        return listItems;
    }

    /**
     * This is called when a shake is registered. By default, it shuffles the items in the list.
     */
    private void doShakeAction() {
        ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
        List<Comparable> listItems = this.getAllAdapterItems(adapter);

        Collections.shuffle(listItems); // Shuffle the elements.

        // Add the list as the items in the ListView.
        adapter.clear();
        adapter.addAll(listItems);
    }

    /**
     * Perform an action when the phone is moved straight up.
     */
    private void doVerticalAction() {
        ArrayAdapter adapter = (ArrayAdapter) this.getAdapter();
        List<Comparable> listItems = this.getAllAdapterItems(adapter);

        Collections.sort(listItems);

        // Add the list as the items in the ListView.
        adapter.clear();
        adapter.addAll(listItems);
    }

    private void initializeSensor() {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
