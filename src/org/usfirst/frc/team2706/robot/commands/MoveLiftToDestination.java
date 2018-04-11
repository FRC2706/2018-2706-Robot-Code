package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftToDestination extends Command {

    TalonPID liftPID;
    double liftDestination;
    
    public MoveLiftToDestination() {
        liftPID = Robot.lift.getPID();
  }
  protected void initialize() {
      liftPID.setOutputRange(-Lift.SPEED, Lift.SPEED);

      setDestination(Robot.lift.getEncoderHeight());
      Robot.lift.useUpPID();
      liftPID.enable();
  }
  
public void setDestination(double destination) {
//    Log.d(this, "Setting destination to " + destination);
    liftDestination = destination;
    liftPID.setSetpoint(destination);
    
}

    public void execute() {
//        Log.d(this, "Current setpoint: " + liftPID.getSetpoint());
        liftPID.update();
        
        if(Robot.lift.getEncoderHeight() < 0) {
            Robot.lift.reset();
        }
    }
    

    public void end() {
        liftPID.disable();
    }
    
    @Override
    public void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }
}
