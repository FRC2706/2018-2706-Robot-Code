package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;


public class IntakeCube extends LoggedCommand {

    private Intake inhale;

    private final Supplier<Double> speed;
    
    private boolean sameRatio;
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public IntakeCube(Joystick stick, int axis, boolean sameRatio) {
        this(() -> stick.getRawAxis(axis), sameRatio);
        
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The the speed
     */
    public IntakeCube(double speed, boolean sameRatio) {
        this(() -> speed, sameRatio);
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The supplier for the speed
     */
    public IntakeCube(Supplier<Double> speed, boolean sameRatio) {
        inhale = Robot.intake;
        this.speed = speed;
        this.sameRatio = sameRatio;
      //  this.requires(Robot.intake);
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        if(sameRatio) {
            inhale.inhaleCubeStatic(speed.get());
        }
        else {
            inhale.inhaleCube(speed.get());
        }
           
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        inhale.stopMotors();
    }
    

    @Override
    /**
     * Used to detect whether the motors should stop
     */
    protected boolean isFinished() {
            return false;
    }

}
