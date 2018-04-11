package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

public class EjectCubeWithIR extends LoggedCommand {

    private Intake exhale;
    
    /**
     * Allows us to use the methods in 'Intake'
     */
    public EjectCubeWithIR() {
        exhale = Robot.intake;
        this.requires(Robot.intake);
    }
    
    /**
     * Turns on the motors on to eject the cube
     */
    public void execute() {
        exhale.exhaleCube(1.0);
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
        return exhale.readIRSensor() <= 0.65;
    }

}