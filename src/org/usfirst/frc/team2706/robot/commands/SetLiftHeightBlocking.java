package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

public class SetLiftHeightBlocking extends SetLiftHeightUntilCancelled {

    public SetLiftHeightBlocking(double height) {
        super(height);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Robot.lift.getPID().getSetpoint() - Robot.lift.getEncoderHeight()) < 0.25;
    }
    
    @Override
    public void end() {
        Robot.lift.resetSetpoint();
    }
    
}
