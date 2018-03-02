package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class StartCimbing extends Command {

private Climber climb;

    public StartCimbing() {
        // TODO Auto-generated constructor stub
        climb = Robot.climb;
    }
    
    public void initialize() {}
    
    public void execute() {
        climb.climb();
    }

    public void end() {
        climb.stopClimberMotor();
    }
    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

}
