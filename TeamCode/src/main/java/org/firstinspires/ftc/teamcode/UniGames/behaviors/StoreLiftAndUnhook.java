package org.firstinspires.ftc.teamcode.UniGames.behaviors;

import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.teamcode.UniGames.Constants;

/**
 * Created by sailo on 9/20/2016.
 */

public class StoreLiftAndUnhook extends Action {

    @Override
    public void run() {
        globalSingleton.autodrive = true;
        drivebase.setLeftAndRight(0.0,0.0);

        if(intake.getTargetPosition() > Constants.ARM_SAFE_POSITION){
            intake.setTargetPosition(Constants.ARM_SAFE_POSITION);
        }


        lift.setTargetPosition(Constants.LIFT_SCORE_LOW_BASKET);
        lift.setBucketTiltPosition(Constants.BUCKET_TILT_CENTER);
        lift.setPortDoorPosition(Constants.PORT_DOOR_CLOSED);
        lift.setStbdDoorPosition(Constants.STBD_DOOR_CLOSED);
        while(!lift.isInPosition()){
            SystemUtil.slowLoopDelay();
        }

        drivebase.unhookUnderLoad();
        globalSingleton.autodrive = true;
        drivebase.setLeftAndRight(0.75,0.75);
        SystemUtil.delay(50);

        lift.store();
        SystemUtil.delay(100);
        while(!lift.isInPosition()){
            SystemUtil.slowLoopDelay();
        }

        globalSingleton.autodrive = false;
        globalSingleton.automated = false;
    }
}
