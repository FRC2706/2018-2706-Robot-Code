package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.EjectCubeWithIR;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLift;
import org.usfirst.frc.team2706.robot.commands.MoveLiftWithPID;
import org.usfirst.frc.team2706.robot.commands.PickupCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightUntilCancelled;
import org.usfirst.frc.team2706.robot.commands.SpinCubeInIntake;
import org.usfirst.frc.team2706.robot.commands.StartCimbing;
import org.usfirst.frc.team2706.robot.controls.StickQuadrantButtonJoystick;
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
        
        // Intake commands
        TriggerButtonJoystick intakeCube = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_BACK_LEFT_TRIGGER);
        intakeCube.runWhileHeld(new IntakeCube(controlStick, JoystickMap.XBOX_BACK_LEFT_TRIGGER, true));

        TriggerButtonJoystick ejectCube = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER);
        ejectCube.runWhileHeld(new EjectCube(controlStick, JoystickMap.XBOX_BACK_RIGHT_TRIGGER));

        EJoystickButton holdCube = new EJoystickButton(controlStick, JoystickMap.XBOX_LB_BUTTON);
        holdCube.runWhileHeld(new IntakeCube(1, false));

        EJoystickButton ejectSmooth = new EJoystickButton(controlStick, JoystickMap.XBOX_RB_BUTTON);
        ejectSmooth.runWhileHeld(new EjectCubeWithIR());

        EJoystickButton ejectTimed = new EJoystickButton(driverStick, JoystickMap.XBOX_RB_BUTTON);
        ejectTimed.runWhileHeld(new PickupCube());

        EJoystickButton spinCube = new EJoystickButton(controlStick, JoystickMap.XBOX_START_BUTTON);
        spinCube.runWhileHeld(new SpinCubeInIntake());
        
        
        // Climber command
        EJoystickButton climber = new EJoystickButton(controlStick, JoystickMap.XBOX_X_BUTTON);
        climber.runWhileHeld(new StartCimbing());


        // Lift manual control
        TriggerButtonJoystick moveLift = new TriggerButtonJoystick(controlStick, JoystickMap.XBOX_LEFT_AXIS_Y, 0.2);
        moveLift.runWhileHeld(new MoveLiftWithPID(controlStick, JoystickMap.XBOX_LEFT_AXIS_Y, true));

        
        // Lift manual override
        EJoystickButton moveLiftUp = new EJoystickButton(controlStick, JoystickMap.XBOX_Y_BUTTON);
        moveLiftUp.runWhileHeld(new MoveLift(0.6));

        EJoystickButton moveLiftDown = new EJoystickButton(controlStick, JoystickMap.XBOX_A_BUTTON);
        moveLiftDown.runWhileHeld(new MoveLift(-0.35));


        // Sending lift to fixed destinations
        StickQuadrantButtonJoystick liftLevelRight = new StickQuadrantButtonJoystick(controlStick,
                        JoystickMap.XBOX_RIGHT_AXIS_X, JoystickMap.XBOX_RIGHT_AXIS_Y,
                        StickQuadrantButtonJoystick.RIGHT, 0.2);
        liftLevelRight.runWhileHeld(new SetLiftHeightUntilCancelled(6));


        StickQuadrantButtonJoystick liftLevelUp = new StickQuadrantButtonJoystick(controlStick,
                        JoystickMap.XBOX_RIGHT_AXIS_X, JoystickMap.XBOX_RIGHT_AXIS_Y,
                        StickQuadrantButtonJoystick.DOWN, 0.2);
        liftLevelUp.runWhileHeld(new SetLiftHeightUntilCancelled(5.62));


        StickQuadrantButtonJoystick liftLevelLeft = new StickQuadrantButtonJoystick(controlStick,
                        JoystickMap.XBOX_RIGHT_AXIS_X, JoystickMap.XBOX_RIGHT_AXIS_Y,
                        StickQuadrantButtonJoystick.LEFT, 0.2);
        liftLevelLeft.runWhileHeld(new SetLiftHeightUntilCancelled(Lift.MAX_HEIGHT * 0.5));


        StickQuadrantButtonJoystick liftLevelDown = new StickQuadrantButtonJoystick(controlStick,
                        JoystickMap.XBOX_RIGHT_AXIS_X, JoystickMap.XBOX_RIGHT_AXIS_Y,
                        StickQuadrantButtonJoystick.UP, 0.2);
        liftLevelDown.runWhileHeld(new SetLiftHeightUntilCancelled(0));


        // Don't spam console with unplugged warnings
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

