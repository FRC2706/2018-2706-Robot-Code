package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;

/**
 * Keeps robot in brake mode until the command is interrupted
 */
public class HandBrake extends LoggedCommand {

    /**
     * Puts robot into brake mode and can disable driving if wanted
     * 
     * @param stopDriving Whether to disable driving
     * @param name The name of the of the configuration properties to look for
     */
    public HandBrake(boolean stopDriving, String name) {
        super(name);

        if (RobotConfig.get(name + ".stopDriving", stopDriving))
            requires(Robot.driveTrain);
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.brakeMode(true);
    }

    @Override
    protected void end() {
        Robot.driveTrain.brakeMode(false);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
