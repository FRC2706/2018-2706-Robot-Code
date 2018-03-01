package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartExchangeCube extends CommandGroup {
    public CenterStartExchangeCube() {
        this.addSequential(new MoveLiftUp(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.5, 46 / 12.0, 0.1, 3, "DriveFawfaawforward"),2);
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3, "RoawfawftateLeft"),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,62 / 12.0,0.1,3, "DssriveLeft"),2);
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3,"RotawfawfateLeftAgain"),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,46 / 12.0,0.1,3, "DriveBacawfawfk"),2);
        this.addSequential(new EjectCube(0.6),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,-10,0.1,3,"fff"));
    }
}
