package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.IntakeCubeCustom;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward and intakes until the cube is detected
 */
public class IntakeUntilGrabbedNoDrive extends LoggedCommand {
    private final Command intakeCube;

    /**
     * Drives forward and intakes until the cube is detected
     * 
     * @param leftSpeed The speed of the left intake
     * @param rightSpeed The speed of the right intake
     */
    public IntakeUntilGrabbedNoDrive(double leftSpeed, double rightSpeed) {
        intakeCube = new IntakeCubeCustom(leftSpeed, rightSpeed);
    }

    @Override
    public void initialize() {
        intakeCube.start();
    }

    @Override
    public void end() {
        Robot.driveTrain.drive(0, 0);
        intakeCube.cancel();
    }

    @Override
    protected boolean isFinished() {
        return Robot.intake.readIRSensor() > 1.75;
    }

}
