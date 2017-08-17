package org.firstinspires.ftc.AusLIB;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;

/**
 * Created by sailo on 5/9/2016.
 */
public class RobotDrive {

    public final String LIBTAG = "LIB";

    public final char LEFT_MOTORS = 'L';
    public final char RIGHT_MOTORS = 'R';
    public final char STRAFE_MOTORS = 'S';


    // Variables
    public ArrayList<DcMotor> leftMotors = new ArrayList<DcMotor>();
    public ArrayList<DcMotor> rightMotors = new ArrayList<DcMotor>();
    public ArrayList<DcMotor> strafeMotors = new ArrayList<DcMotor>();
    private int motorsPerWheel = 0;
    private double driveGain = 1.0;
    private int leftDirection = 1;
    private int rightDirection = -1;
    private int strafeDirection = 1;
    private double topHatFwd = 0.25;
    private double topHatTurn = 0.25;
    private double topHatRev = -0.25;

    //////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////

    /**
     * default constructor, not for general use.
     * Use this if you have a unique drivetrain that does not match
     * a current constructor
     */
    public RobotDrive() {
        motorsPerWheel = 0;
    }

    /**
     * @param left:  the left drive motor on your robot
     * @param right: the right drive motor on your robot
     *               <p/>
     *               This constructor creates a new object of type TankDriveTrain
     * @apiNote Used for Tank Drive
     */
    public RobotDrive(DcMotor left, DcMotor right) {
        leftMotors.add(left);
        rightMotors.add(right);
        motorsPerWheel = 1;
    }

    /**
     * @param left1:  the first left motor on your robot
     * @param left2:  the second left motor on your robot
     * @param right1: the first right motor on your robot
     * @param right2: the second right motor on your robot
     * @apiNote Used for Tank Drive
     */
    public RobotDrive(DcMotor left1, DcMotor left2, DcMotor right1, DcMotor right2) {
        leftMotors.add(left1);
        leftMotors.add(left2);
        rightMotors.add(right1);
        rightMotors.add(right2);
        motorsPerWheel = 2;
    }

    /**
     * @param left1:   first left drive motor
     * @param left2:   second left drive motor
     * @param right1:  first right drive motor
     * @param right2:  second right drive motor
     * @param strafe1: first strafing drive motor
     * @param strafe2: second strafing drive motor
     * @apiNote Used for H-Drive
     */
    public RobotDrive(DcMotor left1, DcMotor left2, DcMotor right1, DcMotor right2, DcMotor strafe1, DcMotor strafe2) {
        leftMotors.add(left1);
        leftMotors.add(left2);
        rightMotors.add(right1);
        rightMotors.add(right2);
        strafeMotors.add(strafe1);
        strafeMotors.add(strafe2);
        motorsPerWheel = 2;
    }

    /**
     * @param left1:   first left drive motor
     * @param left2:   second left drive motor
     * @param right1:  first right drive motor
     * @param right2:  second right drive motor
     * @param strafe1: single strafing drive motor
     * @apiNote Used for H-Drive
     */
    public RobotDrive(DcMotor left1, DcMotor left2, DcMotor right1, DcMotor right2, DcMotor strafe1) {
        leftMotors.add(left1);
        leftMotors.add(left2);
        rightMotors.add(right1);
        rightMotors.add(right2);
        strafeMotors.add(strafe1);
        motorsPerWheel = 2;
    }

    /**
     * @param left1:   single left drive motor
     * @param right1:  single right drive motor
     * @param strafe1: single strafing drive motor
     * @apiNote Used for H-Drive or Kiwi-Drive
     */
    public RobotDrive(DcMotor left1, DcMotor right1, DcMotor strafe1) {
        leftMotors.add(left1);
        rightMotors.add(right1);
        strafeMotors.add(strafe1);
        motorsPerWheel = 1;
    }

    //////////////////////////////////////////////////////////////////////
    // Methods
    //////////////////////////////////////////////////////////////////////

