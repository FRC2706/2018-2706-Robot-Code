package org.usfirst.frc.team2706.robot.controls.talon;

import org.usfirst.frc.team2706.robot.controls.LimitSwitch;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * A {@code WPI_TalonSRX} that cannot drive downwards when the limit switch is pressed
 */
// TODO: Allow for a top limit
public class TalonLimit extends WPI_TalonSRX {

    private final LimitSwitch liftDown;

    /**
     * Creates the limited Talon
     * 
     * @param deviceNumber The ID of the Talon to use
     * @param liftDown The limit switch to stop the motor
     */
    public TalonLimit(int deviceNumber, LimitSwitch liftDown) {
        super(deviceNumber);
        this.liftDown = liftDown;
    }

    @Override
    public void set(double value) {
        // Can move unless moving downwards when limit switch is hit
        if (!liftDown.get() || value > 0) {
            super.set(value);
        } else {
            super.set(0);
        }
    }

}
