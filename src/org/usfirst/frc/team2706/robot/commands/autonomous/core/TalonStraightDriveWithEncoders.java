package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;

/**
 * Have the robot drive certain distance
 */
// FIXME: Make Talons hold position until both are finished
public class TalonStraightDriveWithEncoders extends LoggedCommand {

    private final double speed;

    private final double distance;

    private final double error;

    private TalonPID pid;

    private final int minDoneCycles;

    private final double P = 7.5, I = 2.0, D = 25, F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in
     * @param name The name of the of the configuration properties to look for
     */
    public TalonStraightDriveWithEncoders(double speed, double distance, double error,
                    int minDoneCycles, String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed = RobotConfig.get(name + ".speed", speed);

        this.distance = RobotConfig.get(name + ".distance", distance);

        this.error = RobotConfig.get(name + ".error", error) / 12;

        this.minDoneCycles = RobotConfig.get(name + ".minDoneCycles", minDoneCycles);

        pid = Robot.driveTrain.getTalonPID();
        pid.setPID(P, I, D, F);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Log.i(this, "Talon driving " + distance + " feet at a speed of " + speed);

        Robot.driveTrain.reset();

        Robot.driveTrain.brakeMode(true);

        // Set output speed range
        if (speed > 0) {
            pid.setOutputRange(-speed, speed);
        } else {
            pid.setOutputRange(speed, -speed);
        }

        Robot.driveTrain.initGyro = Robot.driveTrain.getHeading();

        pid.setSetpoint(distance);

        // Will accept within 5 inch of target
        pid.setError(error);

        // Start going to location
        pid.enable();

        this.doneTicks = 0;
    }

    private int doneTicks;

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        if (pid.isOnTarget())
            doneTicks++;
        else
            doneTicks = 0;

        return doneTicks >= minDoneCycles;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Disable PID output and stop robot to be safe
        pid.disable();
        Robot.driveTrain.drive(0, 0);

        Log.i(this, "Done driving");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
