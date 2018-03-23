package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScale extends CommandGroup {
    
    public RightStartRightScale() {
        this.addParallel(new SetLiftHeightBlocking(4,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,18.795 /*1/2robotlength*/,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".startForwardToScale"), 8);
        
        //this.addSequential(new StraightDriveWithEncoders(0.6,8.795 /*1/2robotlength*/,2,3, this + ".startForwardToScale"));
        this.addSequential(new RotateDriveWithGyro(-36, this + ".turnRightTowardsScale"),2);
       // this.addSequential(new MoveLiftUp(), 3.5);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.2),3);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,3.8,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
      
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),0.8);
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,-2.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
    }
    
}