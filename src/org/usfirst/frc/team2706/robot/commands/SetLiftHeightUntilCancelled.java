package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

/**
 * Sets the lift to a specified height and wait for the command to be cancelled
 */
public class SetLiftHeightUntilCancelled extends SetLiftHeight {

    /**
     * Sets the lift to a specified height and wait for the command to be cancelled
     */
    public SetLiftHeightUntilCancelled(double height) {
        super(height);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public void end() {
        Robot.lift.resetSetpoint();
    }
    
}
