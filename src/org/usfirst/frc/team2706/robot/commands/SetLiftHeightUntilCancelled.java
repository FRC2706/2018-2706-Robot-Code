package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

public class SetLiftHeightUntilCancelled extends SetLiftHeight {

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
