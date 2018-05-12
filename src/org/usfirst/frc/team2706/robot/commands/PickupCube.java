package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.vision.FollowCamera;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Follows the cube using vision
 */
public class PickupCube extends CommandGroup {

    /**
     * Follows the cube using vision
     */
    public PickupCube() {
        this.addParallel(new FollowCamera());
//        this.addSequential(new IntakeCube(1, false));
    }
}
