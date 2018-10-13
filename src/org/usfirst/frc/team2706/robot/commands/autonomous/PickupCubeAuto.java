package org.usfirst.frc.team2706.robot.commands.autonomous;

import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbedNoDrive;
import org.usfirst.frc.team2706.robot.vision.FollowCamera;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Picks up a cube while driving forward following vision
 */
public class PickupCubeAuto extends CommandGroup {

    private final IntakeUntilGrabbedNoDrive grab;

    /**
     * Intakes with specified speeds driving 0.3 forward
     * 
     * @param leftSpeed The left intake speed
     * @param rightSpeed The right intake speed
     */
    public PickupCubeAuto(double leftSpeed, double rightSpeed) {
        this(leftSpeed, rightSpeed, 0.3);
    }

    /**
     * Intakes with specified speeds driving at a specified speed forward
     * 
     * @param leftSpeed The left intake speed
     * @param rightSpeed The right intake speed
     */
    public PickupCubeAuto(double leftSpeed, double rightSpeed, double speed) {
        this.addParallel(new FollowCamera(speed));
        grab = new IntakeUntilGrabbedNoDrive(leftSpeed, rightSpeed);
        this.addSequential(grab);
    }

    @Override
    protected boolean isFinished() {
        return grab.isCompleted();
    }
}
