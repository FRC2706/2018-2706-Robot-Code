package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartExchangeCube extends CommandGroup {
    public CenterStartExchangeCube() {
        //this.addSequential(new SetPosition(1));
        this.addSequential(new StraightDriveWithEncoders(0.5, 1, 0.1, 3, "DriveForward"));
        this.addSequential(new RotateDriveWithGyro(0.5,-90,3, "RotateLeft"));
        this.addSequential(new StraightDriveWithEncoders(0.5,1,0.1,3, "DriveLeft"));
        this.addSequential(new RotateDriveWithGyro(0.5,-90,3,"RotateLeftAgain"));
        this.addSequential(new StraightDriveWithEncoders(0.5,1,0.1,3, "DriveBack"));
        this.addSequential(new EjectCube(0.8),2);
    }
}
