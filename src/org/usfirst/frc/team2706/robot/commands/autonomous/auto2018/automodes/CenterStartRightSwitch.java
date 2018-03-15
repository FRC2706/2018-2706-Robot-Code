package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitch extends CommandGroup {
    
    public CenterStartRightSwitch() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.8, 1.02, 2, 2, this + ".awayFromWall"), 1);
        this.addSequential(new RotateDriveWithGyro(0.6, 38.5, 1.5, 3, this + ".faceSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 5, 2, 2, this + ".driveToSwitch"), 5);
        this.addSequential(new RotateDriveWithGyro(0.6, -38.5, 1.5, 3, this + ".turnInFrontOfSwitch"), 2);
        this.addParallel(new SetLiftHeightBlocking(2.5,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(0.6, 2.5, 1.5, 2, this + ".forwardToSwitch"), 2);

        // this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.8), 0.8);
    }
   
}
