package org.firstinspires.ftc.teamcode.UniGames;

/**
 * Created by sailo on 9/20/2016.
 */

public class Constants {

    // drivebase
    public static final double PORT_HOOK_RETRACTED = 0.5;
    public static final double PORT_HOOK_EXTENDED = 0.0;
    public static final double STBD_HOOK_RETRACTED = 0.5;
    public static final double STBD_HOOK_EXTENDED = 1.0;

    // intake
    public static final int ARM_DOWN_POSITION = 0;
    public static final int ARM_STORE_POSITION = 1100;
    public static final int ARM_MOUNTAIN_POSITION = 1450;
    public static final int ARM_SAFE_POSITION = 1500;
    public static final int ARM_DUMP_POSITION = 1700;
    public static final int ARM_MAX = 2000;
    public static final int ARM_MIN = 0;
    public static final double SPINNER_INTAKE_SPEED = 1.0;
    public static final double SPINNER_EJECT_SPEED = -0.8;
    public static final double SPINNER_STOPPED_SPEED = 0.0;

    // lift
    public static final int LIFT_MAX = 4500;
    public static final int LIFT_MIN = 0;
    public static final int LIFT_STORE_POSITION = 0;
    public static final int LIFT_DUMP_POSITION = 800;
    public static final int LIFT_SCORE_LOW_BASKET = 1000;
    public static final int LIFT_SCORE_MID_BASKET = 2200;
    public static final int LIFT_SCORE_HIGH_BASKET = 4100;
    public static final int LIFT_HANG_POSITION = 4800;

    // bucket
    public static final double BUCKET_TILT_CENTER = 0.4;
    public static final double BUCKET_TILT_PORT = 0.2;
    public static final double BUCKET_TILT_STBD = 0.6;
    public static final double PORT_DOOR_CLOSED = 0.0;
    public static final double PORT_DOOR_DUMP = 0.5;
    public static final double PORT_DOOR_STORED = 1.0;
    public static final double PORT_DOOR_INTAKE = 0.2;
    public static final double STBD_DOOR_CLOSED = 1.0;
    public static final double STBD_DOOR_DUMP = 0.4;
    public static final double STBD_DOOR_STORED = 0.0;
    public static final double STBD_DOOR_INTAKE = 0.7;
    public static final boolean TIP_PORT = true;
    public static final boolean TIP_STBD = false;
}
