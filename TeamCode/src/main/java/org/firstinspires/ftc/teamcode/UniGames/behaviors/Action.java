package org.firstinspires.ftc.teamcode.UniGames.behaviors;

import org.firstinspires.ftc.teamcode.UniGames.GlobalSingleton;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Intake;
import org.firstinspires.ftc.teamcode.UniGames.subsystems.Lift;


/**
 * Created by sailo on 9/20/2016.
 */

public abstract class Action implements Runnable {
    GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
    Drivebase drivebase = Drivebase.getInstance();
    Intake intake = Intake.getInstance();
    Lift lift = Lift.getInstance();


}
