package org.usfirst.frc.team2706.robot.commands;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Intakes a cube into the intake
 */
public class IntakeCube extends LoggedCommand {

    private final Supplier<Double> speed;

    private boolean sameRatio;

    /**
     * Intake cube using a joystick axis
     * 
     * @param stick The joystick to use
     * @param axis The axis to use
     * @param sameRatio Whether to use the optimal ratio for intaking free cubes or not
     */
    public IntakeCube(Joystick stick, int axis, boolean sameRatio) {
        this(() -> stick.getRawAxis(axis), sameRatio);

    }

    /**
     * Intakes a cube at a constant speed
     * 
     * @param speed The the speed
     * @param sameRatio Whether to use the optimal ratio for intaking free cubes or not
     */
    public IntakeCube(double speed, boolean sameRatio) {
        this(() -> speed, sameRatio);
    }

    /**
     * Intakes a cube at a supplied speed
     * 
     * @param speed The supplier for the speed
     * @param sameRatio Whether to use the optimal ratio for intaking free cubes or not
     */
    public IntakeCube(Supplier<Double> speed, boolean sameRatio) {
        this.speed = speed;
        this.sameRatio = sameRatio;

        // FIXME: Sometimes stops auto modes from running correctly
        // this.requires(Robot.intake);
    }

    /**
     * Run the motors at the given speed
     */
    @Override
    public void execute() {
        if (sameRatio) {
            Robot.intake.inhaleCubeStatic(speed.get());
        } else {
            Robot.intake.inhaleCube(speed.get());
        }

    }

    /**
     * Stops both intake motors
     */
    @Override
    public void end() {
        Robot.intake.stopMotors();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
