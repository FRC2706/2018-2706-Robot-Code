package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the centre position.
 * 
 * Places the cube in the left switch, grabs another,
 * and places it in the left scale
 */
public class CenterStartLeftSwitchMultiCube extends CommandGroup {

    public CenterStartLeftSwitchMultiCube() {
        // Place first cube in switch
        this.addSequential(new CenterStartLeftSwitch());

        // Lower lift and back away
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, -43.0 / 12.0, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, this + ".back"), 2);
        
        // Face the centre pile
        this.addSequential(new RotateDriveWithGyro(45, this + ".turnToPile"), 2);
        
        // Move forward and intake until cube detected
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(AutoConstants.SPEED_CONTROLLED,0.4,0.8);
        this.addSequential(g,5);
        
        // Hold cube and move back to in front of switch
        this.addParallel(new IntakeCube(1,true),2);
        this.addSequential(new StraightDriveFromCommand(AutoConstants.SPEED_FAST, g, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, this + ".backFromWall"),2);
        
        // Raise arm and turn to face the switch
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new RotateDriveWithGyro(-45, this + ".turnForSecondCube"),2);
        
        // Drive into switch
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 45 / 12.0, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch2"), 2);
        
        // Eject cube into switch
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);

    }
}
