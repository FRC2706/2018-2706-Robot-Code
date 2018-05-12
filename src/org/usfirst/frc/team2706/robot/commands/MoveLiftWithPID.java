package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * Controls lift using a speed that moves the setpoint of the lift (manual control)
 */
public class MoveLiftWithPID extends LoggedCommand {
    
    private final Supplier<Double> liftspeed;

    /**
     * The speed in feet per second that the lift setpoint changes when moving upwards
     */
    public static final double SPEED_UP_PER_SECOND = 6.4 / 2.0;
    
    /**
     * The speed in feet per second that the lift setpoint changes when moving downwards
     */
    public static final double SPEED_DOWN_PER_SECOND = 7.0 / 1.3;
    
    /**
     * The height in feet at which to make the lift go to the bottom
     */
    public static final double MIN_HEIGHT = 0.5;
    
    private double lastTime;
    
    /**
     * Moves the lift using a joystick axis
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public MoveLiftWithPID(Joystick stick, int axis) {
        this(stick, axis, false);
    }
    
    /**
     * Moves the lift using a joystick axis with a specified inversion
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     * @param invert Whether to invert the axis values or not
     */
    public MoveLiftWithPID(Joystick stick, int axis, boolean invert) {
        this(() -> (invert ? -1 : 1) * stick.getRawAxis(axis));
    }
    
    /**
     * Moves the lift at a constant speed
     * 
     * @param speed The the speed
     */
    public MoveLiftWithPID(double speed) {
        this(() -> speed);
    }
    
    /**
     * Moves the lift at a supplied speed
     * 
     * @param speed The supplier for the speed
     */
    public MoveLiftWithPID(Supplier<Double> speed) {
        this.liftspeed = speed;
        this.requires(Robot.lift);
    }

    @Override
    public void initialize() {
       lastTime = Timer.getFPGATimestamp();
       Robot.lift.useUpPID();
       Robot.lift.resetSetpoint();
    }
    
    /**
     * Run the motors at the given speed
     */
    @Override
    public void execute() {
        // Find the change in time since the last tick
        double current = Timer.getFPGATimestamp();
        double delta = current - lastTime;
        
        double speed;
        // Sending lift down
        if(liftspeed.get() < 0) {
            // Use downward speed
            speed = SPEED_DOWN_PER_SECOND;
            
            // The lift is below the minimum height and moving downwards, so smoothly go to the bottom
            if(Robot.lift.getEncoderHeight() < MIN_HEIGHT || Robot.lift.getPID().getSetpoint() < MIN_HEIGHT) {
                OneTimeCommand.run(new SetLiftHeight(0));
                return;
            }
        }
        // Sending the lift upwards
        else {
            speed = SPEED_UP_PER_SECOND;
        }
        
        // Calculate the change in the lift position using the lift speed as a percentage to apply
        double position = speed * delta * liftspeed.get();
        
        // Set the lift to the new height
        Robot.lift.setHeight(Robot.lift.getPID().getSetpoint() + position, false);
        
        // Update the time of the last tick
        lastTime = current;
    }
    
    @Override
    protected boolean isFinished() {
        return false;
    }

}
