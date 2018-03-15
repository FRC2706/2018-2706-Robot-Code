package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeight;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitchMultiCube extends CommandGroup {

    public CenterStartRightSwitchMultiCube() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.8, 1.02, 2, 2, this + ".awayFromWall"), 1);
        this.addSequential(new RotateDriveWithGyro(0.6, 38.5, 1.5, 3, this + ".faceSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 5, 2, 2, this + ".driveToSwitch"), 5);
        this.addSequential(new RotateDriveWithGyro(0.6, -38.5, 1.5, 3, this + ".turnInFrontOfSwitch"), 2);
        this.addParallel(new SetLiftHeightBlocking(2.5,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(0.6, 2.5, 1.5, 2, this + ".forwardToSwitch"), 2);

        // this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.8), 0.8);

        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(0.7, -3.3, 1, 1, this + ".back"), 2);
        this.addParallel(new MoveLift(-0.3),0.25);
        this.addSequential(new RotateDriveWithGyro(0.6, -44, 2.5, 3, this + ".turnToPile"), 2);
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(0.55,0.6);
        this.addSequential(g,3);
        //this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
       // this.addParallel(new IntakeCube(0.6), 2);
        this.addSequential(new StraightDriveFromCommand(0.75, g, 1, 1, this + ".backFromWall"),2);
        this.addSequential(new RotateDriveWithGyro(0.6, 44, 2.5, 3, this + ".turnForSecondCube"), 2);
        this.addParallel(new SetLiftHeight(2.5));
        this.addSequential(new StraightDriveWithEncoders(0.7,3, 2, 3, this + ".forwardToSwitch2"), 2);
        this.addSequential(new EjectCube(0.8), 0.8);

    }
}
