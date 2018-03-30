package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartRightScale extends CommandGroup{

    public LeftStartRightScale() {
        this.addParallel(new SetLiftHeightBlocking(0.7,5,0.15),0.5);
        this.addSequential(new StraightDriveWithEncoders(0.8, 220 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 6);
        this.addSequential(new RotateDriveWithGyro(90,this + ".rotateToSwitch"), 2);
        this.addSequential(new StraightDriveWithEncoders(0.8,240/ 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch"), 10);
        this.addParallel(new SetLiftHeightBlocking(3,2,0.2), 5);
        this.addSequential(new RotateDriveWithGyro(-125, this + ".rotateToSwitch2"), 2);
        //this.addSequential(new IntakeCube(0.1, true),0.5);
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.2), 5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,53.5 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch2"),5);
        
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),1);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-1.5,AutoConstants.ACCURATE_ERROR, 1, this + ".endForwardToScale"),2);
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,5,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-1.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
    }
}
