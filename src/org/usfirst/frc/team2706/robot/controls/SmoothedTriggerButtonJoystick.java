package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Button that activates when a trigger is pressed
 */
public class SmoothedTriggerButtonJoystick extends EJoystickButton {

    private final Joystick joystick;
    private final int axis;
    private final double deadzone;
    private final double alpha;

    private double last = 0;

    /**
     * Creates a the button for an axis on a joystick
     * 
     * @param joystick The joystick the axis is on
     * @param axis The axis to act as a button
     */
    public SmoothedTriggerButtonJoystick(Joystick joystick, int axis, double alpha) {
        this(joystick, axis, 0.1, alpha);
    }

    /**
     * Creates a the button for an axis on a joystick
     * 
     * @param joystick The joystick the axis is on
     * @param axis The axis to act as a button
     * @param
     */
    public SmoothedTriggerButtonJoystick(Joystick joystick, int axis, double deadzone,
                    double alpha) {
        super(joystick, axis);

        this.joystick = joystick;
        this.axis = axis;
        this.deadzone = deadzone;
        this.alpha = alpha;
    }

    @Override
    public boolean get() {
        // Get the current axis value
        double unsmoothed = joystick.getRawAxis(axis);

        // Apply smoothing to the input
        double smoothed = alpha * unsmoothed + (1 - alpha) * last;

        // Save the smoothed input for next tick
        last = smoothed;

        // Check if the smoothed input is outside the deadzone
        return smoothed >= deadzone || smoothed <= -deadzone;
    }

    /**
     * Gets the smoothed axis input without changing how often the smoothing is applied
     * 
     * @return The last smoothed input
     */
    public double getAxis() {
        return last;
    }
}
