package org.firstinspires.ftc.teamcode.UniGames.subsystems;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.AusLIB.MathUtil;
import org.firstinspires.ftc.AusLIB.Subsystem;
import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.UniGames.Constants;
import org.firstinspires.ftc.teamcode.tduLIB.Loopable;

/**
 * Created by sailo on 9/20/2016.
 */
public class Lift extends Subsystem implements Loopable {
    private static Lift ourInstance = new Lift();

    public static Lift getInstance() {
        return ourInstance;
    }

    private Lift() {
        super("lift");
    }

    DcMotor motorLift;
    Servo servoTilt;
    Servo servoPortDoor;
    Servo servoStbdDoor;
    String TAG = "lift";
    Intake intake = Intake.getInstance();

    int targetPosition = 0;
    int privTargetPosition = 0;
    double tiltPosition = 0.5;
    double portDoorPosition = 0.5;
    double stbdDoorPosition = 0.5;

    int currentPosition = 0;
    double kP = -2;

    double tolerance = 20;
    int toleranceCounter = 0;
    int toleranceCount = 10;

    public double rawThrottleLift = 0.0;

    @Override
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        kP = -0.001;

        motorLift = hardwareMap.dcMotor.get("motorLift");
        servoTilt = hardwareMap.servo.get("servoTilt");
        servoPortDoor = hardwareMap.servo.get("servoPortDoor");
        servoStbdDoor = hardwareMap.servo.get("servoStbdDoor");

        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(motorLift.getCurrentPosition() != 0){
            SystemUtil.fastLoopDelay();
        }
        motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        currentPosition = -motorLift.getCurrentPosition();
        targetPosition = currentPosition;
        privTargetPosition = currentPosition;

        portDoorPosition = Constants.PORT_DOOR_INTAKE;
        stbdDoorPosition = Constants.STBD_DOOR_INTAKE;
        tiltPosition = Constants.BUCKET_TILT_CENTER;
    }

    @Override
    public void update() {
        // calculate arm motor throttleLift
        currentPosition = -motorLift.getCurrentPosition();
        if(intake.isLiftSafeToMove()){
            privTargetPosition = (int)MathUtil.limitValue(targetPosition,Constants.LIFT_MAX, Constants.LIFT_MIN);
        }
        int error = privTargetPosition - currentPosition;
        double throttleLift = error * kP;
        if(Math.abs(error) < 0)
            throttleLift = 0;

        // keep running talley if in position
        if(Math.abs(error) < tolerance){
            toleranceCounter++;
        } else
            toleranceCounter = 0;

        throttleLift = MathUtil.limitValue(throttleLift,0.8,-0.8);

        // log
        Log.d(TAG, "update: errorVal: " + error + " throttleLift: " + rawThrottleLift + " " +
                "inPosition: " + isInPosition());
        Log.d(TAG, "update: current: " + currentPosition + " privTarget: " + privTargetPosition);
        telemetry.addData("liftInPos", isInPosition());
        telemetry.addData("target", targetPosition);
        telemetry.addData("current", currentPosition);
        telemetry.addData("throttleLift", throttleLift);
        //telemetry.addData("rawthrottleLift", rawThrottleLift);
        Log.d(TAG, "update: tilt: " + tiltPosition + " port door: " + portDoorPosition + " stbd " +
                "door: " + stbdDoorPosition);

        if(!enabled){
            motorLift.setPower(0.0);

        } else {
            motorLift.setPower(throttleLift);
            servoTilt.setPosition(tiltPosition);
            servoPortDoor.setPosition(portDoorPosition);
            servoStbdDoor.setPosition(stbdDoorPosition);
        }
    }

    public void store(){
        setTargetPosition(Constants.LIFT_STORE_POSITION);
        setBucketTiltPosition(Constants.BUCKET_TILT_CENTER);
        setPortDoorPosition(Constants.PORT_DOOR_CLOSED);
        setStbdDoorPosition(Constants.STBD_DOOR_CLOSED);
    }

    public void setTargetPosition(int position){
        targetPosition = position;
    }

    public boolean isInPosition(){
        return toleranceCounter >= toleranceCount;
    }


    public void enable(){
        enabled = true;
        targetPosition = currentPosition;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public void setBucketTiltPosition(double pos){
        tiltPosition = pos;
    }

    public void setPortDoorPosition(double pos){
        portDoorPosition = pos;
    }

    public void setStbdDoorPosition(double pos){
        stbdDoorPosition = pos;
    }

    public double getTiltPosition(){
        return tiltPosition;
    }

    public double getPortDoorPosition(){
        return portDoorPosition;
    }

    public double getStbdDoorPosition(){
        return stbdDoorPosition;
    }
}
