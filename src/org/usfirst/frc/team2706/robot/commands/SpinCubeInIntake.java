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
        Robot.intake.leftCube(0.7);
        Robot.intake.rightCube(-0.7);
    }
    
    @Override
    protected void end() {
        Robot.intake.stopMotors();
    }
    
}
