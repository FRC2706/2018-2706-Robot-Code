package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the left position.
 * 
 * Curve drives to the left scale, places the cube, grabs another, and places it in the left switch.
 */
public class LeftStartLeftScaleLeftSwitchMultiCube extends CommandGroup {

    public LeftStartLeftScaleLeftSwitchMultiCube() {
        // Initialize the lift
        this.addParallel(new InitLift());

        // Go to scale and raise the lift after 5 seconds
        this.addParallel(new SetLiftHeightBlockingAfterTime(AutoConstants.SCALE_HEIGHT, 5, 0.2, 1000));
        this.addSequential(new CurveDrive(2, 19, 25, 0.75, false, this + ".curveToScale"));

        // Place the cube in the scale
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 1);

        // Lower lift and drive backwards
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE, 5, 0.2, 1000), 5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -2.48,
                        AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".endForwardToScale"), 2);

        // Turn towards the switch
        this.addSequential(new RotateDriveWithGyro(125, this + ".turnRightTowardsSwitch"), 2);

        // Drive forward and pickup the cube
        this.addSequential(new PickupCubeAuto(0.2, 0.9), 5);

        // Raise the lift
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT, 2, 0.1), 2);

        // Put the cube in the switch
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 2);
    }

}
