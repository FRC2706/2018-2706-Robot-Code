package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
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
        Log.i(this, "Ended");
        Robot.lift.resetSetpoint();
        Robot.lift.disableMotor(false);
    }

}
