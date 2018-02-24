package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.vision.FollowCamera;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PickupCube extends CommandGroup {

    public PickupCube() {
        this.addParallel(new FollowCamera());
        this.addSequential(new IntakeCube(0.7));
    }
}
