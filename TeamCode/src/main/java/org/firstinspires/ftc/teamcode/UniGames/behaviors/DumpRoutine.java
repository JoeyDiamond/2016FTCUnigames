package org.firstinspires.ftc.teamcode.UniGames.behaviors;

import android.util.Log;

import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.teamcode.UniGames.Constants;

/**
 * Created by sailo on 9/23/2016.
 */

public class DumpRoutine extends Action {

    String TAG = "dumpRoutine";


    @Override
    public void run() {
        lift.setTargetPosition(Constants.LIFT_DUMP_POSITION);

        lift.setBucketTiltPosition(Constants.BUCKET_TILT_CENTER);
        lift.setPortDoorPosition(Constants.PORT_DOOR_INTAKE);
        lift.setStbdDoorPosition(Constants.STBD_DOOR_INTAKE);

        intake.setSpinnerIntake();
        intake.setTargetPosition(Constants.ARM_STORE_POSITION);
        Log.d(TAG, "run: initial conditions set");

        SystemUtil.delay(50);
        while(!lift.isInPosition()){
            SystemUtil.slowLoopDelay();
        }

        Log.d(TAG, "run: lift extended");
        intake.setTargetPosition(Constants.ARM_DUMP_POSITION);

        while(!intake.isInPosition()){
            SystemUtil.slowLoopDelay();
        }
        intake.setSpinnerEject();

        Log.d(TAG, "run: arm in dump position");
        SystemUtil.delay(2000);

        intake.setTargetPosition(Constants.ARM_STORE_POSITION);
        intake.stopSpinner();
        Log.d(TAG, "run: arm set to store");

        while(!intake.isInPosition()){
            SystemUtil.slowLoopDelay();
        }

        Log.d(TAG, "run: moving lift back down");
        lift.setTargetPosition(Constants.LIFT_STORE_POSITION);

        lift.setPortDoorPosition(Constants.PORT_DOOR_CLOSED);
        lift.setStbdDoorPosition(Constants.STBD_DOOR_CLOSED);

        for(int i=0; i<5;i++){
            lift.setBucketTiltPosition(Constants.BUCKET_TILT_CENTER + 0.1);
            SystemUtil.delay(200);
            lift.setBucketTiltPosition(Constants.BUCKET_TILT_CENTER - 0.1);
            SystemUtil.delay(200);
        }

        lift.setBucketTiltPosition(Constants.BUCKET_TILT_CENTER);

        globalSingleton.automated = false;

        Log.d(TAG, "run: COMPLETE");

    }
}
