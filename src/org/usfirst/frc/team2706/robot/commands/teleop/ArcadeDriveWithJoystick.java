package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.JoystickMap;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive arcade style using the Xbox Joystick until interrupted.
 */
public class ArcadeDriveWithJoystick extends Command {

    protected Joystick joystick;

    /**
     * Arcade drives with the driver joystick
     */
    public ArcadeDriveWithJoystick() {
        this(Robot.oi.getDriverJoystick());
    }

    /**
     * Arcade drives with a given joystick
     * 
     * @param joystick The joystick to drive with
     */
    public ArcadeDriveWithJoystick(Joystick joystick) {
        requires(Robot.driveTrain);

        this.joystick = joystick;
    }

    /**
     * Sets the joystick to be used to arcade drive with
     * 
     * @param joystick The joystick to drive with
     */
    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //Robot.driveTrain.drive(joystick);
            double turn = joystick.getRawAxis(JoystickMap.XBOX_RIGHT_AXIS_X);
            double speed =joystick.getRawAxis(JoystickMap.XBOX_LEFT_AXIS_Y);
            Robot.driveTrain.curvatureDrive(-speed, (turn > -0.05 && turn < 0.05) ? 0 : turn, (speed > -0.25 && speed < 0.25));
            //Robot.driveTrain.setVoltageDrive(false);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false; // Runs until interrupted
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
