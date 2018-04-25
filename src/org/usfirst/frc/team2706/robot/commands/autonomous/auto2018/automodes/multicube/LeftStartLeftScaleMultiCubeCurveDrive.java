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

public class LeftStartLeftScaleMultiCubeCurveDrive extends CommandGroup {

    public LeftStartLeftScaleMultiCubeCurveDrive() {
        this.addParallel(new SetLiftHeightBlocking(1,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(1.0,0.3,0.1,1,"aabab"),0.5);
        this.addSequential(new StraightDriveWithEncoders(0.0,0.25,0.1,1,"awfawf"),0.15);
        
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 5, 0.2));
        this.addSequential(new CurveDriveTwoSpeed(2.5,19,25,0.95,0.9,0.7,10, 14,false,"B"));
        this.addSequential(new EjectCube(0.8),0.5);
//        
//        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.1, 750),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -2,1,2,"aff"));
//        this.addSequential(new RotateDriveWithGyro(0.35, 143, 2, 3, this + ".turnRightTowardsScale"),5);
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.1, 10),5);
        this.addSequential(new RotateDriveWithGyro(0.45, 120, 2, 3, this + ".turnRightTowardsScale"),3);
        
        this.addSequential(new StraightDriveWithEncoders(0.8,1.7,1.0,1,"a"));
     //   IntakeUntilGrabbed g = new IntakeUntilGrabbed(AutoConstants.SPEED_SLOW,1);
     //   this.addSequential(g);
        this.addSequential(new PickupCubeAuto(0.3,0.9, 0.25), 5);
        this.addSequential(new IntakeCube(1,false),0.25);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-5.3,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new RotateDriveWithGyro(0.45, -100, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 1.5,1,2,"aff"));
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),0.5);
        
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.2, 750),5);
        this.addSequential(new RotateDriveWithGyro(0.35, 100, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new StraightDriveWithEncoders(0.8,2.0,1.0,1,"a"));
        this.addSequential(new PickupCubeAuto(0.3,0.9), 5);
        this.addSequential(new IntakeCube(1,false),0.25);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-5.4,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        
        this.addSequential(new RotateDriveWithGyro(0.35, -100, 2, 3, this + ".turnRightTowardsScale"),2);
      //  this.addSequential(new EjectCube(0.9),1);
    }
}
