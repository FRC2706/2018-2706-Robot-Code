package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.PickupCubeAuto;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders2Speed;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartRightScale extends CommandGroup{

    public LeftStartRightScale() {
        this.addParallel(new SetLiftHeightBlocking(0.7,5,0.15),0.5);
        this.addSequential(new StraightDriveWithEncoders2Speed(1.0,0.75,140/12, 216 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 100);
        this.addSequential(new RotateDriveWithGyro(0.45,90, AutoConstants.ROTATE_ERROR, AutoConstants.ROTATE_CYCLES,this + ".rotateToSwitch"), 2);
        this.addParallel(new SetLiftHeightBlockingAfterTime(6.9,2,0.4,2300), 6);
        this.addSequential(new StraightDriveWithEncoders2Speed(1.0,0.75, 135 / 12, 240/ 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch"), 10);
      //  this.addParallel(new SetLiftHeightBlocking(3,2,0.2), 5);
        
        this.addSequential(new RotateDriveWithGyro(0.55, -115, 2, 3, this + ".turnRightTowardsScale"),3);
        //this.addSequential(new IntakeCube(0.1, true),0.5);
        
        this.addSequential(new StraightDriveWithEncoders(0.6,46.5 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch2"),5);
        
        this.addSequential(new EjectCube(0.8),0.25);
      //  this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-1.5,AutoConstants.ACCURATE_ERROR, 1, this + ".endForwardToScale"),2);
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,2,0.2,500),5);
      //  this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-1.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
        this.addSequential(new RotateDriveWithGyro(0.45, -135, 2, 3, this + ".turnRightTowardsScale"),3);
        this.addSequential(new StraightDriveWithEncoders(0.9,2.0,1.0,1,"a"),2);
        this.addParallel(new MoveLift(-0.3),0.5);
        this.addSequential(new PickupCubeAuto(0.9,0.3), 5);
        this.addSequential(new IntakeCube(1,false),0.25);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),5);
        this.addSequential(new StraightDriveWithEncoders(0.7,-5.2,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        
        this.addSequential(new RotateDriveWithGyro(0.45, 120, 2, 3, this + ".turnRightTowardsScale"),2);
        this.addSequential(new EjectCube(0.8),0.5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-1.5,AutoConstants.ACCURATE_ERROR, 1, this + ".endForwardToScale"),2);
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,5,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-1.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
    }
}
