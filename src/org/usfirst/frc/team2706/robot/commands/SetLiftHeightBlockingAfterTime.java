package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

/**
 * Sets the lift to a specified height after the delay has passed and waits until it arrives to complete
 */
public class SetLiftHeightBlockingAfterTime extends SetLiftHeightUntilCancelled {

    private final int minDoneCycles;
    private final double error;
    private int currentCycles = 0;
    private final long timeMs;
    private long currentMs;
    
    /**
     * Sets the lift to a specified height after the delay has passed and waits until it arrives to complete
     * 
     * @param height The height to set the lift to
     * @param minDoneCycles The number of cycles that the lift is within the minimum error to complete
     * @param error The maximum acceptable distance from the setpoint
     * @param timeMs The time in milliseconds to delay setting the lift height
     */
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
    
    @Override
    public void execute() {
        // XXX: Command will be initialized multiple times
        if(System.currentTimeMillis() - currentMs >= timeMs) {
            // Delay has passed so initialize object11
            super.initialize();
        }
    }
    
    @Override
    public boolean isFinished() {
        // Wait until command has started to check if the lift is at its destination
        if(System.currentTimeMillis() - currentMs < timeMs) {
            return false;
        }
        
        // Check to see if the lift is in the correct position and for the minimum number of ticks
        return Math.abs(Robot.lift.getPID().getSetpoint() - Robot.lift.getEncoderHeight()) < error && ++currentCycles >= minDoneCycles;
    }
    
    @Override
    public void end() {
        Robot.lift.resetSetpoint();
    }
    
}
