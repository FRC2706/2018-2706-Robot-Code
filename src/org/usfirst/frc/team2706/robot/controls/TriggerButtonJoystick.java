package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Button that activates when a trigger is pressed
 */
public class TriggerButtonJoystick extends EJoystickButton {

    private final Joystick joystick;
    private final int axis;
    private final double deadzone;

    /**
     * Creates a the button for an axis on a joystick
     * 
     * @param joystick The joystick the axis is on
     * @param axis The axis to act as a button
     */
    public TriggerButtonJoystick(Joystick joystick, int axis) {
        this(joystick, axis, 0.1);
    }
    
    /**
     * Creates a the button for an axis on a joystick
     * 
     * @param joystick The joystick the axis is on
     * @param axis The axis to act as a button
     * @param 
     */
    public TriggerButtonJoystick(Joystick joystick, int axis, double deadzone) {
        super(joystick, axis);

        this.joystick = joystick;
        this.axis = axis;
        this.deadzone = deadzone;
    }

    @Override
    public boolean get() {
        if (joystick.getRawAxis(axis) < deadzone && joystick.getRawAxis(axis) > -deadzone) {
            return false;
        }
        return true;
    }
}
