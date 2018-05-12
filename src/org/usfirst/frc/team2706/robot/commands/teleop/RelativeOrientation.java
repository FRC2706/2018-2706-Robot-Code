package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

public class RelativeOrientation extends LoggedCommand {

    public void initialize() {
        Robot.driveTrain.resetGyro();
    }
    public void execute() {
        Robot.oi.getDriverJoystick().getRawAxis(JoystickMap.XBOX_LEFT_AXIS_Y);
        
    }
    @Override
    protected boolean isFinished() {
        return false;
    }

}
