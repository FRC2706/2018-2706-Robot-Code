package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftWithPID extends Command {
    
    private Lift move;
    
    private final Supplier<Double> liftspeed;

    public static final double SPEED_UP_PER_SECOND = 7.0 / 2.0;
    public static final double SPEED_DOWN_PER_SECOND = 7.0 / 1.3;
    
    public static final double MIN_HEIGHT = 0.5;
    
    private double lastTime;
    
    public MoveLiftWithPID(Joystick stick, int axis) {
        this(stick, axis, false);
    }
    
    public MoveLiftWithPID(Joystick stick, int axis, boolean invert) {
        this(() -> (invert ? -1 : 1) * stick.getRawAxis(axis));
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The the speed
     */
    public MoveLiftWithPID(double speed) {
        this(() -> speed);
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The supplier for the speed
     */
    public MoveLiftWithPID(Supplier<Double> speed) {
        move = Robot.lift;
        this.liftspeed = speed;
        this.requires(Robot.lift);
    }

    public void initialize() {
       lastTime = Timer.getFPGATimestamp();
       Robot.lift.useUpPID();
       Robot.lift.resetSetpoint();
    }
    
    
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
      //  Log.d("Move", "Regular");
        
        double current = Timer.getFPGATimestamp();
        double delta = current - lastTime;
        
        double speed;
        if(liftspeed.get() < 0) {
            speed = SPEED_DOWN_PER_SECOND;
            
         //   Log.d(this, Robot.lift.getEncoderHeight() + " " + Robot.lift.getPID().getSetpoint());
            
            if(Robot.lift.getEncoderHeight() < MIN_HEIGHT || Robot.lift.getPID().getSetpoint() < MIN_HEIGHT) {
             //   Log.d(this, "Going to zero!");
                OneTimeCommand.run(new SetLiftHeight(0));
                return;
            }
        }
        else {
            speed = SPEED_UP_PER_SECOND;
        }
        
        double position = speed * delta * liftspeed.get();
        
        Robot.lift.setHeight(Robot.lift.getPID().getSetpoint() + position, false);
        
        lastTime = current;
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
     //   move.stop();
        Robot.lift.resetSetpoint();
    }

    protected boolean isFinished() {
        return false;
    }

}
