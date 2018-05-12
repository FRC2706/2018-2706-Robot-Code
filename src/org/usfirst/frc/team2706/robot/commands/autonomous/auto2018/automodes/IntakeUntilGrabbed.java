package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.IntakeCubeCustom;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward and intakes until the cube is detected
 */
public class IntakeUntilGrabbed extends LoggedCommand {
    
    private final double driveSpeed;
    private double distance;
    private final Command intakeCube;

    /**
     * Drives forward and intakes until the cube is detected
     * 
     * @param driveSpeed The speed to drive forward at
     * @param leftSpeed The speed of the left intake
     * @param rightSpeed The speed of the right intake
     */
    public IntakeUntilGrabbed(double driveSpeed, double leftSpeed, double rightSpeed) {
        this.driveSpeed = driveSpeed;
        
        intakeCube = new IntakeCubeCustom(leftSpeed, rightSpeed);
    }

    @Override
    public void initialize() {
        intakeCube.start();
    }

    @Override
    public void execute() {
        Robot.driveTrain.drive(driveSpeed, driveSpeed);
    }

    @Override
    public void end() {
        distance = Robot.driveTrain.getDistance();
        Robot.driveTrain.drive(0, 0);
        intakeCube.cancel();
    }

    @Override
    protected boolean isFinished() {
        return Robot.intake.readIRSensor() > 1.75;
    }
    
    /**
     * Gets the distance driven forward
     * 
     * @return The forward distance
     */
    public double getDistanceDriven() {
        return distance;
    }

}
