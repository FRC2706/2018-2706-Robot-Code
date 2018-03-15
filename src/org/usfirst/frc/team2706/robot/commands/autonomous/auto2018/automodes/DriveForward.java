package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveForward extends CommandGroup {

    public DriveForward() {
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.7, 12, 1, 5, this + ".testForward"));
    }
}
