package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightSwitch extends CommandGroup {
    
    public RightStartRightSwitch() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 142 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 7);
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateToSwitch"),3);
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,48 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveToSwitch"),1);
     
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),0.8);
    }
    
}