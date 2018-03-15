package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeight;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitchMultiCube extends CommandGroup {

    public CenterStartRightSwitchMultiCube() {
        this.addParallel(new InitLift(), 1);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 1.02, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".awayFromWall"), 1);
        this.addSequential(new RotateDriveWithGyro(38.5, this + ".faceSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 5, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveToSwitch"), 5);
        this.addSequential(new RotateDriveWithGyro(-38.5, this + ".turnInFrontOfSwitch"), 2);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 2.5, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 2);

        // this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);

        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, -3.3, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".back"), 2);
        this.addParallel(new MoveLift(-0.3),0.25);
        this.addSequential(new RotateDriveWithGyro(-44, this + ".turnToPile"), 2);
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(AutoConstants.SPEED_SLOW,0.6);
        this.addSequential(g,3);
        //this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
       // this.addParallel(new IntakeCube(0.6), 2);
        this.addSequential(new StraightDriveFromCommand(AutoConstants.SPEED_FAST, g, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".backFromWall"),2);
        this.addSequential(new RotateDriveWithGyro(44, this + ".turnForSecondCube"), 2);
        this.addParallel(new SetLiftHeight(AutoConstants.SWITCH_HEIGHT));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,3, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch2"), 2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);

    }
}
