package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDController;

/**
 * Have the robot drive certain distance, and switches the maximum speed after reaching a certain
 * distance
 */
public class StraightDriveWithEncoders2Speed extends LoggedCommand {

    private final double speed1;

    private final double speed2;

    private final double switchPoint;

    private final double distance;

    private final double error;

    private PIDController pid;

    private final int minDoneCycles;

    private final double P = RobotMap.STRAIGHT_DRIVE_P, I = RobotMap.STRAIGHT_DRIVE_I,
                    D = RobotMap.STRAIGHT_DRIVE_D, F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed1 The initial speed in range [-1,1]
     * @param speed2 The second speed in range [-1,1]
     * @param switchPoint The distance to switch to the second speed at
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in
     * @param name The name of the of the configuration properties to look for
     */
    public StraightDriveWithEncoders2Speed(double speed1, double speed2, double switchPoint,
                    double distance, double error, int minDoneCycles, String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed1 = RobotConfig.get(name + ".speed1", speed1);
        this.speed2 = RobotConfig.get(name + ".speed2", speed2);
        this.switchPoint = RobotConfig.get(name + ".switchPoint", switchPoint);
        this.distance = RobotConfig.get(name + ".distance", distance);

        this.error = RobotConfig.get(name + ".error", error) / 12;

        this.minDoneCycles = RobotConfig.get(name + ".minDoneCycles", minDoneCycles);

        pid = new PIDController(P, I, D, F, Robot.driveTrain.getAverageEncoderPIDSource(),
                        Robot.driveTrain.getDrivePIDOutput(true, false, false));

//        SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//        SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//        SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//         pid.setP(SmartDashboard.getNumber("P", P));
//         pid.setI(SmartDashboard.getNumber("I", I));
//         pid.setD(SmartDashboard.getNumber("D", D));

        Log.i(this, "Driving " + distance + " feet at a speed of " + speed1);

        Robot.driveTrain.reset();

        Robot.driveTrain.brakeMode(true);

        // Set output speed range
        if (speed1 > 0) {
            pid.setOutputRange(-speed1, speed1);
        } else {
            pid.setOutputRange(speed1, -speed1);
        }

        Robot.driveTrain.initGyro = 0;

        pid.setSetpoint(distance);


        // Will accept within 5 inch of target
        pid.setAbsoluteTolerance(error);

        // Start going to location
        pid.enable();
    }

    private int doneTicks;
    private boolean first = false;

    protected void execute() {
        if (Robot.driveTrain.getDistance() > switchPoint && !first) {
            first = true;
            if (speed2 > 0) {
                pid.setOutputRange(-speed2, speed2);
            } else {
                pid.setOutputRange(speed2, -speed2);
            }
        }
    }

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
        first = false;
        
        // Disable PID output and stop robot to be safe
        pid.disable();
        Robot.driveTrain.drive(0, 0);

        Log.i(this, "Done driving, drove " + Robot.driveTrain.getDistance());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
