package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;

/**
 * Makes the lift move to a specified height
 */
public class SetLiftHeight extends OneTimeCommand {
    
    /**
     * Makes the lift move to a specified height
     * 
     * @param height The height in feet that the lift should go to
     */
    public SetLiftHeight(double height) {
        super(() -> {
            // Use up PID when moving upwards
            if(height < Robot.lift.getEncoderHeight()) {
                Robot.lift.useDownPID();
                 Robot.lift.setHeight(height, false);
            }
            // Use down PID when moving downwards
            else {
                Robot.lift.useDownPID();
                Robot.lift.setHeight(height, false);
            }
        });
    }
}
