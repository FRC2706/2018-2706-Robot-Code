package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.TalonStraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Stops CurveDrive right at where it ends using PID and rotation PID
 * @author Connor A
 *
 */
public class CurveDriveStop extends CommandGroup {

    /**
     * @param endCurve End Angle preferred to stop at.
     */
    public CurveDriveStop(double endCurve) {
        this.addSequential(new TalonStraightDriveWithEncoders(0.6, 0.0, 0.1, 10, "stop"));
//        this.addSequential(new RotateDriveWithGyro(0.5, -(Robot.driveTrain.getHeading() - endCurve),10,"rot"));
    }
}
