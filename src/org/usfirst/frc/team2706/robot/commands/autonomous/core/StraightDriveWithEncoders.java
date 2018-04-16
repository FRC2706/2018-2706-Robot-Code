package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDController;

/**
 * Have the robot drive certain distance
 */
public class StraightDriveWithEncoders extends LoggedCommand {

    private final double speed;

    private final double distance;

    private final double error;

    private PIDController PID;

    private final int minDoneCycles;

    private final double P = RobotMap.STRAIGHT_DRIVE_P,
                         I = RobotMap.STRAIGHT_DRIVE_I,
                         D = RobotMap.STRAIGHT_DRIVE_D,
                         F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in
     * @param name The name of the of the configuration properties to look for
     */
    public StraightDriveWithEncoders(double speed, double distance, double error, int minDoneCycles,
                    String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed = RobotConfig.get(name + ".speed", speed);

        this.distance = RobotConfig.get(name + ".distance", distance);

        this.error = RobotConfig.get(name + ".error", error) / 12;

        this.minDoneCycles = RobotConfig.get(name + ".minDoneCycles", minDoneCycles);

        PID = new PIDController(P, I, D, F, Robot.driveTrain.getAverageEncoderPIDSource(),
                        Robot.driveTrain.getDrivePIDOutput(true, false, false));
        
//        SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//        SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//        SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        PID.setP(SmartDashboard.getNumber("P", P));
//        PID.setI(SmartDashboard.getNumber("I", I));
//        PID.setD(SmartDashboard.getNumber("D", D));

        Log.i(this, "Driving " + distance + " feet at a speed of " + speed);
        
        Robot.driveTrain.reset();

        Robot.driveTrain.brakeMode(true);

        // Set output speed range
        if (speed > 0) {
            PID.setOutputRange(-speed, speed);
        } else {
            PID.setOutputRange(speed, -speed);
        }

        Robot.driveTrain.initGyro = 0;

        PID.setSetpoint(distance);


        // Will accept within 5 inch of target
        PID.setAbsoluteTolerance(error);

        // Start going to location
        PID.enable();
    }

    private int doneTicks;

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        if (PID.onTarget())
            doneTicks++;
        else
            doneTicks = 0;

        return doneTicks >= minDoneCycles;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Disable PID output and stop robot to be safe
        PID.disable();
        Robot.driveTrain.drive(0, 0);

        Log.i(this, "Done driving, drove " + Robot.driveTrain.getDistance());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
