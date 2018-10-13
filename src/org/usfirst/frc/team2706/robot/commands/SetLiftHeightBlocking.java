package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

/**
 * Sets the lift to a specified height and waits until it arrives to complete
 */
public class SetLiftHeightBlocking extends SetLiftHeightUntilCancelled {

    private final int minDoneCycles;
    private final double error;
    private int currentCycles = 0;

    /**
     * Sets the lift to a specified height and waits until it arrives to complete
     * 
     * @param height The height to set the lift to
     * @param minDoneCycles The number of cycles that the lift is within the minimum error to
     *        complete
     * @param error The maximum acceptable distance from the setpoint
     */
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
        // Check to see if the lift is in the correct position and for the minimum number of ticks
        return Math.abs(Robot.lift.getPID().getSetpoint() - Robot.lift.getEncoderHeight()) < error
                        && ++currentCycles >= minDoneCycles;
    }
}
