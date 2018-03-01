package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveLiftToDestination extends Command {

    TalonPID liftPID;
    double liftDestination;
    
    
    private double P = 1.0, I = 0.0, D = 0.0;
    
    public MoveLiftToDestination(double destination) {
        this.requires(Robot.lift);
        liftPID = Robot.lift.getPID();
     
        liftPID.setPID(P, I, D);
        
      SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
      SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
      SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
  }

  protected void initialize() {
      liftPID.setPID(SmartDashboard.getNumber("P", P), SmartDashboard.getNumber("I", I), (SmartDashboard.getNumber("D", D)));
      
      liftPID.setOutputRange(-Lift.SPEED, Lift.SPEED);
      liftPID.setSetpoint(liftDestination);
      liftPID.enable();
      
  }
  
public void setDestination(double destination) {
    liftDestination = destination;
    liftPID.setSetpoint(destination);
    Log.d(this, destination);
}

    public void execute() {
        liftPID.update();
    }
    

    public void end() {
        liftPID.disable();
        Log.d(this, "Disabed");
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
