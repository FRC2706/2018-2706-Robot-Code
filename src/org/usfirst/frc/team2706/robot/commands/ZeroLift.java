package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @deprecated Use {@link SetLiftHeight} instead
 */
public class ZeroLift extends Command {

    public ZeroLift() {
        requires(Robot.lift);
    }
    
    public void execute() {
        Robot.lift.moveDown();
    }
    
    @Override
    protected boolean isFinished() {
        return Robot.lift.bottomLimit();
    }
    
    @Override
    public void end() {
        Robot.lift.stop();
        Robot.lift.resetSetpoint();
    }

}
