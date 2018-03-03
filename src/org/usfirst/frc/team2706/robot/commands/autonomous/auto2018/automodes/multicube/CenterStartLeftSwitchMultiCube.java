package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitchMultiCube extends CommandGroup {

    public CenterStartLeftSwitchMultiCube() {
        this.addSequential(new StraightDriveWithEncoders(0.8, 0.96, 2, 3, "luhhl"), 2);
        this.addSequential(new RotateDriveWithGyro(0.6, -36, 1, 2, "lful"), 2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 8.1, 2, 3, "lqAhWFGul"), 5);
        this.addSequential(new RotateDriveWithGyro(0.6, 36, 1, 3, "lfugl"), 2);
        this.addParallel(new MoveLiftDown(), 1.0);
        this.addSequential(new StraightDriveWithEncoders(0.6, 1.325, 3, 3, "lqAWFwefGul"), 1);

        // this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.5), 0.5);

        this.addParallel(new MoveLiftUp(), 1.0);
        this.addSequential(new StraightDriveWithEncoders(0.8, -39.0 / 12.0, 2, 3, "Back"));
        this.addSequential(new RotateDriveWithGyro(0.6, 45, 1, 3, "FFF"), 2);
        this.addParallel(new IntakeCube(0.6), 4);
        this.addSequential(new StraightDriveWithEncoders(0.55, 3.0, 2, 3, "BBB"), 2.5);
        this.addSequential(new StraightDriveWithEncoders(0.55, -3.0, 2, 3, "LLL"));
        this.addSequential(new RotateDriveWithGyro(0.6, -45, 1, 3, "WWW"));
        this.addParallel(new MoveLiftDown(), 1.0);
        this.addSequential(new StraightDriveWithEncoders(0.7, 42 / 12.0, 2, 3, "UHHHHHHHHH"), 2);
        this.addSequential(new EjectCube(0.5), 0.5);

    }
}
