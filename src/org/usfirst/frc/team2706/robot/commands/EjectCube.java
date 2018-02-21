package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class EjectCube extends Command {

    private Intake exhale;
    private Joystick m_joystick;
    private int m_axis;
    
    /**
     * Allows us to use the methods in 'Intake'
     */
    public EjectCube(Joystick joystick, int axis) {
        exhale = Robot.intake;
        this.requires(Robot.intake);
        m_joystick = joystick;
        m_axis = axis;
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {}
    
    /**
     * Turns on the motors on to eject the cube
     */
    public void execute() {
        exhale.exhaleCube(m_joystick.getRawAxis(m_axis)); //TODO check out if correct
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
            return false;     
    }

}