package org.usfirst.frc.team2706.robot.commands.autonomous;

import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Priority object for NetworkTable communications and seeing if it can run
 *
 */
public class Priority {

    public static final boolean IS_SWITCH = true;
    public static final boolean IS_SCALE = true;
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    private final String id;
    private final String name;
    private final boolean guaranteedPriority;
    private final boolean location;
    private final boolean isSwitch;
    private final boolean isScale;
    private final Command linkedCommand;

    /**
     * Allows the priority to be checked to see if it is possible.
     * 
     * @param id Priority's ID
     * @param name Display name of the priority
     * @param location A switch-based command
     * @param side Goes to the left side of the switch/scale
     * @param linkedCommand Command that the priority runs
     */
    public Priority(String id, String name, boolean isSwitch, boolean isScale, boolean location,
                    Command linkedCommand) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.isSwitch = isSwitch;
        this.isScale = isScale;
        this.linkedCommand = linkedCommand;
        guaranteedPriority = false;
    }

    /**
     * In this constructor, the priority has no starting restrictions.
     * 
     * @param id Priority's ID
     * @param name Display name of the priority
     * @param linkedCommand Command that the priority runs
     */
    public Priority(String id, String name, Command linkedCommand) {
        this.id = id;
        this.name = name;
        this.location = false;
        this.isSwitch = false;
        this.isScale = false;
        this.linkedCommand = linkedCommand;
        guaranteedPriority = true;
    }

    /**
     * Non-guaranteed no text priority
     */
    public Priority(Command linkedCommand, boolean location, boolean isSwitch, boolean isScale) {
        id = null;
        name = null;
        this.location = location;
        this.isSwitch = isSwitch;
        this.isScale = isScale;
        this.linkedCommand = linkedCommand;
        guaranteedPriority = false;
    }

    /**
     * No feature priority
     */
    public Priority(Command linkedCommand) {
        id = null;
        name = null;
        this.location = false;
        this.isSwitch = false;
        this.isScale = false;
        this.linkedCommand = linkedCommand;
        guaranteedPriority = true;
    }

    /**
     * Determines if it is possible for this priority to run. Assumes FMS actually gives a value
     * 
     * @return Whether the command can run
     */
    public boolean getPossible() {
        if (guaranteedPriority
                        || DriverStation.getInstance().getGameSpecificMessage().length() < 2) {
            return true;
        }

        boolean switchLeft = DriverStation.getInstance().getGameSpecificMessage()
                        .toCharArray()[0] == 'L';
        boolean scaleLeft = DriverStation.getInstance().getGameSpecificMessage()
                        .toCharArray()[1] == 'L';

        // You only care whether the switch/scale is on your side if you require it
        // Ensure that you have or don't need both the switch and the scale
        return (isSwitch != IS_SWITCH || location == switchLeft)
                        && (isScale != IS_SCALE || location == scaleLeft);
    }

    /**
     * Gets the id of the priority, its identification string.
     * 
     * @return The identification tag for this priority, such as "left_scale_multi_cube"
     */
    public String getID() {
        return id;
    }

    /**
     * Gets the user friendly name for this priority
     * 
     * @return The user friendly name for this autonomous priority, such as "Left scale multi cube"
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the command linked with this priority
     * 
     * @return The linked command
     */
    public Command getCommand() {
        return linkedCommand;
    }

    /**
     * Finds the first command in the priority list that can actually be ran, and returns it
     * 
     * @param priorityList The priority list
     * @return The chosen command
     */
    public static Command chooseCommandFromPriorityList(Priority[] priorityList) {
        if (priorityList == null) {
            return null;
        }
        for (Priority priority : priorityList) {
            if (priority == null) {
                return null;
            }
            if (priority.getPossible()) {
                return priority.getCommand();
            }
        }
        return new ArcadeDriveWithJoystick();
    }
}

