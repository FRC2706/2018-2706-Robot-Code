package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

/**
 * Disables the lift motor and begins climbing
 */
public class StartCimbing extends LoggedCommand {

    public StartCimbing() {
        // FIXME: Makes command fail to start
        // requires(Robot.lift);
    }

    @Override
    public void initialize() {
        Log.i(this, "Started Climbing");

        // Stop PID commands from running when climbing so motors aren't fighting the lift
        if (Robot.lift.getDefaultCommand() != null) {
            Robot.lift.getDefaultCommand().cancel();
        }
        if (Robot.lift.getCurrentCommand() != null) {
            Robot.lift.getCurrentCommand().cancel();
        }

        // Allow lift motor to coast, and disable it
        Robot.lift.setBrakeMode(false);
        Robot.lift.disableMotor(true);
    }

    @Override
    public void execute() {
        Robot.climb.climb();
    }

    @Override
    public void end() {
        Robot.climb.stopClimberMotor();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
