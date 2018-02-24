package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Button that activates when a trigger is pressed
 */
public class TriggerButtonJoystick extends EJoystickButton {

    private Joystick joystick;
    private int axis;

    /**
     * Creates a the button for an axis on a joystick
     * 
     * @param joystick The joystick the axis is on
     * @param axis The axis to act as a button
     */
    public TriggerButtonJoystick(Joystick joystick, int axis) {
        super(joystick, axis);

        this.joystick = joystick;
        this.axis = axis;
    }

    @Override
    public boolean get() {
        if (joystick.getRawAxis(axis) < 0.01 && joystick.getRawAxis(axis) > -0.01) {
            return false;
        }
        return true;
    }
}
