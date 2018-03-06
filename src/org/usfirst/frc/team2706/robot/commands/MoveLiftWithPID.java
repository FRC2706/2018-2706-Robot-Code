package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class MoveLiftWithPID extends Command {
    
    private Lift move;
    
    private final Supplier<Double> liftspeed;

    public static final double SPEED_UP_PER_SECOND = 2.2;
    public static final double SPEED_DOWN_PER_SECOND = 3.5;
    
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
       Robot.lift.setHeight(Robot.lift.getEncoderHeight());
    }
    
    
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        double current = Timer.getFPGATimestamp();
        double delta = current - lastTime;
        
        double speed;
        if(liftspeed.get() < 0) {
            speed = SPEED_DOWN_PER_SECOND;
        }
        else {
            speed = SPEED_UP_PER_SECOND;
        }
        
        double position = speed * delta * liftspeed.get();
        
        Robot.lift.setHeight(Robot.lift.getPID().getSetpoint() + position);
        
        lastTime = current;
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        move.stop();
        Robot.lift.setHeight(Robot.lift.getEncoderHeight());
    }

    protected boolean isFinished() {
        return false;
    }

}
