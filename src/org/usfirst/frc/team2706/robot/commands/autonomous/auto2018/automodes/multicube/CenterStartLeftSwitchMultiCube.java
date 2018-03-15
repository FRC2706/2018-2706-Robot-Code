package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitchMultiCube extends CommandGroup {

    public CenterStartLeftSwitchMultiCube() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.8, 0.8, 1, 1, this + ".awayFromWall"),1);
        this.addSequential(new RotateDriveWithGyro(0.6, -36, 2.5, 3, this + ".faceSwitch"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 8.1, 1, 1, this + ".forwardToSwitch"),5);
        this.addSequential(new RotateDriveWithGyro(0.6, 36, 2.5, 3, this + ".rotateToSwitch"),2);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(0.6, 2, 1, 1, this + ".hitSwitch"),2);

     //   this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.8),0.8);

        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(0.7, -43.0 / 12.0, 1, 1, "back"), 2);
        this.addParallel(new MoveLift(-0.3),0.25);
        this.addSequential(new RotateDriveWithGyro(0.6, 45, 2.5, 3, "turnToPile"), 2);
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(0.55,0.6);
        this.addSequential(g,3);
        //this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
        this.addSequential(new StraightDriveFromCommand(0.75, g, 1, 1, "backFromWall"),2);
        this.addSequential(new RotateDriveWithGyro(0.6, -45, 2.5, 3, "turnForSecondCube"),2);
        this.addParallel(new SetLiftHeightBlocking(2.5,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(0.7, 45 / 12.0, 2, 3, this + ".forwardToSwitch2"), 2);
        this.addSequential(new EjectCube(0.8), 0.8);

    }
}
