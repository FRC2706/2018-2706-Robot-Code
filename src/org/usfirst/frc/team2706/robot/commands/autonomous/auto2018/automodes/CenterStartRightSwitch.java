package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartRightSwitch extends CommandGroup {
    
    public CenterStartRightSwitch() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 1.02, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".awayFromWall"), 1);
        this.addSequential(new RotateDriveWithGyro(38.5, this + ".faceSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 5, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveToSwitch"), 5);
        this.addSequential(new RotateDriveWithGyro(-38.5, this + ".turnInFrontOfSwitch"), 2);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 2.5, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 2);

        // this.addSequential(new StraightDriveWithEncoders(0.5, 1.325, 1, 3, "lTYUul"),1);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED), 0.8);
    }
   
}
