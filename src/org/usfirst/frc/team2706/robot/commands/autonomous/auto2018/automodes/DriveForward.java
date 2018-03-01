package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveForward extends CommandGroup {

    public DriveForward() {
        this.addSequential(new MoveLiftUp(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.8, 12, 1, 5, "TestFoAAFWrward"));
    }
}
