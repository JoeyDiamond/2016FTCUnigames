package org.firstinspires.ftc.teamcode.UniGames;

/**
 * Created by sailo on 9/20/2016.
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return ourInstance;
    }

    private GlobalSingleton() {
    }

    public boolean automated = false;
    public boolean autodrive = false;
}
