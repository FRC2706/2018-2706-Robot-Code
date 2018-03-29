package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScaleRightSwitchMultiCube extends CommandGroup {
    
    public RightStartRightScaleRightSwitchMultiCube() {
        this.addParallel(new InitLift());
        this.addParallel(new SetLiftHeightBlockingAfterTime(AutoConstants.SCALE_HEIGHT, 5, 0.2,1000));
        this.addSequential(new CurveDrive(2,19,25,0.8,true,"B"));
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),1);
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.2, 1000),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-2.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
        this.addSequential(new RotateDriveWithGyro(-125, this + ".turnRightTowardsScale"),2);
        //this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,4.5,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new PickupCubeAuto(0.9,0.2), 5);
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.1),2);
       // this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"),2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
    
}