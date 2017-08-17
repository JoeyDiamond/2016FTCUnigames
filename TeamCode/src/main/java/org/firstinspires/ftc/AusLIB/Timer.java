package org.firstinspires.ftc.AusLIB;

/**
 * A class to manage time and keep track
 *
 * Created by sailo on 6/18/2016.
 */
public class Timer {

    long startTimeMillis = 0;
    long lastCheckedTimeMillis = 0;
    long lastElapsedTimeMillis = 0;

    public void start(){
        startTimeMillis = System.currentTimeMillis();
    }

    public long getElapsedTimeMillis(){
        lastElapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        lastCheckedTimeMillis = System.currentTimeMillis();
        return lastElapsedTimeMillis;
    }

    public long getLastElapsedTimeMillis(){
        return lastElapsedTimeMillis;
    }

    public void reset(){
        startTimeMillis = System.currentTimeMillis();
        lastCheckedTimeMillis = startTimeMillis;
        lastElapsedTimeMillis = 0;
    }

    public boolean hasTimeElapsed(long millis){
        return (millis >= System.currentTimeMillis() - startTimeMillis);
    }

    public void waitUntilTimeFine(long millis){
        waitUntilTime(millis, 5);
    }

    public void waitUntilTimeCoarse(long millis){
        waitUntilTime(millis, 100);
    }

    public void waitUntilTime(long millis){
        waitUntilTime(millis, 20);
    }

    public void waitUntilTime(long millis, long resolution){
        while(!hasTimeElapsed(millis)){
            SystemUtil.delay(resolution);
        }
    }

    public double getElapsedTimeSeconds(){
        return getElapsedTimeMillis() / 1000.0;
    }

    public void sleep(long mSec){
        SystemUtil.delay(mSec);
    }
}
