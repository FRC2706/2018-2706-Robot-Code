package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;

import edu.wpi.first.wpilibj.PIDController;

/**
 * This mostly works, but use the QuickRotate command instead. PID control using gyro heading is
 * slower and needs tuning of P,I,D parameters.
 * 
 * Note that the gyro heading is now absolute and not relative, so angle is a target heading and not
 * a relative turn angle.
 */
public class RotateDriveWithGyro extends LoggedCommand {

    private final double speed;

    private final double angle;

    private PIDController pid;

    private final int minDoneCycles;

    private final double P = RobotMap.ROTATE_DRIVE_P, I = RobotMap.ROTATE_DRIVE_I, D = RobotMap.ROTATE_DRIVE_D, F = 0;

    private final double error;

    public RotateDriveWithGyro(double speed, double angle, int minDonecycles, String name) {
        this(speed, angle, 1, minDonecycles, name);
    }

    public RotateDriveWithGyro(double angle, String name) {
        this(AutoConstants.ROTATE_SPEED, angle, AutoConstants.ROTATE_ERROR,
                        AutoConstants.ROTATE_CYCLES, name);
    }

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param angle The angle to rotate to
     * @param name The name of the of the configuration properties to look for
     */
    public RotateDriveWithGyro(double speed, double angle, double error, int minDonecycles,
                    String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed = RobotConfig.get(name + ".speed", speed);

        this.angle = RobotConfig.get(name + ".angle", angle);

        this.minDoneCycles = RobotConfig.get(name + ".minDoneCycles", minDonecycles);

        this.error = RobotConfig.get(name + ".error", error);

        pid = new PIDController(P, I, D, F, Robot.driveTrain.getGyroPIDSource(false),
                        Robot.driveTrain.getDrivePIDOutput(false, false, true));

//         SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//         SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//         SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//         pid.setP(SmartDashboard.getNumber("P", P));
//         pid.setI(SmartDashboard.getNumber("I", I));
//         pid.setD(SmartDashboard.getNumber("D", D));

        Log.i(this, "Rotating " + angle + " degrees at a speed of " + speed);

        Robot.driveTrain.reset();

        pid.setInputRange(-360.0, 360.0);

        // Make input infinite
        pid.setContinuous();
        if (speed > 0) {
            // Set output speed range
            pid.setOutputRange(-speed, speed);
        } else {
            pid.setOutputRange(speed, -speed);
        }

        // Set the tolerance in degrees
        pid.setAbsoluteTolerance(error);

        pid.setSetpoint(angle);

        // Start going to location
        pid.enable();
    }

    private int doneTicks;

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (pid.onTarget())
            doneTicks++;
        else
            doneTicks = 0;

        return doneTicks >= minDoneCycles;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Disable PID output and stop robot to be safe
        pid.disable();
        Log.i(this, "Done rotating, rotated " + Robot.driveTrain.getHeading());
        Robot.driveTrain.drive(0, 0);

        Robot.driveTrain.reset();

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
