package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveTwoSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the left position.
 * 
 * Curve drives to the left scale, places the cube, grabs another, places it, and grabs one more
 * without placing it.
 */
public class LeftStartLeftScaleMultiCubeCurveDrive extends CommandGroup {

    public LeftStartLeftScaleMultiCubeCurveDrive() {
        // Move the lift up, then start driving and stop quickly to drop intake
        this.addParallel(new SetLiftHeightBlocking(1, 2, 0.2));
        this.addSequential(new StraightDriveWithEncoders(1.0, 0.3, 0.1, 1, this + ".stutterStart"), 0.5);
        this.addSequential(new StraightDriveWithEncoders(0.0, 0.25, 0.1, 1, this + ".stutterStop"), 0.15);

        // Raise the lift and drive to the scale
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 5, 0.2));
        this.addSequential(new CurveDriveTwoSpeed(2.5, 20, 25, 0.95, 0.9, 0.7, 10, 14, false, this + ".curveToScale"));

        // Place the cube in the scale
        this.addSequential(new EjectCube(0.66), 0.5);

        // Lower lift and back off of scale
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE, 5, 0.1, 500), 5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -2, 1, 2, this + ".backFromScale"));

        // Turn towards switch
        this.addSequential(new RotateDriveWithGyro(0.55, 125, 2, 3, this + ".turnRightTowardsSwitch"), 3);

        // Drive towards cube
        this.addSequential(new StraightDriveWithEncoders(0.8, 1.25, 1.0, 1, this + ".driveTowardsCube"));

        // Pickup the cube while driving forward until the cube is detected
        this.addSequential(new PickupCubeAuto(0.5, 0.9, 0.25), 5);

        // Intake the cube to ensure it was grabbed
        this.addSequential(new IntakeCube(1, false), 0.25);

        // Raise the lift and drive backwards to scale
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 2, 0.1), 4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -5.3,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".backToScale"), 3);

        // Turn towards scale
        this.addSequential(new RotateDriveWithGyro(0.45, -85, 2, 3, this + ".turnRightTowardsScale"), 2);

        // Drive towards the scale
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 3, 1, 2, this + ".forwardToScale"));

        // Place the cube in the scale
        this.addSequential(new EjectCube(0.95), 0.5);

        // Lower lift and turn towards switch
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE, 5, 0.2, 750), 5);
        this.addSequential(new RotateDriveWithGyro(0.35, 110, 2, 3, this + ".turnRightTowardsSwitch2"), 2);

        // Drive towards cube
        this.addSequential(new StraightDriveWithEncoders(0.8, 2.0, 1.0, 1, this + ".towardsScale"));

        // Pickup the cube while driving forward until the cube is detected
        this.addSequential(new PickupCubeAuto(0.5, 0.9), 5);

        // Intake the cube to ensure it was grabbed
        this.addSequential(new IntakeCube(1, false), 0.25);

        // Raise lift and drive towards scale
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 2, 0.1), 4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -5.4,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".endForwardToScale"), 3);

        // Face the scale
        this.addSequential(new RotateDriveWithGyro(0.35, -110, 2, 3, this + ".turnRightTowardsScale2"), 2);
    }
}
