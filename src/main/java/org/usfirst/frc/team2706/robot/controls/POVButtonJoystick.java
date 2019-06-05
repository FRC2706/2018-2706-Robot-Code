package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Button that activates when a POV is pressed
 */
public class POVButtonJoystick extends EJoystickButton {

    private Joystick joystick;
    private int POV;

    /**
     * Creates a the button for a POV on a joystick
     * 
     * @param joystick The joystick the POV is on
     * @param POV The POV as a button
     */
    public POVButtonJoystick(Joystick joystick, int POV) {
        super(joystick, POV);

        this.joystick = joystick;
        this.POV = POV;
    }

    @Override
    public boolean get() {
        return joystick.getPOV() == POV;
    }
}
