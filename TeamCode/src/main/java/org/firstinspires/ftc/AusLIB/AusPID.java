package org.firstinspires.ftc.AusLIB;

import android.util.Log;

/**
 * Created by sailo on 6/19/2016.
 */
public class AusPID implements Controller {

    // variables
    protected double kP;
    protected double kI;
    protected double kD;
    protected double targetVal;
    protected double previousError = 0;
    protected double errorSum = 0;
    protected boolean firstCycle;
    protected double maxErrorIncrement = 1;
    protected double errorTolerance = 1;
    protected double maxOutput;
    protected double minNumCycles;
    protected double cycleCount;
    protected boolean shouldLogCat;

    /**
     * Normal use constructor
     * @param p coefficient for proportional term
     * @param i coefficient for integral term
     * @param d coefficient for derivative term
     * @param tolerance
     */
    public AusPID(double p, double i, double d, double tolerance){
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.errorTolerance = tolerance;

        this.targetVal = 0;
        this.maxOutput = 1;
        this.maxErrorIncrement = 1;

        this.cycleCount = 0;
        this.minNumCycles = 5;
        this.firstCycle = true;
        this.shouldLogCat = false;
    }

    /**
     * Default constructor defines a PID that will give outputs of only zero
     */
    public AusPID(){
        this(0,0,0,0);
    }

    /**
     * Constructor if desired tolerance is 0
     * @param p coefficient for proportional term
     * @param i coefficient for integral term
     * @param d coefficient for derivative term
     */
    public AusPID(double p, double i, double d){
        this(p,i,d,0);
    }

    /**
     * Updates PID coefficents
     * @param p coefficient for proportional term
     * @param i coefficient for integral term
     * @param d coefficient for derivative term
     */
    public void setConstants(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    /**
     * enables logging to the logcat stream
     */
    public void enableLoging(){
        this.shouldLogCat = true;
    }

    /**
     * Sets the acceptable error tolerance
     * @param tolerance the acceptable error tolerance
     */
    public void setErrorTolerance(double tolerance){
        this.errorTolerance = tolerance;
    }

    /**
     * Set target setpoint
     * @param target the new target value
     */
    public void setTarget(double target){
        this.targetVal = target;
    }

    @Override
    public double getTarget() {
        return this.targetVal;
    }

    /**
     * Configure the maximum output of the system, limited from 0-1
     * @param max max output of the system
     */
    public void setMaxOutput(double max){
        if(max < 0.0) {
            this.maxOutput = 0.0;
        } else if(max > 1.0) {
            this.maxOutput = 1.0;
        } else {
            this.maxOutput = max;
        }
    }

    /**
     * Configure the minimum number of cycles when checking if isOnTarget()
     * @param num number of cycles
     */
    public void setMinNumCycles(int num){
        this.minNumCycles = num;
    }

    /**
     * Reset the integral windup sum to 0
     */
    public void resetErrorSum(){
        this.errorSum = 0.0;
    }

    public double getTargetVal(){
        return this.targetVal;
    }

    public double calcOutput(double current){
        return calcPIDError(this.targetVal - current);
    }

    /**
     * calculates the PID output based on error
     * @param error the error between the target and current value
     * @return
     */
    public double calcPIDError(double error){
        double pVal = 0.0;
        double iVal = 0.0;
        double dVal = 0.0;

        // handle first cycle condition where previous error is unknown
        if(this.firstCycle) {
            this.previousError = error;
            this.firstCycle = false;
        }

        // P value calculation
        pVal = error * this.kP;

        // I value calculation
        if(error > this.errorTolerance){ // if positive error is outside of tolerance
            if(this.errorSum < 0.0){ // if error buildup is in wrong direction
                this.errorSum = 0.0;
            }

            this.errorSum += Math.min(error, this.maxErrorIncrement);

        } else if(error < -1*this.errorTolerance) { // if negative error is outside of tolerance
            if(this.errorSum < 0.0){ // if error buildup is in wrong direction
                this.errorSum = 0.0;
            }

            this.errorSum += Math.max(error, -1.0*this.maxErrorIncrement);

        } else { // error is within acceptable tolerance
            this.errorSum = 0;
        }

        iVal = this.errorSum * this.kI;

        // D value calculation
        double deriv = error - this.previousError;
        dVal = deriv * this.kD;

        // sum the terms
        double output = pVal + iVal + dVal;

        // limit the output
        output = MathUtil.limitValue(output,this.maxOutput);

        // store previous error
        this.previousError = error;

        if(this.shouldLogCat){
            Log.d("AusPID", "error: " + error);
            Log.d("AusPID", "pVal: " + pVal);
            Log.d("AusPID", "iVal: " + iVal);
            Log.d("AusPID", "dVal: " + dVal);
            Log.d("AusPID", "output: " + output);
        }
        
        return output;
    }

    /**
     * Ensures the current is within tolerance of target for a given number of cycles
     * i.e. is holding at target
     * @return true if error is less than tolerance for specified number of cycles
     */
    public boolean isOnTarget(){
        double currError = Math.abs(this.previousError);

        //close enough to target
        if(currError <= this.errorTolerance) {
            this.cycleCount++;
        }
        //not close enough to target
        else {
            this.cycleCount = 0;
        }

        return this.cycleCount > this.minNumCycles;
    }

    public void reset(){
        this.cycleCount = 0;
        this.previousError = 0;
        this.firstCycle = true;
        this.errorSum = 0;
    }
}
