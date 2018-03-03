package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitchNoCurve extends CommandGroup {
    
    public CenterStartRightSwitchNoCurve() {
        this.addSequential(new StraightDriveWithEncoders(0.8, 0.96, 2, 3, "luhhl"),2);
        this.addSequential(new RotateDriveWithGyro(0.6, -36, 1, 2, "lful"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 8.1, 2, 3, "lqAhWFGul"),5);
        this.addSequential(new RotateDriveWithGyro(0.6, 36, 1, 3, "lfugl"),2);
        this.addParallel(new MoveLiftDown(),1.0);
        this.addSequential(new StraightDriveWithEncoders(0.6, 1.325, 3, 3, "lqAWFwefGul"),1);

     //   this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.5),0.5);
    }
   
}
