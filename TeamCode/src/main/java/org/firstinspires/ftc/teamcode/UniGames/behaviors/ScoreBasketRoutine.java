package org.firstinspires.ftc.teamcode.UniGames.behaviors;

import org.firstinspires.ftc.AusLIB.SystemUtil;
import org.firstinspires.ftc.teamcode.UniGames.Constants;

/**
 * Created by sailo on 9/23/2016.
 */

public class ScoreBasketRoutine extends Action {

    int topHeight;
    boolean dumpDirection;

    public ScoreBasketRoutine(int topPosition, boolean direction){
        topHeight = topPosition;
        dumpDirection = direction;
    }

    @Override
    public void run() {
        // get the intake out of the way
        if(!intake.isLiftSafeToMove()){
            intake.setTargetPosition(Constants.ARM_SAFE_POSITION);
            while(!intake.isLiftSafeToMove()){
                SystemUtil.slowLoopDelay();
            }
        }

        // extend the lift
        lift.setTargetPosition(topHeight);
        SystemUtil.delay(100);
        while(!lift.isInPosition()){
            SystemUtil.slowLoopDelay();
        }

        if(dumpDirection == Constants.TIP_PORT){
            lift.setBucketTiltPosition(Constants.BUCKET_TILT_PORT);
            lift.setPortDoorPosition(Constants.PORT_DOOR_DUMP);
            lift.setStbdDoorPosition(Constants.STBD_DOOR_CLOSED);

            //SystemUtil.delay(500);

            for(int i=0;i<20;i++){
                lift.setBucketTiltPosition(Constants.BUCKET_TILT_PORT + 0.1);
                lift.setPortDoorPosition(Constants.PORT_DOOR_DUMP + 0.1);
                SystemUtil.delay(50);
                lift.setBucketTiltPosition(Constants.BUCKET_TILT_PORT);
                lift.setPortDoorPosition(Constants.PORT_DOOR_DUMP);
                SystemUtil.delay(50);
            }

        } else {
            lift.setBucketTiltPosition(Constants.BUCKET_TILT_STBD);
            lift.setPortDoorPosition(Constants.PORT_DOOR_CLOSED);
            lift.setStbdDoorPosition(Constants.STBD_DOOR_DUMP);

            //SystemUtil.delay(500);

            for(int i=0;i<20;i++) {
                lift.setBucketTiltPosition(Constants.BUCKET_TILT_STBD - 0.1);
                lift.setPortDoorPosition(Constants.STBD_DOOR_DUMP - 0.1);
                SystemUtil.delay(50);
                lift.setBucketTiltPosition(Constants.BUCKET_TILT_STBD);
                lift.setPortDoorPosition(Constants.STBD_DOOR_DUMP);
                SystemUtil.delay(50);
            }
        }



        new StoreLiftAndUnhook().run();
    }
}
