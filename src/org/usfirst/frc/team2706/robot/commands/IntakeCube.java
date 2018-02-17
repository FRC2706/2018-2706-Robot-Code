package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeCube extends Command {

    private Intake inhale;
    private AnalogInput IR_sensor;
    /**
     * Allows us to use the methods in 'Intake'
     */
    public IntakeCube() {
        inhale = Robot.intake;
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        System.out.println(IR_sensor.getVoltage());
        if (IR_sensor.getVoltage() >= 0.5) {
            inhale.inhaleCube(Robot.oi.getDriverJoystick().getRawAxis(3)); 
            
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
        if (inhale.cubeCaptured() == true) {
            return true;
        }
        else {
            return false;
        }
        
    }

}
