package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Moves the lift at a specified speed using override control
 */
public class MoveLift extends LoggedCommand {
    
    private final Supplier<Double> liftspeed;

    /**
     * Moves the lift using a joystick axis
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public MoveLift(Joystick stick, int axis) {
        this(stick, axis, false);
    }
    
    /**
     * Moves the lift using a joystick axis with a specified inversion
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     * @param invert Whether to invert the axis values or not
     */
    public MoveLift(Joystick stick, int axis, boolean invert) {
        this(() -> (invert ? -1 : 1) * stick.getRawAxis(axis));
    }
    
    /**
     * Moves the lift at a constant speed
     * 
     * @param speed The speed to move the lift
     */
    public MoveLift(double speed) {
        this(() -> speed);
    }
    
    /**
     * Moves the lift at a supplied speed
     * 
     * @param speed The supplier for the speed
     */
    public MoveLift(Supplier<Double> speed) {
        this.liftspeed = speed;
        this.requires(Robot.lift);
    }

    @Override
    public void initialize() {
        Robot.lift.setUnsafeCurrentLimit();
    }
    
    /**
     * Run the motors at the given speed
     */
    @Override
    public void execute() {
        Robot.lift.move(liftspeed.get());
    }
    
    /**
     * Sets the setpoint to the current height, and return to regular current limitting
     */
    @Override
    public void end() {
        Robot.lift.setRegularCurrentLimit();
        Robot.lift.stop();
        Robot.lift.resetSetpoint();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
