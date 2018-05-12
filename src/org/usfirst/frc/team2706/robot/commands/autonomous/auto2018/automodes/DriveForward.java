package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in any position.
 * 
 * Drives past the auto line (somewhat consistent :/)
 */
public class DriveForward extends CommandGroup {

    public DriveForward() {
        // Put the lift down
        this.addSequential(new InitLift());
        
        // Drive past the auto line
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, 12, AutoConstants.ACCURATE_ERROR, AutoConstants.LENIENT_CYCLES, this + ".testForward"));
    }
}
