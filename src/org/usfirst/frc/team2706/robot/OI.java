package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.EjectCubeWithIR;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.MoveLiftWithPID;
import org.usfirst.frc.team2706.robot.commands.PickupCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightUntilCancelled;
import org.usfirst.frc.team2706.robot.commands.StartCimbing;
import org.usfirst.frc.team2706.robot.controls.POVButtonJoystick;
import org.usfirst.frc.team2706.robot.controls.TriggerButtonJoystick;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

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
        intakeCube.runWhileHeld(new IntakeCube(controlStick, JoystickMap.XBOX_BACK_LEFT_TRIGGER, true));
        
        TriggerButtonJoystick ejectCube = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER);
        ejectCube.runWhileHeld(new EjectCube(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER));
        
        EJoystickButton holdCube = new EJoystickButton(controlStick, JoystickMap.XBOX_LB_BUTTON);
        holdCube.runWhileHeld(new IntakeCube(1, false));
        
        EJoystickButton ejectSmooth = new EJoystickButton(controlStick, JoystickMap.XBOX_RB_BUTTON);
        ejectSmooth.runWhileHeld(new EjectCubeWithIR());
        
        EJoystickButton climber = new EJoystickButton(controlStick, JoystickMap.XBOX_X_BUTTON);
        climber.runWhileHeld(new StartCimbing());
        
        EJoystickButton ejectTimed = new EJoystickButton(driverStick, JoystickMap.XBOX_RB_BUTTON);
        ejectTimed.runWhileHeld(new PickupCube());
        
        // Currently lift is mapped to buttons as well
        // Final: Elevator on axis 1
        TriggerButtonJoystick MoveLift = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_LEFT_AXIS_Y, 0.25);
        MoveLift.runWhileHeld(new MoveLiftWithPID (controlStick, JoystickMap.XBOX_LEFT_AXIS_Y, true));
        
        EJoystickButton MoveLiftUp = new EJoystickButton(controlStick, JoystickMap.XBOX_Y_BUTTON);
        MoveLiftUp.runWhileHeld(new MoveLift(0.3));
        
        EJoystickButton MoveLiftDown = new EJoystickButton(controlStick, JoystickMap.XBOX_A_BUTTON);
        MoveLiftDown.runWhileHeld(new MoveLift(-0.3));

        // Sending lift to fixed destinations   
        POVButtonJoystick liftLevelRight = new POVButtonJoystick(controlStick, JoystickMap.XBOX_POV_RIGHT);
        liftLevelRight.runWhileHeld(new SetLiftHeightUntilCancelled(Lift.MAX_HEIGHT));
        
        POVButtonJoystick liftLevelUp = new POVButtonJoystick(controlStick, JoystickMap.XBOX_POV_UP);
        liftLevelUp.runWhileHeld(new SetLiftHeightUntilCancelled(Lift.MAX_HEIGHT * 0.75));
        
        POVButtonJoystick liftLevelLeft = new POVButtonJoystick(controlStick, JoystickMap.XBOX_POV_LEFT);
        liftLevelLeft.runWhileHeld(new SetLiftHeightUntilCancelled(Lift.MAX_HEIGHT * 0.5));
        
        POVButtonJoystick liftLevelDown = new POVButtonJoystick(controlStick, JoystickMap.XBOX_POV_DOWN);
        liftLevelDown.runWhileHeld(new SetLiftHeightUntilCancelled(0));

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

