package org.usfirst.frc.team2706.robot.vision;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Drives the robot in a straight line towards the target found by the camera. Used for lining up
 * the peg at short distances
 */
public class FollowCamera extends LoggedCommand {

    private final PIDController rotatePid;
    private final double P = 0.75, I = 0.0, D = 0.0;

    /**
     * Drive at the speed of an axis on the driver joystick using the camera to control rotation
     */
    public FollowCamera() {
        this(() -> -Robot.oi.getDriverJoystick().getRawAxis(JoystickMap.XBOX_LEFT_AXIS_Y));
    }

    /**
     * Drive at a constant speed using the camera to control rotation
     * 
     * @param speed The speed to drive forward at
     */
    public FollowCamera(double speed) {
        this(() -> speed);
    }

    /**
     * Drive at a supplied speed using the camera to control rotation
     * 
     * @param speed The supplied speed to drive forward at
     */
    public FollowCamera(Supplier<Double> forwardSpeed) {
        requires(Robot.driveTrain);

        rotatePid = new PIDController(P, I, D, new CameraPID(),
                        Robot.driveTrain.getDrivePIDOutput(false, true, forwardSpeed, false));

//         SmartDashboard.putNumber("P", SmartDashboard.getNumber("P", P));
//         SmartDashboard.putNumber("I", SmartDashboard.getNumber("I", I));
//         SmartDashboard.putNumber("D", SmartDashboard.getNumber("D", D));
    }


    private class CameraPID implements PIDSource {

        private PIDSourceType pidSource = PIDSourceType.kDisplacement;

        private final NetworkTableEntry ctrX;
        private final NetworkTableEntry numTargetsFound;

        {
            NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
            ctrX = table.getEntry("ctrX");
            numTargetsFound = table.getEntry("numTargetsFound");
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
            return numTargetsFound.getDouble(0) > 0 ? -ctrX.getDouble(0) : 0;
        }

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
//         rotatePid.setP(SmartDashboard.getNumber("P", P));
//         rotatePid.setI(SmartDashboard.getNumber("I", I));
//         rotatePid.setD(SmartDashboard.getNumber("D", D));

        rotatePid.setOutputRange(-0.5, 0.5);
        rotatePid.setSetpoint(0);
        rotatePid.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        rotatePid.disable();

        // Robot.camera.enableRingLight(false);
        Robot.driveTrain.brakeMode(false);

        // Disable PID output and stop robot to be safe
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
