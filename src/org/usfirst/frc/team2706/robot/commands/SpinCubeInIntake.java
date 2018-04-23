package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SpinCubeInIntake extends Command {

    @Override
    protected boolean isFinished() {
        return false;
    }
    
    @Override
    protected void initialize() {
        Robot.intake.leftCube(2.0);
        Robot.intake.rightCube(-2.0);
    }
    
    @Override
    protected void end() {
        Robot.intake.stopMotors();
    }
    
}
