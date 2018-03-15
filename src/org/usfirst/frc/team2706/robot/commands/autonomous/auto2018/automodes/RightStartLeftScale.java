package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartLeftScale extends CommandGroup {
    public RightStartLeftScale() {
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST, 229 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".forwardToSwitch"));
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateToSwitch"));
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_FAST,195 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch"));
        this.addSequential(new RotateDriveWithGyro(90, this + ".rotateToSwitch2"));
        this.addParallel(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,37 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".pressSwitch2"), 2);
        this.addSequential(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT,2,0.2));
        
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
    }
}
