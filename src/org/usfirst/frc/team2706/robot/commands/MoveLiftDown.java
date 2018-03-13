package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftDown extends Command {
    
    public MoveLiftDown() {
        this.requires(Robot.lift);
    }
    
    public void initialize() {
        Robot.lift.setUnsafeCurrentLimit();
    }
      
   
      public void execute() {
          Robot.lift.moveDown();
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
