package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class EjectCubeTimed extends Command {

    private Intake exhale;
    private double time;
    
    /**
     * Allows us to use the methods in 'Intake'
     */
    public EjectCubeTimed() {
        exhale = Robot.intake;
        this.requires(Robot.intake);
    }
    
    public void initialize() {
        time = Timer.getFPGATimestamp();
    }
    
    /**
     * Turns on the motors on to eject the cube
     */
    public void execute() {
        exhale.exhaleCube(1.0); //TODO check out if correct
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
        return Timer.getFPGATimestamp() - time > 0.135;
    }

}