package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class EjectCube extends Command {

    private Intake exhale;
    
    /**
     * Allows us to use the methods in 'Intake'
     */
    public EjectCube() {
        exhale = Robot.intake;
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns on the motors on to eject the cube
     */
    public void execute() {
        exhale.exhaleCube(Robot.oi.getDriverJoystick().getRawAxis(2)); //TODO check out if correct
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        exhale.stopMotors();
    }
    

    @Override
    /**
     * Used to detect whether the motors should stop
     */
    protected boolean isFinished() {
        if (exhale.cubeCaptured() == false) {
            return true;
        }
        else {
            return false;
        }
        
    }

}