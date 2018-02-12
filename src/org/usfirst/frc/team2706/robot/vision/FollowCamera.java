package org.usfirst.frc.team2706.robot.vision;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot in a straight line towards the target found by the camera. Used for lining up
 * the peg at short distances
 */
public class FollowCamera extends Command {

    /**
     * Drive at a specific speed based on camera
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in inches
     */
    public FollowCamera() {
        requires(Robot.driveTrain);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
   
    protected void execute() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
        double input = table.getEntry("ctrX").getDouble(0);
        double rotateVal = (input < 0 ? -1 : 1) * Math.sqrt(Math.abs(input));
        System.out.println(rotateVal);
        Robot.driveTrain.arcadeDrive(0.0, -rotateVal);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Robot.camera.enableRingLight(false);
        Robot.driveTrain.brakeMode(false);

        // Disable PID output and stop robot to be safe
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
