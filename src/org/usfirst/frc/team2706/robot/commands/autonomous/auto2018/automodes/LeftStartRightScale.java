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
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 223 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"), 8);
        this.addSequential(new RotateDriveWithGyro(90,this + ".rotateToSwitch"), 3);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,180 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch"), 10);
        this.addSequential(new RotateDriveWithGyro(-135, this + ".rotateToSwitch2"), 3);
        this.addParallel(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,30 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch2"),5);
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.2), 5);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
}
