package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Ejects the cube from the intake
 */
public class EjectCube extends LoggedCommand {

    private final Supplier<Double> speed;

    /**
     * Ejects cube using a joystick axis
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public EjectCube(Joystick stick, int axis) {
        this(() -> stick.getRawAxis(axis));
    }
    
    /**
     * Ejects a cube at a constant speed
     * 
     * @param speed The the speed
     */

    public EjectCube(double speed) {
        this(() -> speed);
    }
    
    /**
     * Ejects a cube at a supplied speed
     * 
     * @param speed The supplier for the speed
     */
    public EjectCube(Supplier<Double> speed) {
        this.speed = speed;
    }
    
    /**
     * Run the motors at the given speed
     */
    @Override
    public void execute() {
        Robot.intake.exhaleCube(speed.get()); 
    }
    
    /**
     * Stops both intake motors
     */
    @Override
    public void end() {
        Robot.intake.stopMotors();
    }
    
    @Override
    protected boolean isFinished() {
        return false;     
    }

}
