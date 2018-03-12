package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartExchangeCube extends CommandGroup {
    public CenterStartExchangeCube() {
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.5, 46 / 12.0, 0.1, 3, this + ".driveForward"),2);
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3, this + ".rotateLeft"),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,62 / 12.0,0.1,3, this + ".driveLeft"),2);
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3, this + ".rotateLeftAgain"),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,46 / 12.0,0.1,3, this + ".driveBack"),2);
        this.addSequential(new EjectCube(0.6),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,-10,0.1,3, this + ".backup"));
    }
}
