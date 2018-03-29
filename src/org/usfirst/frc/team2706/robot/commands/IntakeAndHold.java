package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeAndHold extends Command {

    private Intake inhale;
    @SuppressWarnings("unused")
    private AnalogInput IR_sensor;
    @SuppressWarnings("unused")
    private double m_motorPower;
    /**
     * Allows us to use the methods in 'Intake'
     */
    public IntakeAndHold(double motorPower) {
        inhale = Robot.intake;
        this.requires(Robot.intake);
        m_motorPower = motorPower;
       
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        if (inhale.readIRSensor() >= 0.26 && inhale.readIRSensor() < 0.5) {
            inhale.inhaleCube(Robot.oi.getDriverJoystick().getRawAxis(JoystickMap.XBOX_BACK_RIGHT_TRIGGER)); 
            
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
       // if (inhale.cubeCaptured() == true) {
     //       return true;
     //   }
    //    else {
            return false;
    //    }
        
    }

}
