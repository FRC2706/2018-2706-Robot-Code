package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeCube extends Command {

    private Intake inhale;
    private AnalogInput IR_sensor;
    private Joystick m_joystick;
    private int m_axis;
    /**
     * Allows us to use the methods in 'Intake'
     */
    public IntakeCube(Joystick joystick, int axis) {
        inhale = Robot.intake;
        this.requires(Robot.intake);
        m_joystick = joystick;
        m_axis = axis;
                        
       
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
           inhale.inhaleCube(m_joystick.getRawAxis(m_axis));
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
