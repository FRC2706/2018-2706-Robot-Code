package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;

public class SetLiftHeightBlockingAfterTime extends SetLiftHeightUntilCancelled {

    private int minDoneCycles;
    private double error;
    private int currentCycles = 0;
    private long timeMs;
    private long currentMs;
    public SetLiftHeightBlockingAfterTime(double height, int minDoneCycles, double error, long timeMs) {
        super(height);
        this.timeMs = timeMs;
        this.minDoneCycles = minDoneCycles;
        this.error = error;
    }

    @Override
    public void initialize() {
        
        currentMs = System.currentTimeMillis();
        currentCycles = 0;
    }
    public void execute() {
        if(System.currentTimeMillis() - currentMs >= timeMs) {
            super.initialize();
        }
    }
    
    @Override
    public boolean isFinished() {
        if(System.currentTimeMillis() - currentMs < timeMs) {
            return false;
        }
        Log.d(this, Robot.lift.getPID().getSetpoint() + " " +  Robot.lift.getEncoderHeight());
        return Math.abs(Robot.lift.getPID().getSetpoint() - Robot.lift.getEncoderHeight()) < error && ++currentCycles >= minDoneCycles;
    }
    
    @Override
    public void end() {
        Robot.lift.resetSetpoint();
    }
    
}
