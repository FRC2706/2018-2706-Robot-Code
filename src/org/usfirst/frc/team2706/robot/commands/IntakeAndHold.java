package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

/**
 * Intakes a cube when it is not being held in the intake
 */
public class IntakeAndHold extends LoggedCommand {

    /**
     * Intakes a cube when it is not being held in the intake
     */
    public IntakeAndHold() {
        this.requires(Robot.intake);

    }

    /**
     * Runs the motors when the cube isn't detected
     */
    @Override
    public void execute() {
        if (Robot.intake.readIRSensor() >= 0.26 && Robot.intake.readIRSensor() < 0.5) {
            Robot.intake.inhaleCube(Robot.oi.getDriverJoystick()
                            .getRawAxis(JoystickMap.XBOX_BACK_RIGHT_TRIGGER));
        }
    }

    /**
     * Stops the intake motors
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
