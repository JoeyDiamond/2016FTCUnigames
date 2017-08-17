package org.firstinspires.ftc.teamcode.UniGames.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.AusLIB.GamepadEnhanced;
import org.firstinspires.ftc.AusLIB.RobotDrive;
import org.firstinspires.ftc.AusLIB.Subsystem;
import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.UniGames.Constants;
import org.firstinspires.ftc.teamcode.UniGames.GlobalSingleton;

/**
 * Created by sailo on 9/20/2016.
 */
public class Drivebase extends Subsystem{
    private static Drivebase ourInstance = new Drivebase();

    public static Drivebase getInstance() {
        return ourInstance;
    }

    GlobalSingleton globalSingleton = GlobalSingleton.getInstance();

    DcMotor motorLeft;
    DcMotor motorRight;
    Servo portHook;
    Servo stbdHook;


    RobotDrive robotDrive;

    double portHookPosition = Constants.PORT_HOOK_RETRACTED;
    double stbdHookPosition = Constants.STBD_HOOK_RETRACTED;
    boolean hookDeployed = false;

    private Drivebase() {
        super("drivebase");
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
        portHook = hardwareMap.servo.get("servoHookPort");
        stbdHook = hardwareMap.servo.get("servoHookStbd");

        robotDrive = new RobotDrive(motorLeft,motorRight);
        robotDrive.turnOnEncoders();
        retractHook();
        enable();

        robotDrive.configTopHatControl(0.5,0.5,0.75);
    }

    public void drive(GamepadEnhanced gpad){
        if(!enabled){
            return;
        }
        if(!globalSingleton.autodrive)
            robotDrive.tankDriveStandard(gpad);

    }

    public void deployHook(){
        if(!enabled){
            return;
        }
        hookDeployed = true;
        portHook.setPosition(Constants.PORT_HOOK_EXTENDED);
        stbdHook.setPosition(Constants.STBD_HOOK_EXTENDED);

    }

    public void retractHook(){
        if(!enabled){
            return;
        }
        hookDeployed = false;
        portHook.setPosition(Constants.PORT_HOOK_RETRACTED);
        stbdHook.setPosition(Constants.STBD_HOOK_RETRACTED);

    }

    public void unhookUnderLoad(){
        if(!enabled){
            return;
        }
        globalSingleton.autodrive = true;
        robotDrive.setLeftAndRight(-1);
        retractHook();
        SystemUtil.delay(200);
        robotDrive.setLeftAndRight(0);
        globalSingleton.autodrive = false;
    }

    public void setSoftLowGear(){
        robotDrive.setDriveGain(0.5);
    }

    public void setSoftHighGear(){
        robotDrive.setDriveGain(1.0);
    }

    public double getDriveGain() {
        return robotDrive.getDriveGain();
    }

    public void setLeftAndRight(double left, double right){
        robotDrive.setLeftAndRight(left, right);
    }
}
