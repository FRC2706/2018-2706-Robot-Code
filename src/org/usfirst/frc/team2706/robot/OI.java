package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.EjectCubeTimed;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.StartCimbing;
import org.usfirst.frc.team2706.robot.controls.TriggerButtonJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
// Operator Interface
public class OI {

    // Joystick for driving the robot around
    private final Joystick driverStick;

    // Joystick for controlling the mechanisms of the robot
    private final Joystick controlStick;

    public Joystick getDriverJoystick() {
        return driverStick;
    }

    public Joystick getOperatorJoystick() {
        return controlStick;
    }

    /**
     * Initializes Oi using the two default real joysticks
     */
    public OI() {
        this(new Joystick(0), new Joystick(1));
    }

    /**
     * Initializes Oi with non-default joysticks
     * 
     * @param driverStick The driver joystick to use
     * @param controlStick The operator joystick to use
     */
    public OI(Joystick driverStick, Joystick controlStick) {

        // Joystick for driving the robot around
        this.driverStick = driverStick;

        // Runs the code depending which button/trigger is pressed
        TriggerButtonJoystick driverBackLeftTrigger = new TriggerButtonJoystick(driverStick, 2);
        driverBackLeftTrigger.runWhileHeld(new EjectCube());
        
        TriggerButtonJoystick driverBackRightTrigger = new TriggerButtonJoystick(driverStick, 3);
        driverBackRightTrigger.runWhileHeld(new IntakeCube());
        
        EJoystickButton joybutten = new EJoystickButton(driverStick, 3);
        joybutten.runWhileHeld(new StartCimbing());
        
        EJoystickButton ejectTimed = new EJoystickButton(controlStick, 6);
        ejectTimed.runWhileHeld(new EjectCubeTimed());

        // The Joystick for controlling the mechanisms of the robot
        this.controlStick = controlStick;

        removeUnplugWarning();
    }

    /**
     * Makes the DriverStation not spam the console
     */
    private void removeUnplugWarning() {
        try {
            Field f = DriverStation.getInstance().getClass().getDeclaredField("m_nextMessageTime");
            f.setAccessible(true);
            f.set(DriverStation.getInstance(), Double.POSITIVE_INFINITY);
            f.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            Log.e("Oi", "Error occured trying to keep programmers sane", e);
        }
    }
    
    /**
     * Removes ButtonSchedulers that run commands that were added in Oi
     */
    public void destroy() {
        try {
            Field f = Scheduler.getInstance().getClass().getDeclaredField("m_buttons");
            f.setAccessible(true);
            f.set(Scheduler.getInstance(), null);
            f.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            Log.e("Oi", "Error occured destroying m_buttons", e);
        }
    }
}

