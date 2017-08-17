package com.qualcomm.ftcrobotcontroller.AusLIB;

import android.hardware.SensorManager;

/**
 * Created by sailo on 5/18/2016.
 */
public class AndroidOrientation {

    SensorManager mSensorManager;
    float[] rates = new float[3];
    float[] orientation = new float[3];

    public AndroidOrientation(){
        //mSensorManager = (SensorManager) Context.getSystemService(Context.SENSOR_SERVICE);
    }

    public float[] getAll(){
        update();
        return orientation;
    }

    protected void update(){
        mSensorManager.getOrientation(rates,orientation);
    }

    public float getRoll(){
        update();
        return orientation[2];
    }

    public float getPitch(){
        update();
        return orientation[1];
    }

    public float getYaw(){
        update();
        return orientation[0];
    }
}
