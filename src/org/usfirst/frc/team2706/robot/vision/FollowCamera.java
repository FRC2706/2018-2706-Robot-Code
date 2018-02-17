package org.usfirst.frc.team2706.robot.vision;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drives the robot in a straight line towards the target found by the camera. Used for lining up
 * the peg at short distances
 */
public class FollowCamera extends Command {

    private final PIDController rotatePID;
    private final double P = 1, I = 0, D = 0;
    
    /**
     * Drive at a specific speed based on camera
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in inches
     */
    public FollowCamera() {
        requires(Robot.driveTrain);
        
        rotatePID = new PIDController(P, I, D, new CameraPID(), Robot.driveTrain.getDrivePIDOutput(false, true, false));
        
        SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
        SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
        SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }

    private class CameraPID implements PIDSource {

        private PIDSourceType pidSource = PIDSourceType.kDisplacement;
        
        private final NetworkTableEntry ctrX;
        
        {
            NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
            ctrX = table.getEntry("ctrX");
        }
        
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            this.pidSource = pidSource;
            
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return pidSource;
        }

        @Override
        public double pidGet() {
            return -ctrX.getDouble(0);
        }
        
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        rotatePID.setP(SmartDashboard.getNumber("P", P));
        rotatePID.setI(SmartDashboard.getNumber("I", I));
        rotatePID.setD(SmartDashboard.getNumber("D", D));
        
        rotatePID.setOutputRange(-0.5, 0.5);
        rotatePID.setSetpoint(0);
        rotatePID.enable();
    }
   
    protected void execute() {
//        NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
//        double input = table.getEntry("ctrX").getDouble(0);
//        double rotateVal = (input < 0 ? -1 : 1) * Math.sqrt(Math.abs(input));
//        System.out.println(rotateVal);
//        Robot.driveTrain.arcadeDrive(0.0, -rotateVal);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        rotatePID.disable();
        
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
