package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftScaleMultiCubeCurveDrive extends CommandGroup {

    public LeftStartLeftScaleMultiCubeCurveDrive() {
        this.addParallel(new InitLift());
        this.addParallel(new SetLiftHeightBlockingAfterTime(AutoConstants.SCALE_HEIGHT, 5, 0.2,1000));
        this.addSequential(new CurveDrive(2,19.5,25,0.75,false,"B"));
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),1.5);
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.2, 500),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-2.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
        this.addSequential(new RotateDriveWithGyro(125, this + ".turnRightTowardsScale"),2);
        //this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,4.5,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new PickupCubeAuto(0.2,0.9), 5);
        this.addParallel(new IntakeCube(1,false),1);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-1,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addParallel(new SetLiftHeightBlocking(3,2,0.1),4);
        this.addSequential(new RotateDriveWithGyro(-125, this + ".turnRightTowardsScale"),2);
      //  this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.5),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,4.4,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        
        // this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"),2);
    //    this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
}
