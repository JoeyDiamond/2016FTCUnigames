package org.firstinspires.ftc.AusLIB;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by sailo on 9/20/2016.
 */

public abstract class Subsystem {
    protected String name;
    protected boolean enabled = false;
    protected Telemetry telemetry;
    protected HardwareMap hardwareMap;
    /**
     * General constructor
     * @param name name of the instance
     */
    public Subsystem(String name){
        this.name = name;
    }

    public abstract void init(HardwareMap hardwareMap, Telemetry telemetry);

    /**
     * enables subsystem operation
     */
    public void enable(){
        enabled = true;
    }

    /**
     * disables subsystem without affecting controlling object
     */
    public void disable(){
        enabled = false;
    }

    /**
     *
     * @return the state of the subsystem
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     *
     * @return the name of the instance
     */
    public String getName(){
        return name;
    }
}
