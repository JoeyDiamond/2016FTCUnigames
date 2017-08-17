package org.firstinspires.ftc.AusLIB;

import android.util.Log;

/**
 * Created by sailo on 5/14/2016.
 */
public class LinearAutoDriveRoutines {
    public static final String LOGTAG = "linDrive";
    double minAccelVelocity = 0.08;
    double minDecelVelocity = 0.05;

    RobotDrive robotDrive;
    public double ticksToDistance = 1;
    public double SPIN_GAIN = 1;
    public double DRIVE_GAIN = 1;
    public double TURN_TOLERANCE = 1;

    public LinearAutoDriveRoutines(RobotDrive robotDrive, int encTicksToInches){
        this.robotDrive = robotDrive;
        this.robotDrive.turnOnEncoders();

        ticksToDistance = encTicksToInches;
    }

    public LinearAutoDriveRoutines(RobotDrive robotDrive, int encTicksToInches, double
            drive_gyro_gain, double turn_gyro_gain, double turn_tolerance){
        this.robotDrive = robotDrive;
        this.robotDrive.turnOnEncoders();
        ticksToDistance = encTicksToInches;

        SPIN_GAIN = turn_gyro_gain;
        DRIVE_GAIN = drive_gyro_gain;
        TURN_TOLERANCE = turn_tolerance;
    }

    // basic functions
    public void moveDistanceAtSpeed(double distance, double speed, boolean stopEnd){
        robotDrive.resetLeftAndRightEncoders();
        double calculatedDistance = Math.abs(distance) * ticksToDistance;
        double currentPose = Math.abs((robotDrive.getLeftPosition() + robotDrive.getRightPosition()) / 2);
        while(currentPose < calculatedDistance){
            robotDrive.setLeftAndRight(speed,speed);
            currentPose = Math.abs((robotDrive.getLeftPosition() + robotDrive.getRightPosition()) / 2);
        }
        if(stopEnd){
            robotDrive.setAll(0);
        }

    }

    public void turnLeftSideAtSpeed(double distance, double speed, boolean stopEnd){
        robotDrive.resetLeftEncoders();
        double calculatedDistance = Math.abs(distance) * ticksToDistance;
        double currentPose = Math.abs(robotDrive.getLeftPosition());
        while(currentPose < calculatedDistance){
            robotDrive.setLeft(speed);
            currentPose = Math.abs(robotDrive.getLeftPosition());
        }
        if(stopEnd){
            robotDrive.setAll(0);
        }
    }

    public void turnRightSideAtSpeed(double distance, double speed, boolean stopEnd){
        robotDrive.resetRightEncoders();
        double calculatedDistance = Math.abs(distance) * ticksToDistance;
        double currentPose = Math.abs(robotDrive.getRightPosition());
        while(currentPose < calculatedDistance){
            robotDrive.setRight(speed);
            currentPose = Math.abs(robotDrive.getRightPosition());
        }
        if(stopEnd){
            robotDrive.setAll(0);
        }
    }


    // motion profiled motions
    /**
     *
     * @param targetDistance: the distance in mm you want to travel
     * @param maxVelocity: the max velocity you want to travel at
     * @param maxJerk: the max acceleration you want to take in velocity/encoder ticks
     * @param targetHeading: the gyro heading you want to track to
     *
     * This method takes inputs and drives a trapezoidal motion profile along the gyro heading
     * specified.
     */
    public void moveTrapDistanceAtSpeedWithAccelOnHeading(double targetDistance, double
            maxVelocity, double maxJerk, double targetHeading){

        Log.d(LOGTAG, "moveTrapDistanceAtSpeedWithAccelOnHeading: starting calculations");
        // method variables
        int calculatedDistance = (int)Math.abs(targetDistance * ticksToDistance);
        double averageEncoderDistance = 0;
        double currentVelocity = 0;
        double accelStopPoint = 0;
        double decelPoint = 0;
        double headingError = 0;
        boolean decelerating = false;
        robotDrive.resetLeftAndRightEncoders();


        // pre-move calculations
        accelStopPoint = Math.abs(maxVelocity / maxJerk);
        decelPoint = calculatedDistance - accelStopPoint;
        if(accelStopPoint + (calculatedDistance - decelPoint) > calculatedDistance){
            Log.d(LOGTAG,"ERROR: ramps are longer than total move");
            return;
        }

        Log.d(LOGTAG, "moveTrapDistanceAtSpeedWithAccelOnHeading: starting motion");
        // movement code
        while(averageEncoderDistance < calculatedDistance){

            //headingError = targetHeading - heading;

            if(averageEncoderDistance < accelStopPoint){	// if accelerating
                currentVelocity = maxJerk * averageEncoderDistance * Math.signum
                        (maxVelocity);
                Log.d(LOGTAG,"accelerating");
            }
            else if(averageEncoderDistance < decelPoint){	// if cruising
                currentVelocity = maxVelocity;
                Log.d(LOGTAG,"cruising");
            }
            else{	//	if decelerating
                currentVelocity = (-maxJerk * averageEncoderDistance + maxJerk*calculatedDistance)*Math.signum
                        (maxVelocity);
                Log.d(LOGTAG,"decelerating");
                decelerating = true;
            }

            if(decelerating){
                if(Math.abs(currentVelocity) < minDecelVelocity)
                    currentVelocity = Math.signum(maxVelocity) * minDecelVelocity;
            } else {
                if(Math.abs(currentVelocity) < minAccelVelocity)
                    currentVelocity = Math.signum(maxVelocity) * minAccelVelocity;
            }

            Log.d(LOGTAG, "moveTrapDistanceAtSpeedWithAccelOnHeading: currVel: " + currentVelocity);
            robotDrive.setLeft(currentVelocity + (headingError * DRIVE_GAIN));
            robotDrive.setRight(currentVelocity - (headingError * DRIVE_GAIN));

            // update the position of the Encoders
            //averageEncoderDistance = (Math.abs(motorLeft.getEncoderValue()) + Math.abs(motorRight.getEncoderValue()))/2;
            averageEncoderDistance = (Math.abs(robotDrive.getLeftPosition()) + Math.abs(robotDrive
                    .getRightPosition())) *0.5;

        }
        robotDrive.setAll(0.0);
        Log.d(LOGTAG, "moveTrapDistanceAtSpeedWithAccelOnHeading: done");
    }


