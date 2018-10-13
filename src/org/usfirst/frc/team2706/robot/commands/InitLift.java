package org.usfirst.frc.team2706.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Raises the lift so it will fall at the start of auto
 */
public class InitLift extends CommandGroup {

    /**
     * Initializes the lift
     */
    public InitLift() {
        this.addSequential(new SetLiftHeightBlocking(0.5, 5, 0.1), 0.25);
    }

}
