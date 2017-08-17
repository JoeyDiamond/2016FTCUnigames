package org.firstinspires.ftc.AusLIB;

/**
 * Created by sailo on 5/9/2016.
 */
public class RobotUtil {

    /**
     *
     * @param  joyValue: the value of the joystick to scale
     * @return a squared joystick value with a deadzone of +- 0.1
     *
     * @apiNote
     * This method is designed to take a joystick value (-1 thru +1) and
     * create a scaled value with a deadzone of +-0.1 around 0
     */
    public static float calcThrottleParabola(float joyValue){
        double adjustedValue;
        adjustedValue = (joyValue - 0.1f) * (joyValue - 0.1f) * 1.23456f;
        if(adjustedValue > 0)
            adjustedValue = adjustedValue * Math.signum(joyValue);
        else
            return 0;

        return (float) MathUtil.limitValue(adjustedValue,1);
    }


}
