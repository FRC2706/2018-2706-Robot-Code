package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.GenericHID;

public class StickQuadrantButtonJoystick extends EJoystickButton {

    public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    public final GenericHID joystick;
    public final int axis1;
    public final int axis2;
    public final int quadrant;
    public final double deadzone;

    public StickQuadrantButtonJoystick(GenericHID joystick, int axis1, int axis2, int quadrant,
                    double deadzone) {
        super(joystick, axis1);

        if (quadrant < 0 || quadrant > 3) {
            throw new IllegalArgumentException("Quadrant range is 0-3");
        }

        this.joystick = joystick;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.quadrant = quadrant;
        this.deadzone = deadzone;
    }

    @Override
    public boolean get() {
        double x = joystick.getRawAxis(axis1);
        double y = joystick.getRawAxis(axis2);

        if(Math.abs(x) <= deadzone && Math.abs(y) < deadzone) {
            return false;
        }
        
        double a, b;
        if (quadrant == 0) {
            a = x;
            b = y;
        } else if (quadrant == 2) {
            a = -x;
            b = -y;
        } else if (quadrant == 1) {
            a = -y;
            b = x;
        } else {
            a = y;
            b = -x;
        }


        if (a > 0) {
            return b > Math.abs(a);
        } else {
            return b >= Math.abs(a);
        }
    }
}
