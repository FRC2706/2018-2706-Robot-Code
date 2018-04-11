package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

public class MoveLiftUp extends LoggedCommand {

    public MoveLiftUp() {
        this.requires(Robot.lift);
    }
    
    
  public void initialize() {
      Robot.lift.setUnsafeCurrentLimit();
  }
    
 
    public void execute() {
        Robot.lift.moveUp();
    }
    
    public void end() {
        Robot.lift.setRegularCurrentLimit();
        Robot.lift.stop();
        Robot.lift.resetSetpoint();
    }
    

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
