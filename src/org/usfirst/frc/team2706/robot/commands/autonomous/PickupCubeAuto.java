package org.usfirst.frc.team2706.robot.commands.autonomous;

import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.IntakeUntilGrabbedNoDrive;
import org.usfirst.frc.team2706.robot.vision.FollowCamera;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PickupCubeAuto extends CommandGroup {
    
    private final IntakeUntilGrabbedNoDrive grab;
    public PickupCubeAuto(double leftSpeed, double rightSpeed) {
        this.addParallel(new FollowCamera(0.325));
        grab = new IntakeUntilGrabbedNoDrive(leftSpeed, rightSpeed);
        this.addSequential(grab);
    }
    
    public void end() {
    }
    @Override
    protected boolean isFinished() {
        return grab.isCompleted();
    }
}
