package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 0.8, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".awayFromWall"),1);
        this.addSequential(new RotateDriveWithGyro(-36, this + ".faceSwitch"),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 8.1, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"),5);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new RotateDriveWithGyro(36, this + ".rotateToSwitch"),2);
        
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 2, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".hitSwitch"),2);

     //   this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),0.8);
    }
    
}