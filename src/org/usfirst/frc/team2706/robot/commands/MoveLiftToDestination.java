package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftToDestination extends Command {

    TalonPID liftPID;
    double liftDestination;
    
    
    private double P = 0.5, I = 0.0, D = 0.0;
    
    public MoveLiftToDestination() {
        liftPID = Robot.lift.getPID();
     
        Log.d(this, "Using PID values of " + P + " " + I + " " + D);
        liftPID.setPID(P, I, D);
        
//      SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//      SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//      SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
  }
  protected void initialize() {
//      liftPID.setPID(SmartDashboard.getNumber("P", P), SmartDashboard.getNumber("I", I), (SmartDashboard.getNumber("D", D)));
      liftPID.setOutputRange(-Lift.SPEED, Lift.SPEED);

      setDestination(Robot.lift.getEncoderHeight());
      liftPID.enable();
  }
  
public void setDestination(double destination) {
    Log.d(this, "Setting destination to " + destination);
    liftDestination = destination;
    liftPID.setSetpoint(destination);
    
}

    public void execute() {
        Log.d(this, "Current setpoint: " + liftPID.getSetpoint());
        liftPID.update();
    }
    

    public void end() {
        Log.d(this, "Ended");
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
