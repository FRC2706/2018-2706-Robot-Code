package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeight;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbed;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.StraightDriveFromCommand;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitchMultiCube extends CommandGroup {

    public CenterStartLeftSwitchMultiCube() {
        this.addSequential(new StraightDriveWithEncoders(0.8, 0.96, 1, 1, this + ".awayFromWall"),1);
        this.addSequential(new RotateDriveWithGyro(0.6, -36, 1, 2, this + ".faceSwitch"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 8.1, 1, 1, this + ".forwardToSwitch"),5);
        this.addSequential(new RotateDriveWithGyro(0.6, 36, 1, 2, this + ".rotateToSwitch"),2);
        this.addParallel(new SetLiftHeight(2.5),1.5);
        this.addSequential(new StraightDriveWithEncoders(0.6, 1.325, 1, 1, this + ".hitSwitch"),1.5);

     //   this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.5),0.5);

        this.addParallel(new SetLiftHeight(Double.MIN_VALUE));
        this.addSequential(new StraightDriveWithEncoders(0.7, -43.0 / 12.0, 1, 1, "Back"), 2);
        this.addSequential(new RotateDriveWithGyro(0.6, 45, 1, 1, "FFF"), 2);
        IntakeUntilGrabbed g = new IntakeUntilGrabbed(0.55,0.6);
        this.addSequential(g,3);
        //this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
        this.addSequential(new StraightDriveFromCommand(0.75, g, 1, 1, "LLL"),2);
        this.addSequential(new RotateDriveWithGyro(0.6, -45, 1, 1, "WWW"),2);
        this.addParallel(new SetLiftHeight(2.5));
        this.addSequential(new StraightDriveWithEncoders(0.7, 45 / 12.0, 2, 3, "UHHHHHHHHH"), 2);
        this.addSequential(new EjectCube(0.5), 0.5);

    }
}
