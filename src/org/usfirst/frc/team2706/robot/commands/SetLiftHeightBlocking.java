package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;

public class SetLiftHeightBlocking extends SetLiftHeightUntilCancelled {

    private int minDoneCycles;
    private double error;
    private int currentCycles = 0;
    public SetLiftHeightBlocking(double height, int minDoneCycles, double error) {
        super(height);
        this.minDoneCycles = minDoneCycles;
        this.error = error;
    }

    @Override
    public void initialize() {
        super.initialize();
        
        currentCycles = 0;
    }
    
    @Override
    public boolean isFinished() {
        Log.d(this, Robot.lift.getPID().getSetpoint() + " " +  Robot.lift.getEncoderHeight());
        return Math.abs(Robot.lift.getPID().getSetpoint() - Robot.lift.getEncoderHeight()) < error && ++currentCycles >= minDoneCycles;
    }
    
    @Override
    public void end() {
        Robot.lift.resetSetpoint();
    }
    
}
