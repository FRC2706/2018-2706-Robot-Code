package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftWithPIDOverride extends Command {
    
    private Lift move;

    public static final double SPEED = 0.5;
    
    private double lastTime;
    
    private final boolean up;
    
    /**
     * Allows us to use the methods in 'Intake'
     */
    public MoveLiftWithPIDOverride(boolean up) {
        move = Robot.lift;
        this.requires(Robot.lift);
        
        this.up = up;
    }

    public void initialize() {
       lastTime = Timer.getFPGATimestamp();
       Robot.lift.resetPID();
       Robot.lift.resetSetpoint();
       
       Robot.lift.setUnsafeCurrentLimit();
    }
    
    
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        Log.d("Move", "Override");
        
        double current = Timer.getFPGATimestamp();
        double delta = current - lastTime;
        
        double position = (up ? 1 : -1) * SPEED * delta;
        
        Robot.lift.setHeight(Robot.lift.getPID().getSetpoint() + position, true);
        
        lastTime = current;
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        Robot.lift.setRegularCurrentLimit();
        
        move.stop();
        Robot.lift.resetSetpoint();
    }

    protected boolean isFinished() {
        return false;
    }

}
