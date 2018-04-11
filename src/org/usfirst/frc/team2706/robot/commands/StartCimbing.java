package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Climber;

public class StartCimbing extends LoggedCommand {

private Climber climb;

    public StartCimbing() {
        // TODO Auto-generated constructor stub
        climb = Robot.climb;
    }
    
    public void initialize() {
        Log.i(this, "Started Climbing");
        // Stop PID stuff when climbing so motors are fighting the lift
        if(Robot.lift.getDefaultCommand() != null) {
            Robot.lift.getDefaultCommand().cancel();
        }
        
        if(Robot.lift.getCurrentCommand() != null) {
            Robot.lift.getCurrentCommand().cancel();
        }
        
        Robot.lift.setBrakeMode(false);
        Robot.lift.disableMotor();
    }
    
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
