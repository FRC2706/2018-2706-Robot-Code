package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartRightSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the centre position.
 * 
 * Places the cube in the right switch, grabs another, and places it in the right scale
 */
public class CenterStartRightSwitchMultiCube extends CommandGroup {

    public CenterStartRightSwitchMultiCube() {
        // Place first cube in switch
        this.addSequential(new CenterStartRightSwitch());

        // Lower lift and back away
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE, 2, 0.2));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, -3.25,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, this + ".back"), 2);

        // Face the centre pile
        this.addSequential(new RotateDriveWithGyro(-40, this + ".turnToPile"), 2);

        // Move forward and intake until cube detected
        IntakeUntilGrabbed grabCommand = new IntakeUntilGrabbed(AutoConstants.SPEED_SLOW, 0.8, 0.4);
        this.addSequential(grabCommand, 5);

        // Hold cube and move back to in front of switch
        this.addParallel(new IntakeCube(1, true), 2);
        this.addSequential(new StraightDriveFromCommand(AutoConstants.SPEED_FAST, grabCommand,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".backFromWall"), 2);

        // Raise arm and turn to face the switch
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT, 2, 0.2), 2);
        this.addSequential(new RotateDriveWithGyro(40, this + ".turnForSecondCube"), 2);

        // Drive into switch
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 3.25,
                        AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES,
                        this + ".forwardToSwitch2"), 2);

        // Eject cube into switch
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);

    }
}
