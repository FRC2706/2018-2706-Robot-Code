package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.CheckLiftHeight;
import org.usfirst.frc.team2706.robot.commands.EjectCube;

import org.usfirst.frc.team2706.robot.commands.IntakeAndHold;

import org.usfirst.frc.team2706.robot.commands.EjectCubeTimed;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.MoveLiftToDestination;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.PickupCube;
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
        
        // The Joystick for controlling the mechanisms of the robot
        this.controlStick = controlStick;

        // Runs the code depending which button/trigger is pressed

        TriggerButtonJoystick intakeCube = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_BACK_LEFT_TRIGGER);
        intakeCube.runWhileHeld(new IntakeCube(controlStick, JoystickMap.XBOX_BACK_LEFT_TRIGGER));
        
        TriggerButtonJoystick ejectCube = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER);
        ejectCube.runWhileHeld(new EjectCube(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER));
        
        EJoystickButton holdCube = new EJoystickButton(controlStick, JoystickMap.XBOX_LB_BUTTON);
        holdCube.runWhileHeld(new IntakeAndHold(0.5));

        EJoystickButton cameraCube = new EJoystickButton(driverStick, 1);
        cameraCube.runWhileHeld(new PickupCube());
        
        EJoystickButton climber = new EJoystickButton(controlStick, JoystickMap.XBOX_X_BUTTON);
        climber.runWhileHeld(new StartCimbing());
        
        EJoystickButton ejectTimed = new EJoystickButton(controlStick, JoystickMap.XBOX_RB_BUTTON);
        ejectTimed.runWhileHeld(new EjectCubeTimed());
        
        // Currently lift is mapped to buttons as well
        // Final: Elevator on axis 1
        TriggerButtonJoystick MoveLift = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_LEFT_AXIS_Y);
        MoveLift.runWhileHeld(new MoveLift(controlStick, JoystickMap.XBOX_LEFT_AXIS_Y));
        
        EJoystickButton MoveLiftUp = new EJoystickButton(controlStick, JoystickMap.XBOX_Y_BUTTON);
        MoveLiftUp.runWhileHeld(new MoveLiftUp());
        
        EJoystickButton MoveLiftDown = new EJoystickButton(controlStick, JoystickMap.XBOX_A_BUTTON);
        MoveLiftDown.runWhileHeld(new MoveLiftDown());

        // Sending lift to fixed destinations
        EJoystickButton MoveLiftToDestination = new EJoystickButton(controlStick, JoystickMap.XBOX_POV_UP);
        MoveLiftToDestination.runWhileHeld(new MoveLiftToDestination(0));
        
        
        // For testing only, to be removed later
        EJoystickButton CheckLiftHeight = new EJoystickButton(controlStick, JoystickMap.XBOX_B_BUTTON);
        CheckLiftHeight.runWhileHeld(new CheckLiftHeight());

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

