package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScaleMultiCubeCurveDrive extends CommandGroup {

    public RightStartRightScaleMultiCubeCurveDrive() {
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 2, 0.2));
        this.addSequential(new CurveDrive(2,22,15,0.8,true,1,"B"));
        this.addSequential(new EjectCube(0.6),0.5);
        
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,-2.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
        
        this.addParallel(new MoveLift(-0.3),0.25);
        this.addSequential(new RotateDriveWithGyro(-100, this + ".turnRightTowardsScale"),3);
        this.addParallel(new IntakeCube(0.75, true),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,4.5,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.1),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-4.5,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new RotateDriveWithGyro(100, this + ".turnRightTowardsScale"),3);
        // this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"),2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
}
