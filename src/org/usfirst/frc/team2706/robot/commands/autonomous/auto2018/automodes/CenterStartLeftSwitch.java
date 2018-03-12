package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
       // CurveDrive c = new CurveDrive(76 / 12.0, 94 / 12.0, 0, 0.6, true, 0.25, "CurveToSfawfawfwitch");
        //this.addSequential(c);
        this.addSequential(new StraightDriveWithEncoders(0.8, 0.96, 2, 3, this + ".awayFromWall"),2);
        this.addSequential(new RotateDriveWithGyro(0.6, -36, 1, 2, this + ".faceSwitch"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8, 8.1, 2, 3, this + ".forwardToSwitch"),5);
        this.addSequential(new RotateDriveWithGyro(0.6, 36, 1, 3, this + ".rotateToSwitch"),2);
        this.addParallel(new MoveLiftUp(),1.0);
        this.addSequential(new StraightDriveWithEncoders(0.6, 1.325, 3, 3, this + ".hitSwitch"),1);

     //   this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(0.5),0.5);
    }
    
}