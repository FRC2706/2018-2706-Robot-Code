package org.usfirst.frc.team2706.robot.commands.autonomous;

import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Priority object for NetworkTable communications and seeing if it can run
 *
 */
public class Priority {
    
    public static final boolean SWITCH = true;
    public static final boolean SCALE = false;
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    String id;
    String name;
    boolean guaranteedPriority;
    boolean location;
    boolean side;
    Command linkedCommand;

    /**
     * Allows the priority to be checked to see if it is possible.
     * 
     * @param id Priority's ID
     * @param name Display name of the priority
     * @param location A switch-based command
     * @param side Goes to the left side of the switch/scale
     * @param linkedCommand Command that the priority runs
     */
    public Priority(String id, String name, boolean location, boolean side,
                    Command linkedCommand) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.side = side;
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
        this.linkedCommand = linkedCommand;
        guaranteedPriority = true;
    }

    /**
     * Non-guaranteed no text priority
     */
    public Priority(Command linkedCommand, boolean location, boolean side) {
        this.location = location;
        this.side = side;
        this.linkedCommand = linkedCommand;
        guaranteedPriority = false;
    }

    /**
     * No feature priority
     */
    public Priority(Command linkedCommand) {
        this.linkedCommand = linkedCommand;
        guaranteedPriority = true;
    }

    /**
     * Determines if it is possible for the priority to run. Assumes FMS actually gives a value
     * 
     * @return
     */
    public boolean getPossible() {
        if (guaranteedPriority || DriverStation.getInstance().getGameSpecificMessage().equals(""))
            return true;
        if (location) {
            if (side && DriverStation.getInstance().getGameSpecificMessage()
                            .toCharArray()[0] == 'L')
                return true;
            return false;
        } else {
            if (side && DriverStation.getInstance().getGameSpecificMessage()
                            .toCharArray()[1] == 'L')
                return true;
            return false;
        }
    }

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
        if(priorityList == null) {
            return null;
        }
        for (Priority priority : priorityList) {
            if (priority.getPossible()) {
                return priority.getCommand();
            }
        }
        return new ArcadeDriveWithJoystick();
    }
}

