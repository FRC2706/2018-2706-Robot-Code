package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartExchangeCube extends CommandGroup {
    public CenterStartExchangeCube() {
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 46 / 12.0, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveForward"),2);
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateLeft"),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,62 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveLeft"),2);
        this.addSequential(new RotateDriveWithGyro(-90, this + ".rotateLeftAgain"),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_SLOW,46 / 12.0,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".driveBack"),2);
        this.addSequential(new EjectCube(AutoConstants.EJECT_SPEED),2);
        this.addSequential(new RotateDriveWithGyro(-30, this + ".rotateLeft"),2);
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED,-12,AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".backup"));
    }
}
