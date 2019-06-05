package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

/**
 * Intake with different speeds for each side
 */
public class IntakeCubeCustom extends LoggedCommand {

    private double leftSpeed;

    private double rightSpeed;

    /**
     * Intake with different speeds for each side
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     */
    public IntakeCubeCustom(double leftSpeed, double rightSpeed) {
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    /**
     * Run the motors at the given speed
     */
    @Override
    public void execute() {
        Robot.intake.inhaleCubeCustom(leftSpeed, rightSpeed);

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
