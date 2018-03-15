package org.usfirst.frc.team2706.robot.commands.autonomous.core;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotConfig;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This mostly works, but use the QuickRotate command instead. PID control using gyro heading is
 * slower and needs tuning of P,I,D parameters.
 * 
 * Note that the gyro heading is now absolute and not relative, so angle is a target heading and not
 * a relative turn angle.
 */
public class RotateDriveWithGyro extends Command {

    private final double speed;

    private final double angle;

    private PIDController PID;

    private final int minDoneCycles;

    private final double P = 0.022, I = 0.0, D = 0.01, F = 0;
    
    private final double error;

    public RotateDriveWithGyro(double speed, double angle, int minDonecycles, String name) {
        this(speed, angle, 1, minDonecycles, name);
    }
    public RotateDriveWithGyro(double angle, String name) {
        this(AutoConstants.ROTATE_SPEED, angle, AutoConstants.ROTATE_ERROR, AutoConstants.ROTATE_CYCLES,name);
    }
    
    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param angle The angle to rotate to
     * @param name The name of the of the configuration properties to look for
     */
    public RotateDriveWithGyro(double speed, double angle, double error, int minDonecycles, String name) {
        super(name);
        requires(Robot.driveTrain);

        this.speed = RobotConfig.get(name + ".speed", speed);

        this.angle = RobotConfig.get(name + ".angle", angle);

        this.minDoneCycles = RobotConfig.get(name + ".minDoneCycles", minDonecycles);

        this.error = RobotConfig.get(name + ".error", error);
        
        PID = new PIDController(P, I, D, F, Robot.driveTrain.getGyroPIDSource(false),
                        Robot.driveTrain.getDrivePIDOutput(false, false, true));
        
//      SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//      SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//      SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
  }

  // Called just before this Command runs the first time
  protected void initialize() {
//      PID.setP(SmartDashboard.getNumber("P", P));
//      PID.setI(SmartDashboard.getNumber("I", I));
//      PID.setD(SmartDashboard.getNumber("D", D));
      
        Log.d(this, "Rotating " + angle + " degrees at a speed of " + speed);
        
        Robot.driveTrain.reset();

        PID.setInputRange(-360.0, 360.0);

        // Make input infinite
        PID.setContinuous();
        if (speed > 0) {
            // Set output speed range
            PID.setOutputRange(-speed, speed);
        } else {
            PID.setOutputRange(speed, -speed);
        }
        
        // Set the tolerance in degrees
        PID.setAbsoluteTolerance(error);

        PID.setSetpoint(angle);

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
        
        Robot.driveTrain.reset();
        Log.d(this, "Done rotating");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
