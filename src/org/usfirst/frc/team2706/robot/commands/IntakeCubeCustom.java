package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;


public class IntakeCubeCustom extends Command {

    private Intake inhale;

    private double leftSpeed;
    
    private double rightSpeed;
    /**
     * Allows us to use the methods in 'Intake'
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public IntakeCubeCustom(double leftSpeed, double rightSpeed) {
        inhale = Robot.intake;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        inhale.inhaleCubeCustom(leftSpeed, rightSpeed);
           
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
            return false;
    }

}
