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

public class CenterStartLeftSwitchMultiCube extends CommandGroup {

    public CenterStartLeftSwitchMultiCube() {
        this.addSequential(new CenterStartLeftSwitch());

        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, -43.0 / 12.0, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, "back"), 2);
        this.addSequential(new RotateDriveWithGyro(45, "turnToPile"), 2);
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(AutoConstants.SPEED_CONTROLLED,1);
        this.addSequential(g,6);
        //this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
        this.addParallel(new IntakeCube(1,true),2);
        this.addSequential(new StraightDriveFromCommand(AutoConstants.SPEED_FAST, g, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, "backFromWall"),2);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new RotateDriveWithGyro(-45, "turnForSecondCube"),2);
        
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 45 / 12.0, AutoConstants.LENIENT_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch2"), 2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);

    }
}
