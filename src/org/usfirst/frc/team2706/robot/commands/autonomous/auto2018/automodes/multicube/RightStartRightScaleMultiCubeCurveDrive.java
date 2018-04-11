package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveTwoSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScaleMultiCubeCurveDrive extends CommandGroup {

    public RightStartRightScaleMultiCubeCurveDrive() {
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 5, 0.2));
        this.addSequential(new CurveDriveTwoSpeed(2,19.75,25,0.95,0.85,0.7,10, 14,true,"B"))                                   ;
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),1);
        
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.1, 750),5);
        
        this.addSequential(new RotateDriveWithGyro(0.35, -130, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8,2.0,1.0,1,"a"));
        this.addSequential(new PickupCubeAuto(0.6,0.4), 5);
        this.addSequential(new IntakeCube(1,false),0.25);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-5.4,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new RotateDriveWithGyro(0.35, 120, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),1);
        
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.2, 750),5);
        this.addSequential(new RotateDriveWithGyro(0.35, -105, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8,2.0,1.0,1,"a"));
        this.addSequential(new PickupCubeAuto(0.6,0.4), 5);
        this.addSequential(new IntakeCube(1,false),0.25);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-5.4,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        
        this.addSequential(new RotateDriveWithGyro(0.35, 130, 2, 3, this + ".turnRightTowardsScale"),2);
      //  this.addSequential(new EjectCube(0.9),1);
    }
}