    public void resetLeftEncoders(){
        for(int i=0;i<leftMotors.size();i++){
            leftMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        while(leftMotors.get(0).getCurrentPosition() != 0){
            SystemUtil.delay(2);
        }
        for(int i=0;i<leftMotors.size();i++){
            leftMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void resetRightEncoders(){
        for(int i=0;i<rightMotors.size();i++){
            rightMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        while(rightMotors.get(0).getCurrentPosition() != 0){
            SystemUtil.delay(2);
        }
        for(int i=0;i<rightMotors.size();i++){
            rightMotors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void resetLeftAndRightEncoders(){
        resetLeftEncoders();
        resetRightEncoders();
    }

    public double getLeftPosition(){
        return leftMotors.get(0).getCurrentPosition();
    }

    public double getRightPosition(){
        return rightMotors.get(0).getCurrentPosition();
    }

    /**
     * The drive gain that all outputs are multiplied by.
     * @return drive gain
     */
    public double getDriveGain() {
        return driveGain;
    }

    /**
     * A gain control to adjust all motor output speeds by. This acts as a programatic gear shift
     * for more control
     * @param gain the new value
     * @return the new value
     */
    public double setDriveGain(double gain) {
        driveGain = gain;
        return driveGain;
    }

    public void configTopHatControl(double fwd, double turn, double rev){
        topHatFwd = Math.abs(fwd);
        topHatTurn = Math.abs(turn);
        topHatRev = -Math.abs(rev);
    }

    /**
     * @param speed: set the left side drive motors to a speed
     */
    public void setLeft(double speed) {
        double throttle = MathUtil.limitValue(speed * driveGain);

        for (int i = 0; i < leftMotors.size(); i++) {
            leftMotors.get(i).setPower(throttle * leftDirection);
        }
    }

    /**
     * @param speed: set the right side drive motors to a speed
     */
    public void setRight(double speed) {
        double throttle = MathUtil.limitValue(speed * driveGain);

        for (int i = 0; i < rightMotors.size(); i++) {
            rightMotors.get(i).setPower(throttle * rightDirection);
        }
    }

    public void setStrafe(double speed) {
        double throttle = MathUtil.limitValue(speed * driveGain);

        for (int i = 0; i < strafeMotors.size(); i++) {
            strafeMotors.get(i).setPower(throttle * strafeDirection);
        }
    }

    public void set4Wheels(double leftFront, double leftBack, double rightFront, double rightBack) {
        double throttleleftFront = MathUtil.limitValue(leftFront * driveGain);
        double throttleleftBack = MathUtil.limitValue(leftBack * driveGain);
        double throttleRightFront = MathUtil.limitValue(rightFront * driveGain);
        double throttleRightBack = MathUtil.limitValue(rightBack * driveGain);


        leftMotors.get(0).setPower(throttleleftFront * leftDirection);
        rightMotors.get(0).setPower(throttleRightFront * rightDirection);

        if(leftMotors.get(1) != null){
            leftMotors.get(1).setPower(throttleleftBack * leftDirection);
            rightMotors.get(1).setPower(throttleRightBack * rightDirection);
        }

    }

    public void setAll(double left, double right, double strafe) {
        setLeft(left);
        setRight(right);
        setStrafe(strafe);
    }

    public void setAll(double throttle) {
        throttle = MathUtil.limitValue(throttle * driveGain);

        setLeft(throttle);
        setRight(throttle);
        setStrafe(throttle);
    }

    /**
     * @param speedLeft:  left side speed
     * @param speedRight: right side speed
     */
    public void setLeftAndRight(double speedLeft, double speedRight) {
        setLeft(speedLeft);
        setRight(speedRight);
    }

    /**
     * @param speed: set both side drive motors to the same speed
     */
    public void setLeftAndRight(double speed) {
        setLeft(speed);
        setRight(speed);
    }

    /**
     * reverses a side of the drivetrain
     *
     * @param side:     'L' or 'l' for left, 'R' or 'r' for right, 'S' or 's' for strafing
     * @param direction true for forward, false for reverse
     */
    public void changeSideDirection(char side, boolean direction) {
        Log.d(LIBTAG, "changeSideDirection: started");
        if (side == 'L' || side == 'l') {
            if (direction) {
                Log.d(LIBTAG, "changeSideDirection: left side forward");
                leftDirection = 1;
            } else {
                Log.d(LIBTAG, "changeSideDirection: left side reversed");
                leftDirection = -1;
            }
        } else if (side == 'R' || side == 'r') {
            if (direction) {
                Log.d(LIBTAG, "changeSideDirection: right side forward");
                rightDirection = 1;
            } else {
                Log.d(LIBTAG, "changeSideDirection: right side reversed");
                rightDirection = -1;
            }
        } else if (side == 'S' || side == 's' || side == 'C' || side == 'c') {
            if (direction) {
                Log.d(LIBTAG, "changeSideDirection: strafe side forward");
                strafeDirection = 1;
            } else {
                Log.d(LIBTAG, "changeSideDirection: strafe side reversed");
                strafeDirection = -1;
            }
        }
    }

    /**
     * Reverses which end is considered the "front" of the robot
     */
    public void reverseFrontOfRobot() {
        ArrayList<DcMotor> storage = new ArrayList<DcMotor>();
        for (int i = 0; i < leftMotors.size(); i++) {
            storage.add(leftMotors.get(i));
            leftMotors.set(i, rightMotors.get(i));
        }
        for (int i = 0; i < rightMotors.size(); i++) {
            rightMotors.set(i, storage.get(i));
        }
        if (strafeMotors.size() != 0) {
            if (strafeMotors.get(0).getDirection() == DcMotor.Direction.REVERSE)
                changeSideDirection('S', true);
            else
                changeSideDirection('S', false);
        }

        swapReversedSides();
    }

    /**
     * Easy method for reversing the directions of both sides of the drivetrain
     * Primarily for initial setup.
     */
    public void swapReversedSides() {
        if (leftDirection == 1)
            leftDirection = -1;
        else
            leftDirection = 1;

        if (rightDirection == 1)
            rightDirection = -1;
        else
            rightDirection = 1;

    }

    /**
     * turns on all drive motor encoders
     */
    public void turnOnEncoders() {
        for (int i = 0; i < leftMotors.size(); i++) {
            leftMotors.get(i).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotors.get(i).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        for (int i = 0; i < strafeMotors.size(); i++) {
            strafeMotors.get(i).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     * turns off all drive motor encoders
     */
    public void turnOffEncoders() {
        for (int i = 0; i < leftMotors.size(); i++) {
            leftMotors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightMotors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        for (int i = 0; i < strafeMotors.size(); i++) {
            strafeMotors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }



    //////////////////////////////////////////////////////////////////////
    // Drive Controls
    //////////////////////////////////////////////////////////////////////


    /**
     * @param gpad: Pass the chassis driver gamepad here.
     * @apiNote This method is the standard way of driving the robot (not under autodrive)
     * If the tophat is being used, it takes priority and provides a slow arcade control of
     * the drivetrain. Otherwise, a squared range of the joysticks are calculated using calcThrottleParabola
     */
    public void tankDriveStandard(Gamepad gpad) {


        if (gpad.dpad_up) {
            setLeftAndRight(topHatFwd);
        } else if (gpad.dpad_right) {
            setLeftAndRight(topHatTurn, -topHatTurn);
        } else if (gpad.dpad_down) {
            setLeftAndRight(topHatRev);
        } else if (gpad.dpad_left) {
            setLeftAndRight(-topHatTurn, topHatTurn);
        } else {
            setLeftAndRight(-RobotUtil.calcThrottleParabola(gpad.left_stick_y), -RobotUtil
                    .calcThrottleParabola(gpad.right_stick_y));
        }
    }

    public void tankDriveStandard(GamepadEnhanced gpad){
        if (gpad.dpad_up) {
            setLeftAndRight(topHatFwd);
        } else if (gpad.dpad_right) {
            setLeftAndRight(topHatTurn, -topHatTurn);
        } else if (gpad.dpad_down) {
            setLeftAndRight(topHatRev);
        } else if (gpad.dpad_left) {
            setLeftAndRight(-topHatTurn, topHatTurn);
        } else {
            setLeftAndRight(gpad.getSquaredAxis(GamepadEnhanced.AXIS.AXIS_LEFT_STICK_Y),
                    gpad.getSquaredAxis(GamepadEnhanced.AXIS.AXIS_RIGHT_STICK_Y));
        }
    }

    public void tankDriveBasic(Gamepad gpad) {
        setLeftAndRight(MathUtil.scaleClipped(-gpad.left_stick_y), MathUtil.scaleClipped(-gpad
                .right_stick_y));
    }

    public void tankDriveBasic(GamepadEnhanced gpad){
        setLeftAndRight(-MathUtil.scaleClipped(-gpad.left_stick_y), -MathUtil.scaleClipped(-gpad
                .right_stick_y));
    }

    public void tankDriveBasicSquared(Gamepad gpad) {
        setLeftAndRight(-RobotUtil.calcThrottleParabola(gpad.left_stick_y), -RobotUtil
                .calcThrottleParabola(gpad.right_stick_y));
    }

    public void tankDriveBasicSquared(GamepadEnhanced gpad){
        setLeftAndRight(-RobotUtil.calcThrottleParabola(gpad.left_stick_y), -RobotUtil
                .calcThrottleParabola(gpad.right_stick_y));
    }

    public void tankDriveAxes(float yLeft, float yRight) {
        setLeftAndRight(MathUtil.limitValue(-yLeft), MathUtil.limitValue(-yRight));
    }

    public void tankDriveAxesSquared(float yLeft, float yRight) {
        setLeftAndRight(-RobotUtil.calcThrottleParabola(yLeft), -RobotUtil.calcThrottleParabola
                (yRight));
    }

    public void arcadeDriveSingleLeftStick(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) + MathUtil.scaleClipped(gpad.left_stick_x)), MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) - MathUtil.scaleClipped(gpad.left_stick_x)));
    }

    public void arcadeDriveSingleLeftStick(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) + MathUtil.scaleClipped(gpad.left_stick_x)), MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) - MathUtil.scaleClipped(gpad.left_stick_x)));
    }

    public void arcadeDriveSingleLeftStickSquared(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) - RobotUtil.calcThrottleParabola(gpad.left_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) + RobotUtil.calcThrottleParabola(gpad.left_stick_x)));
    }

    public void arcadeDriveSingleLeftStickSquared(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) - RobotUtil.calcThrottleParabola(gpad.left_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) + RobotUtil.calcThrottleParabola(gpad.left_stick_x)));
    }

    public void arcadeDriveSingleRightStick(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.right_stick_y) + MathUtil.scaleClipped(gpad.right_stick_x)),
                MathUtil.limitValue(MathUtil.scaleClipped(-gpad.right_stick_y) - MathUtil.scaleClipped(gpad.right_stick_x)));
    }

    public void arcadeDriveSingleRightStick(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.right_stick_y) + MathUtil.scaleClipped(gpad.right_stick_x)),
                MathUtil.limitValue(MathUtil.scaleClipped(-gpad.right_stick_y) - MathUtil.scaleClipped(gpad.right_stick_x)));
    }

    public void arcadeDriveSingleRightStickSquared(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.right_stick_y) - RobotUtil.calcThrottleParabola(gpad.right_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.right_stick_y) + RobotUtil.calcThrottleParabola(gpad.right_stick_x)));
    }

    public void arcadeDriveSingleRightStickSquared(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.right_stick_y) - RobotUtil.calcThrottleParabola(gpad.right_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.right_stick_y) + RobotUtil.calcThrottleParabola(gpad.right_stick_x)));
    }

    public void arcadeDriveSplitStick(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) + MathUtil.scaleClipped(gpad.right_stick_x)),
                MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) - MathUtil.scaleClipped(gpad.right_stick_x)));
    }

    public void arcadeDriveSplitStick(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) + MathUtil.scaleClipped(gpad.right_stick_x)),
                MathUtil.limitValue(MathUtil.scaleClipped(-gpad.left_stick_y) - MathUtil.scaleClipped(gpad.right_stick_x)));
    }

    public void arcadeDriveSplitStickSquared(Gamepad gpad) {
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) - RobotUtil.calcThrottleParabola(gpad.right_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) + RobotUtil.calcThrottleParabola(gpad.right_stick_x)));
    }

    public void arcadeDriveSplitSticksquared(GamepadEnhanced gpad){
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) - RobotUtil.calcThrottleParabola(gpad.right_stick_x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(gpad.left_stick_y) + RobotUtil.calcThrottleParabola(gpad.right_stick_x)));
    }

    public void arcadeDriveAxes(float y, float x) {
        setLeftAndRight(MathUtil.limitValue(-y + x),
                MathUtil.limitValue(-y - x));
    }

    public void arcadeDriveAxesSquared(float y, float x) {
        setLeftAndRight(MathUtil.limitValue(RobotUtil.calcThrottleParabola(y) - RobotUtil.calcThrottleParabola(x)),
                MathUtil.limitValue(RobotUtil.calcThrottleParabola(y) + RobotUtil.calcThrottleParabola(x)));
    }

    public void kiwiDriveAxes(float forward, float strafe, float rotate) {
        double leftW = (-rotate / 2) + (0.866 * forward);
        double rightW = (-rotate / 2) - (0.866 * forward);
        double backW = strafe + rotate;

        setAll(leftW, rightW, backW);
    }

    public void kiwiDriveAxesSquared(float forward, float strafe, float rotate) {
        forward = RobotUtil.calcThrottleParabola(forward);
        strafe = RobotUtil.calcThrottleParabola(strafe);
        rotate = RobotUtil.calcThrottleParabola(rotate);

        double leftW = (-rotate / 2) + (0.866 * forward);
        double rightW = (-rotate / 2) - (0.866 * forward);
        double backW = strafe + rotate;

        setAll(leftW, rightW, backW);
    }

    public void mecanumAxes(float forward, float strafe, float rotate) {
        double leftFW = forward + rotate + strafe;
        double leftBW = forward + rotate - strafe;
        double rightFW = forward - rotate - strafe;
        double rightBW = forward - rotate + strafe;

        set4Wheels(leftFW, leftBW, rightFW, rightBW);
    }

    public void mecanumAxesSquared(float forward, float strafe, float rotate) {
        forward = RobotUtil.calcThrottleParabola(forward);
        strafe = RobotUtil.calcThrottleParabola(strafe);
        rotate = RobotUtil.calcThrottleParabola(rotate);

        double leftFW = forward + rotate + strafe;
        double leftBW = forward + rotate - strafe;
        double rightFW = forward - rotate - strafe;
        double rightBW = forward - rotate + strafe;

        set4Wheels(leftFW, leftBW, rightFW, rightBW);
    }

    public void mecanumTank(float leftForward, float leftStrafe, float rightForward, float rightStrafe) {
        double leftFW = leftForward + leftStrafe;
        double leftBW = leftForward - leftStrafe;
        double rightFW = rightForward - rightStrafe;
        double rightBW = rightForward + rightStrafe;

        set4Wheels(leftFW, leftBW, rightFW, rightBW);
    }

    public void mecanumTankSquared(float leftForward, float leftStrafe, float rightForward, float rightStrafe) {
        leftForward = RobotUtil.calcThrottleParabola(leftForward);
        leftStrafe = RobotUtil.calcThrottleParabola(leftStrafe);
        rightForward = RobotUtil.calcThrottleParabola(rightForward);
        rightStrafe = RobotUtil.calcThrottleParabola(rightStrafe);

        double leftFW = leftForward + leftStrafe;
        double leftBW = leftForward - leftStrafe;
        double rightFW = rightForward - rightStrafe;
        double rightBW = rightForward + rightStrafe;

        set4Wheels(leftFW, leftBW, rightFW, rightBW);
    }

    public void omniHoloAxes(float forward, float strafe, float rotate) {
        mecanumAxes(forward, strafe, rotate);
    }

    public void omniHoloAxesSquared(float forward, float strafe, float rotate) {
        mecanumAxesSquared(forward, strafe, rotate);
    }

    public void omniHoloTank(float leftForward, float leftStrafe, float rightForward, float rightStrafe) {
        mecanumTank(leftForward, leftStrafe, rightForward, rightStrafe);
    }

    public void omniHoloTankSquared(float leftForward, float leftStrafe, float rightForward, float rightStrafe) {
        mecanumTankSquared(leftForward, leftStrafe, rightForward, rightStrafe);

    }

    public void cheesyDriveSimple(double throttle, double wheel){
        cheesyDrive(throttle, wheel, (wheel >= wheelDeadband && throttle <= throttleDeadband), true);
    }

    // Cheesy Drive Variables
    double oldWheel, quickStopAccumulator;
    double throttleDeadband = 0.02;
    double wheelDeadband = 0.02;
    public static double kDriveSensitivity = 0.75; // was 0.75
    public static double kNegativeInertiaScalar = 5.0;

    public void cheesyDrive(double throttle, double wheel, boolean isQuickTurn, boolean isHighGear) {
        double wheelNonLinearity;

        wheel = handleDeadband(wheel, wheelDeadband);
        throttle = handleDeadband(throttle, throttleDeadband);

        double negInertia = wheel - oldWheel;
        oldWheel = wheel;

        if (isHighGear) {
            wheelNonLinearity = 0.6;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        } else {
            wheelNonLinearity = 0.5;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        }

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;
        if (isHighGear) {
            negInertiaScalar = 4.0;
            sensitivity = kDriveSensitivity;
        } else {
            if (wheel * negInertia > 0) {
                negInertiaScalar = 2.5;
            } else {
                if (Math.abs(wheel) > 0.65) {
                    negInertiaScalar = 5.0;
                } else {
                    negInertiaScalar = 3.0;
                }
            }
            sensitivity = .85; // Constants.sensitivityLow.getDouble();
        }
        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;

        wheel = wheel + negInertiaAccumulator;
        if (negInertiaAccumulator > 1) {
            negInertiaAccumulator -= 1;
        } else if (negInertiaAccumulator < -1) {
            negInertiaAccumulator += 1;
        } else {
            negInertiaAccumulator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator
                        + alpha * MathUtil.limitValue(wheel, 1.0) * 5;
            }
            overPower = 1.0;
            if (isHighGear) {
                sensitivity = 1.0;
            } else {
                sensitivity = 1.0;
            }
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity
                    - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }

        setLeftAndRight(leftPwm,rightPwm);
    }

    protected double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }

    public void culverDrive(double throttle, double steeringX, double steeringY){
        if(throttle == 0 && steeringX != 0){
            setLeftAndRight(steeringX,-steeringX);
            return;
        }

        Vector steeringVect = new Vector(steeringX,steeringY);

        double throttleLeft = throttle * steeringVect.radiansFrom(Math.PI * 0.5);
        double throttleRight = throttle * steeringVect.radiansFrom(Math.PI * 0.5);

        setLeftAndRight(throttleLeft, throttleRight);
    }

    protected double oldTurn;

    public void wheelDrive(double move, double turn, boolean quickTurn){
        double left = 0;
        double right = 0;
        double leftOverpower = 0;
        double rightOverpower = 0;
        double overPowerGain = 0.1;
        boolean useOverpower = false;

        // calculate negative inertia to help robot anticipate driver actions
        double negIntertia = turn - oldTurn;
        oldTurn = turn;

        if(quickTurn){
            left = move + turn;
            right = move - turn;
        } else {
            left = move + Math.abs(move)*turn;
            right = move - Math.abs(move)*turn;
        }

        if(!useOverpower){
            setLeftAndRight(left,right);
            return;
        }

        if(left > 1){
            leftOverpower = left - 1;
            left = 1;
        }else if(left < -1){
            leftOverpower = left + 1;
            left = -1;
        }

        if(right > 1){
            rightOverpower = right - 1;
            right = 1;
        } else if(right < -1){
            rightOverpower = right + 1;
            right = -1;
        }

        left = left - rightOverpower*overPowerGain;
        right = right - leftOverpower*overPowerGain;

        setLeftAndRight(left,right);

    }
}
