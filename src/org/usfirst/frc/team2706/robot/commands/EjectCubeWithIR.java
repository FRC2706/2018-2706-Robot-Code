package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

/**
 * Ejects a cube until it is no longer in the intake
 */
public class EjectCubeWithIR extends LoggedCommand {

    /**
     * Ejects a cube until it is no longer in the intake
     */
    public EjectCubeWithIR() {
        this.requires(Robot.intake);
    }

    /**
     * Turns on the motors on to eject the cube
     */
    @Override
    public void execute() {
        Robot.intake.exhaleCube(0.8);
    }

    /**
     * Sets both Intake motors to 0, stopping them
     */
    @Override
    public void end() {
        Robot.intake.stopMotors();
    }


    /**
     * Run until IR detects cube is captured
     */
    @Override
    protected boolean isFinished() {
        return Robot.intake.readIRSensor() <= 1.3;
    }

}
