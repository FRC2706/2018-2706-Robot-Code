package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftToDestination extends Command {

    TalonPID liftPID;
   // int liftDestination;
    
    public MoveLiftToDestination() {
        
        liftPID = Robot.lift.getPID();
     
        // Obviously not real values
        liftPID.setPID(1, 0, 0);
    }

    
  public void initialize() {
      liftPID.setOutputRange(-Lift.speed, Lift.speed);
      liftPID.setSetpoint(100);
      liftPID.enable();
      
  }
    

    public void execute() {
    }
    

    public void end() {
        liftPID.disable();
        System.out.println("Lift end");
    }
    

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
