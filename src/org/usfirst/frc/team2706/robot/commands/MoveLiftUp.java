package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

/**
 * Moves the lift up
 */
public class MoveLiftUp extends LoggedCommand {

    /**
     * Moves the lift up
     */
    public MoveLiftUp() {
        this.requires(Robot.lift);
    }

    @Override
    public void initialize() {
        Robot.lift.setUnsafeCurrentLimit();
    }
    
    @Override
    public void execute() {
        Robot.lift.moveUp();
    }
    
    @Override
    public void end() {
        Robot.lift.setRegularCurrentLimit();
        Robot.lift.stop();
        Robot.lift.resetSetpoint();
    }
    
    @Override
    protected boolean isFinished() {
        return false;
    }
}
