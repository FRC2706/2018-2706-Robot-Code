package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders2Speed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the right position.
 * 
 * Puts the cube in the left scale and picks up a second cube and puts it in the scale
 */
public class RightStartLeftScale extends CommandGroup {

    public RightStartLeftScale() {
        // Raises the lift and drives to alley
        this.addParallel(new SetLiftHeightBlocking(0.7, 5, 0.15), 0.5);
        this.addSequential(new StraightDriveWithEncoders2Speed(1.0, 0.75, 140 / 12, 216 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".forwardToAlley"), 100);

        // Turns down alley
        this.addSequential(new RotateDriveWithGyro(0.45, -90, AutoConstants.ROTATE_ERROR,
                        AutoConstants.ROTATE_CYCLES, this + ".rotateToAlley"), 2);

        // Raise the lift all the way and drive down the alley
        this.addParallel(new SetLiftHeightBlockingAfterTime(6.9, 2, 0.4, 2300), 6);
        this.addSequential(new StraightDriveWithEncoders2Speed(1.0, 0.75, 135 / 12, 240 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".driveDownAlley"), 10);

        // Face the scale
        this.addSequential(new RotateDriveWithGyro(0.55, 115, 2, 3, this + ".turnRightTowardsScale"), 3);

        // Drive towards scale
        this.addSequential(new StraightDriveWithEncoders(0.6, 46.5 / 12.0,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".forwardToScale"), 5);

        // Place cube in scale
        this.addSequential(new EjectCube(0.8), 0.25);

        // Lower lift and turn to switch
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE, 2, 0.2, 500), 5);
        this.addSequential(new RotateDriveWithGyro(0.45, 135, 2, 3, this + ".turnRightTowardsSwitch"), 3);

        // Drives towards the cube
        this.addSequential(new StraightDriveWithEncoders(0.9, 2.0, 1.0, 1, this + ".driveTowardsCube"), 2);

        // Ensure lift is down and drive forward until cube is detected
        this.addParallel(new MoveLift(-0.3), 0.5);
        this.addSequential(new PickupCubeAuto(0.5, 0.9), 5);

        // Ensure that the cube is grabbed
        this.addSequential(new IntakeCube(1, false), 0.25);

        // Raise lift and back into scale
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 2, 0.1), 5);
        this.addSequential(new StraightDriveWithEncoders(0.7, -5.2, AutoConstants.ACCURATE_ERROR,
                                        AutoConstants.LENIENT_CYCLES, this + ".backAwayFromScale"), 3);

        // Turn to face scale
        this.addSequential(new RotateDriveWithGyro(0.45, -120, 2, 3, this + ".turnRightTowardsScale"), 2);

        // Place cube in scale
        this.addSequential(new EjectCube(0.8), 0.5);

        // Back away from scale
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW, -1.5,
                        AutoConstants.ACCURATE_ERROR, 1, this + ".endForwardToScale"), 2);

        // Lower lift and back up towards next cube
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE, 5, 0.2), 5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -1.48,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".endForwardToScale"), 2);
    }
}
