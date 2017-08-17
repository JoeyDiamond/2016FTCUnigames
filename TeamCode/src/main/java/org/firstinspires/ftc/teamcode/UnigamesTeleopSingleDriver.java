package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.AusLIB.GamepadEnhanced;
import org.firstinspires.ftc.teamcode.UniGames.Constants;
import org.firstinspires.ftc.teamcode.UniGames.GlobalSingleton;
import org.firstinspires.ftc.teamcode.UniGames.behaviors.DumpRoutine;
import org.firstinspires.ftc.teamcode.UniGames.behaviors.ScoreBasketRoutine;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Intake;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Lift;
import org.firstinspires.ftc.teamcode.tduLIB.MultiLooper;

/**
 * Created by sailo on 9/20/2016.
 */

@TeleOp(name="Unigames Single Driver", group="Opmode")  // @Autonomous(...) is the other common  choice
public class UnigamesTeleopSingleDriver extends OpMode {

    GamepadEnhanced driverGamepad = new GamepadEnhanced(gamepad1);
    //GamepadEnhanced operatorGamepad = new GamepadEnhanced(gamepad2);

    GlobalSingleton globalSingleton = GlobalSingleton.getInstance();

    Drivebase drivebase = Drivebase.getInstance();
    Intake intake = Intake.getInstance();
    Lift lift = Lift.getInstance();
    MultiLooper multiLooper = new MultiLooper("subsystems", 1.0 / 200.0);

    Thread automation;

    boolean firstCycle = true;

    boolean dumpDirection = Constants.TIP_STBD;

    @Override
    public void init() {
        automation = new Thread();
        drivebase.init(hardwareMap, telemetry);
        intake.init(hardwareMap, telemetry);
        lift.init(hardwareMap, telemetry);

        multiLooper.addLoopable(intake);
        multiLooper.addLoopable(lift);
        multiLooper.start();

        globalSingleton.autodrive = false;
        globalSingleton.automated = false;

        telemetry.addData("Status", "Initialized");

        firstCycle = true;
    }

    @Override
    public void loop() {
        if(firstCycle){
            firstCycle = false;

            drivebase.enable();
            intake.enable();
            lift.enable();

            intake.setTargetPosition(Constants.ARM_DOWN_POSITION);

            drivebase.retractHook();
        }

        // update joysticks
        driverGamepad.update(gamepad1);
        Log.d("MAIN", "loop: gamepads updated");

        // drive controls
        //telemetry.addData("autodrive", globalSingleton.automated);
        //telemetry.addData("drive enabled", drivebase.isEnabled());
        if(!globalSingleton.autodrive) {
            Log.d("MAIN", "loop: entered manual driving");

            drivebase.drive(driverGamepad);

            if(driverGamepad.getLeft_bumper()){
                //drivebase.setSoftLowGear();
                drivebase.retractHook();
            }
            if(driverGamepad.getRight_bumper()){
                //drivebase.setSoftHighGear();
                drivebase.deployHook();
            }


        }
        Log.d("MAIN", "loop: drive controls done");


        // intake control
        if(driverGamepad.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_LEFT_TRIGGER)){
            intake.intake();
        }
        if(driverGamepad.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_RIGHT_TRIGGER) &&
                !globalSingleton.automated){
            // start dump routine
            //intake.stopSpinner();
            //intake.setTargetPosition(Constants.ARM_STORE_POSITION);
            globalSingleton.automated = true;
            automation = new Thread(new DumpRoutine());
            automation.start();
        }


        // lift control
        /*if(operatorGamepad.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_RIGHT_TRIGGER) &&
                !globalSingleton.automated){
            globalSingleton.automated = true;
            automation = new Thread(new StoreLiftAndUnhook());
            automation.start();
        } else */
        if(driverGamepad.getA() && !globalSingleton.automated){
            // low basket position and dump
            globalSingleton.automated = true;
            automation = new Thread(new ScoreBasketRoutine(Constants.LIFT_SCORE_LOW_BASKET, dumpDirection));
            automation.start();
        } else if(driverGamepad.getB() || driverGamepad.getX()&& !globalSingleton.automated){
            // mid basket position and dump
            globalSingleton.automated = true;
            automation = new Thread(new ScoreBasketRoutine(Constants.LIFT_SCORE_MID_BASKET, dumpDirection));
            automation.start();
        } else if(driverGamepad.getY()&& !globalSingleton.automated){
            // high basket position and dump
            globalSingleton.automated = true;
            automation = new Thread(new ScoreBasketRoutine(Constants.LIFT_SCORE_HIGH_BASKET, dumpDirection));
            automation.start();
        }

        /*if(operatorGamepad.getStart() && operatorGamepad.getBack()){
            // go to hang extension
            lift.setTargetPosition(Constants.LIFT_HANG_POSITION);
        } */


        if(driverGamepad.getStart()){
            lift.setTargetPosition(Constants.LIFT_SCORE_LOW_BASKET);
            intake.stopSpinner();
            intake.setTargetPosition(Constants.ARM_MOUNTAIN_POSITION);

        }

    }

    public void stop(){
        firstCycle = true;
        intake.disable();
        lift.disable();
        drivebase.disable();
        multiLooper.stop();
        //automation.stop();
    }


}
