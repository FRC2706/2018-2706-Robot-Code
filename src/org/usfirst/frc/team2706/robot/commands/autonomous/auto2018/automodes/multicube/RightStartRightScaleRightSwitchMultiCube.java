package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartRightScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScaleRightSwitchMultiCube extends CommandGroup {
    
    public RightStartRightScaleRightSwitchMultiCube() {
        this.addSequential(new RightStartRightScale());
        this.addParallel(new SetLiftHeightBlocking(Double.MIN_VALUE,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,-2.48,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),2);
        
       // this.addParallel(new MoveLift(-0.3),0.25);
        this.addSequential(new RotateDriveWithGyro(-100, this + ".turnRightTowardsScale"), 3);
        this.addParallel(new IntakeCube(0.75, true),4);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,4.5,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".endForwardToScale"),3);
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SWITCH_HEIGHT,2,0.1),2);
       // this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"),2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
    
}