    /**
     *
     * @param targetDistance: the distance in mm you want to travel
     * @param maxVelocity: the max velocity you want to travel at
     * @param targetHeading: the gyro heading you want to track to
     *
     * This method takes inputs and drives in a sinusoidal motion profile along the
     * gyro heading specified.
     */
    public void moveSinDistanceAtSpeedOnHeading(double targetDistance, double maxVelocity, double targetHeading){

        Log.d(LOGTAG, "moveSinDistanceAtSpeedOnHeading: starting calculations");
        // method variables
        robotDrive.resetLeftAndRightEncoders();
        int calculatedDistance = (int)Math.abs(targetDistance * ticksToDistance);
        double averageEncoderDistance = (Math.abs(robotDrive.getLeftPosition()) + Math.abs(robotDrive
                .getRightPosition())) *0.5;
        double currentVelocity = 0;
        double sinArgument = 0;
        double headingError = 0;
        boolean decelerating = false;
        double prevVel = 0;
        Log.d(LOGTAG,"avgEncDist: " + averageEncoderDistance);

        Log.d(LOGTAG, "moveSinDistanceAtSpeedOnHeading: starting motion");
        // main execution
        while(averageEncoderDistance < calculatedDistance){
            //calculate the heading error
            //headingError = targetHeading - heading;

            // calculate the velocity at each point
            //sinArgument = averageEncoderDistance *(3.14 / calculatedDistance);
            //currentVelocity = maxVelocity * Math.sin(sinArgument);
            sinArgument = (averageEncoderDistance * 3.1415926535) / (0.5 * calculatedDistance);
            currentVelocity = maxVelocity * 0.5 * Math.cos(sinArgument - 3.1415926535) +
                    (maxVelocity * 0.5);

            if(averageEncoderDistance > calculatedDistance*0.5)
                decelerating = true;
            prevVel = currentVelocity;

            if(decelerating){
                if(Math.abs(currentVelocity) < minDecelVelocity)
                    currentVelocity = Math.signum(maxVelocity) * minDecelVelocity;
            } else {
                if(Math.abs(currentVelocity) < minAccelVelocity)
                    currentVelocity = Math.signum(maxVelocity) * minAccelVelocity;
            }



            Log.d(LOGTAG, "moveSinDistanceAtSpeedOnHeading: currVel: " + currentVelocity);
            robotDrive.setLeft(currentVelocity + (headingError * DRIVE_GAIN));
            robotDrive.setRight(currentVelocity - (headingError * DRIVE_GAIN));

            averageEncoderDistance = (Math.abs(robotDrive.getLeftPosition()) + Math.abs(robotDrive
                    .getRightPosition())) *0.5;

        }
        robotDrive.setAll(0);
        Log.d(LOGTAG, "moveSinDistanceAtSpeedOnHeading: done");
    }

    // gyro functions
/*
    public void turnLeftSide(double targetHeading, boolean stopEnd){
        double correction = targetHeading - getHeading();
        correction = correction * SPIN_GAIN;
        while(Math.abs(targetHeading - getHeading()) > TURN_TOLERANCE){
            robotDrive.setLeft(correction);
            correction = targetHeading - getHeading();
            correction = correction * SPIN_GAIN;
        }
        if(stopEnd)
            robotDrive.setLeftAndRight(0,0);
    }

    public void turnRightSide(double targetHeading, boolean stopEnd){
        double correction = targetHeading - getHeading();
        correction = correction * SPIN_GAIN;
        while(Math.abs(targetHeading - getHeading()) > TURN_TOLERANCE){
            robotDrive.setLeft(-correction);
            correction = targetHeading - getHeading();
            correction = correction * SPIN_GAIN;
        }
        if(stopEnd)
            robotDrive.setLeftAndRight(0,0);
    }

    public void turnBothSides(double targetHeading, boolean stopEnd){
        double correction = targetHeading - getHeading();
        correction = correction * SPIN_GAIN;
        while(Math.abs(targetHeading - getHeading()) > TURN_TOLERANCE){
            robotDrive.setLeftAndRight(correction,-correction);
            correction = targetHeading - getHeading();
            correction = correction * SPIN_GAIN;
        }
        if(stopEnd)
            robotDrive.setLeftAndRight(0,0);
    }

    public void moveDistanceAtSpeedOnHeading(double distance, double speed, double targetHeading, boolean stopEnd){
        robotDrive.resetLeftAndRightEncoders();
        double calculatedDistance = Math.abs(distance) * ticksToDistance;
        double currentPose = Math.abs((robotDrive.getLeftPosition() + robotDrive.getRightPosition()) / 2);
        while(currentPose < calculatedDistance){
            double correction = targetHeading - getHeading();
            correction = correction * DRIVE_GAIN;
            robotDrive.setLeftAndRight(speed + correction,speed - correction);
            currentPose = Math.abs((robotDrive.getLeftPosition() + robotDrive.getRightPosition()) / 2);
        }
        if(stopEnd){
            robotDrive.setAll(0);
        }
    }

    /* TODO */
    public double getHeading(double head){
        return head;
    }

}
