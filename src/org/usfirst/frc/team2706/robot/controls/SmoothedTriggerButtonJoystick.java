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
    public SmoothedTriggerButtonJoystick(Joystick joystick, int axis, double deadzone, double alpha) {
        super(joystick, axis);

        this.joystick = joystick;
        this.axis = axis;
        this.deadzone = deadzone;
        this.alpha = alpha;
    }

    @Override
    public boolean get() {
        
        double unsmoothed = joystick.getRawAxis(axis);
        double smoothed = alpha * unsmoothed + (1 - alpha) * last;
        last = smoothed;
        
        if (smoothed < deadzone && smoothed > -deadzone) {
            return false;
        }
        return true;
    }
    
    public double getAxis() {
        System.out.println(last);
        return last;
    }
}
