package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartLeftScale extends CommandGroup {
    public RightStartLeftScale() {
        this.addSequential(new StraightDriveWithEncoders(0.3, 2, 1, 5, "AutoForwardFoot"));
    }
}
