package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;

public class SetLiftHeight extends OneTimeCommand {
    
    public SetLiftHeight(double height) {
        super(() -> {
            if(height < Robot.lift.getEncoderHeight()) {
                Robot.lift.useDownPID();
                 Robot.lift.setHeight(height, false);
            }
            else {
                Robot.lift.useDownPID();
                Robot.lift.setHeight(height, false);
            }
        });
    }
}
