package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftUp extends Command {

    public MoveLiftUp() {
        this.requires(Robot.lift);
    }
    
    
  public void initialize() {}
    
 
    public void execute() {
        Robot.lift.moveUp();
    }
    
    public void end() {
        Robot.lift.stop();
        Robot.lift.resetSetpoint();
    }
    

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
