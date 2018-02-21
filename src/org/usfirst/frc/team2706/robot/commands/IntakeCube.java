package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;


public class IntakeCube extends Command {

    private Intake inhale;
    private final Supplier<Double> speed;
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public IntakeCube(Joystick stick, int axis) {
        this(() -> stick.getRawAxis(axis));
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The the speed
     */
    public IntakeCube(double speed) {
        this(() -> speed);
    }
    
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param speed The supplier for the speed
     */
    public IntakeCube(Supplier<Double> speed) {
        inhale = Robot.intake;
        this.speed = speed;
        this.requires(Robot.intake);
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        System.out.println(inhale.readIRSensor());
        if (inhale.readIRSensor() >= 0.26 && inhale.readIRSensor() < 0.5) {
            inhale.inhaleCube(speed.get()); 
            
        }
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        System.out.println("Ended IntakeCube command");
        inhale.stopMotors();
    }
    

    @Override
    /**
     * Used to detect whether the motors should stop
     */
    protected boolean isFinished() {
       // if (inhale.cubeCaptured() == true) {
     //       return true;
     //   }
    //    else {
            return false;
    //    }
        
    }

}
