package org.firstinspires.ftc.teamcode.UniGames.subsystems;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.AusLIB.MathUtil;
import org.firstinspires.ftc.AusLIB.Subsystem;
import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.UniGames.Constants;
import org.firstinspires.ftc.teamcode.tduLIB.Loopable;

/**
 * Created by sailo on 9/20/2016.
 */
public class Intake extends Subsystem implements Loopable{
    private static Intake ourInstance = new Intake();

    public static Intake getInstance() {
        return ourInstance;
    }

    private Intake() {
        super("intake");
    }

    DcMotor motorArm;
    DcMotor motorSpinner;
    TouchSensor touch;
    String TAG = "intake";

    double throttleSpinner = 0.0;

    int targetPosition = 0;
    int currentPosition = 0;
    double kP = 0.0001;

    double tolerance = 50;
    int toleranceCounter = 0;
    int toleranceCount = 10;
    long currentTime = 0;
    long prevTime = 0;
    long loopTime = 0;

    boolean firstPress = true;

    @Override
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;

        kP = 0.005;

        motorArm = hardwareMap.dcMotor.get("motorArm");
        motorSpinner = hardwareMap.dcMotor.get("motorSpinner");
        touch = hardwareMap.touchSensor.get("switchArm");

        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorSpinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        currentPosition = motorArm.getCurrentPosition();
        targetPosition = currentPosition;
        throttleSpinner = 0;

        firstPress = true;
    }

    @Override
    public void update() {
        //telemetry.addData("intake status", "looping");

        // limit target position
        int privTargetPosition = (int) MathUtil.limitValue(targetPosition,Constants.ARM_MAX, Constants.ARM_MIN);

        // calculate arm motor power
        currentPosition = motorArm.getCurrentPosition();
        int error = privTargetPosition - currentPosition;
        double throttleArm = error * kP;
        if(Math.abs(error) < 0)
            throttleArm = 0;

        // keep running talley if in position
        if(Math.abs(error) < tolerance){
            toleranceCounter++;
        } else {
            toleranceCounter = 0;
        }

        if(touch.isPressed()){
            if(targetPosition == Constants.ARM_DOWN_POSITION)
                throttleArm = 0.0;
            if(firstPress){
                zeroArm();
                firstPress = false;
            }
        } else if(targetPosition == Constants.ARM_DOWN_POSITION && !touch.isPressed()){
            throttleArm = -0.15;
            firstPress = true;
        } else if(targetPosition != Constants.ARM_DOWN_POSITION || !touch.isPressed()){
            firstPress = true;
        }

        // set speed limits
        throttleArm = MathUtil.limitValue(throttleArm,0.2,-0.2);

        // handle disabled state
        if(!enabled){
            motorArm.setPower(0.0);
            motorSpinner.setPower(0.0);
        } else {
            motorArm.setPower(throttleArm);
            motorSpinner.setPower(throttleSpinner);
        }

        prevTime = currentTime;
        currentTime = System.currentTimeMillis();
        loopTime = currentTime - prevTime;

        //telemetry.addData("touch", touch.isPressed());
        //telemetry.addData("intake target" , privTargetPosition);
        //telemetry.addData("intake current" , currentPosition);
        //telemetry.addData("arm error", error);
        //telemetry.addData("kP", kP);
        //telemetry.addData("throttleArm", throttleArm);
        //telemetry.addData("spinner", throttleSpinner);
        //telemetry.addData("loop time", loopTime);
        //telemetry.addData("armInPosition", isInPosition());
        // log
        Log.d(TAG, "update: target: " + targetPosition + " current: " + currentPosition);
        Log.d(TAG, "update: errorVal: " + error + " throttleArm: " + throttleArm + " inPosition: " + isInPosition());
        Log.d(TAG, "update: spinner: " + throttleSpinner + " looptime: " + loopTime);

    }


    public void zeroArm(){
        motorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(motorArm.getCurrentPosition() != 0){
            SystemUtil.fastLoopDelay();
        }
        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setTargetPosition(int position){
        targetPosition = position;
    }

    public boolean isInPosition(){
        return toleranceCounter >= toleranceCount;
    }

    public void setSpinnerIntake(){
        throttleSpinner = Constants.SPINNER_INTAKE_SPEED;
        Log.d(TAG, "setSpinnerIntake: run");
    }

    public void setSpinnerEject(){
        throttleSpinner = Constants.SPINNER_EJECT_SPEED;
        Log.d(TAG, "setSpinnerEject: run");
    }

    public void stopSpinner(){
        throttleSpinner = Constants.SPINNER_STOPPED_SPEED;
        Log.d(TAG, "stopSpinner: run");
    }

    public void setThrottleSpinner(double target){
        target = MathUtil.limitValue(target);
        throttleSpinner = target;
        Log.d(TAG, "setThrottleSpinner: " + target);
    }

    public void intake(){
        setTargetPosition(Constants.ARM_DOWN_POSITION);
        setSpinnerIntake();
    }

    public void enable(){
        enabled = true;
        targetPosition = currentPosition;
    }

    public int getTargetPosition(){
        return targetPosition;
    }

    public boolean isLiftSafeToMove(){
        return (currentPosition <= Constants.ARM_SAFE_POSITION) &&
                (targetPosition <= Constants.ARM_SAFE_POSITION);
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

}
