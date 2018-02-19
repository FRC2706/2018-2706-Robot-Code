package org.usfirst.frc.team2706.robot.commands.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterStartRightSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftStartLeftScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftStartLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartRightScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.RightStartRightSwitch;

import com.google.gson.Gson;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Communicates with the dashboard to send priorities and get selections, eventually getting one
 * autonomous mode chosen after using FMS. 
 * Falls back on two autonomous selector switches this year
 *
 */
public class DashboardAutoSelector implements TableEntryListener {
    Command fallbackCommand;

    Priority[] leftPriorities = {
                    new Priority("left_left_switch", "Left Switch", true, true,
                                    new LeftStartLeftSwitch()),
                    new Priority("left_left_scale", "Left Scale", false, true,
                                    new LeftStartLeftScale())};
    Priority[] centerPriorities = {
                    new Priority("center_left_switch", "Left Switch", true, true,
                                    new CenterStartLeftSwitch()),
                    new Priority("center_right_switch", "Right Switch", true, false,
                                    new CenterStartRightSwitch())};
    Priority[] rightPriorities = {
                    new Priority("right_right_switch", "Right Switch", true, false,
                                    new RightStartRightSwitch()),
                    new Priority("right_right_scale", "Right Scale", false, false,
                                    new RightStartRightScale())};
    String position = "";

    /**
     * Instantiate with any backup command that runs upon no other options
     * 
     * @param fallbackCommand Normally driveStraight or doNothing
     */
    public DashboardAutoSelector(Command fallbackCommand) {
        this.fallbackCommand = fallbackCommand;
    }

    /**
     * Creates a table listener to see when the dashboard sends a position back to the user, and
     * responds
     */
    public void getPositionAndRespond() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        position = table.getEntry("autonomous/selected_position").getString("");
        if (!position.equals(""))
            valueChanged(null, "", null, NetworkTableValue.makeString(position), 0);

        table.addEntryListener("autonomous/selected_position", this, EntryListenerFlags.kUpdate);

    }

    /**
     * When auto starts, gets the priority list from NetworkTables, and returns them in a nice
     * arraylist of objects
     * 
     * @return The priority list
     */
    @SuppressWarnings("unchecked")
    public Priority[] getPriorityList() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        String priorities = table.getEntry("autonomous/selected_modes").getString("");
        if (priorities != "") {
            ArrayList<String> priorityList =
                            new Gson().fromJson(priorities, new ArrayList<String>().getClass());
            ArrayList<Priority> objectPriorityList = new ArrayList<Priority>();
            for (String priority : priorityList) {
                System.out.println("firstpri" + priority);
                System.out.println(position);
                if (position.equals("l")) {
                    objectPriorityList.add(findPriority(priority,
                                    new ArrayList<Priority>(Arrays.asList(leftPriorities))));
                } else if (position.equals("c")) {
                    objectPriorityList.add(findPriority(priority,
                                    new ArrayList<Priority>(Arrays.asList(centerPriorities))));
                } else if (position.equals("r")) {
                    objectPriorityList.add(findPriority(priority,
                                    new ArrayList<Priority>(Arrays.asList(rightPriorities))));
                }
            }
            return objectPriorityList.toArray(new Priority[0]);
        }
        return null;
    }

    /**
     * Finds the first command in the priority list that can actually be ran, and returns it
     * 
     * @param priorityList The priority list
     * @return The chosen command
     */
    public Command chooseCommandFromPriorityList(Priority[] priorityList) {
        for (Priority priority : priorityList) {
            if (priority.getPossible()) {
                return priority.getCommand();
            }

        }
        return fallbackCommand;
    }

    /**
     * When the position value changes on the dashboard, send back a list of automodes from that
     * position
     */
    @Override
    public void valueChanged(NetworkTable table, String key, NetworkTableEntry entry,
                    NetworkTableValue value, int flags) {
        position = value.getString();
        if (position.equals("r")) {
            SmartDashboard.putString("autonomous/auto_modes", new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(rightPriorities)))));
        } else if (position.equals("c")) {
            SmartDashboard.putString("autonomous/auto_modes", new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(centerPriorities)))));
        } else if (position.equals("l")) {
            SmartDashboard.putString("autonomous/auto_modes", new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(leftPriorities)))));
        }
    }

    /**
     * Priority object for NetworkTable communications and seeing if it can run
     *
     */
    class Priority {
        String id;
        String name;
        boolean guarenteedPriority;
        boolean isSwitch;
        boolean isLeft;
        Command linkedCommand;

        /**
         * Allows the priority to be checked to see if it is possible.
         * 
         * @param id Priority's ID
         * @param name Display name of the priority
         * @param isSwitch A switch-based command
         * @param isLeft Goes to the left side of the switch/scale
         * @param linkedCommand Command that the priority runs
         */
        public Priority(String id, String name, boolean isSwitch, boolean isLeft,
                        Command linkedCommand) {
            this.id = id;
            this.name = name;
            this.isSwitch = isSwitch;
            this.isLeft = isLeft;
            this.linkedCommand = linkedCommand;
            guarenteedPriority = false;
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
            guarenteedPriority = true;
        }

        /**
         * Determines if it is possible for the priority to run. Assumes FMS actually gives a value
         * 
         * @return
         */
        public boolean getPossible() {
            if (guarenteedPriority
                            || DriverStation.getInstance().getGameSpecificMessage().equals(""))
                return true;
            if (isSwitch) {
                if (isLeft && DriverStation.getInstance().getGameSpecificMessage()
                                .toCharArray()[0] == 'L')
                    return true;
                return false;
            } else {
                if (isLeft && DriverStation.getInstance().getGameSpecificMessage()
                                .toCharArray()[1] == 'L')
                    return true;
                return false;
            }
        }

        public Command getCommand() {
            return linkedCommand;
        }
    }

    public LinkedHashMap<String, String> listToMap(ArrayList<Priority> pp) {
        LinkedHashMap<String, String> stringList = new LinkedHashMap<String, String>();
        for (Priority p : pp) {
            stringList.put(p.id, p.name);
        }
        return stringList;
    }

    public Priority findPriority(String id, ArrayList<Priority> pp) {
        for (Priority p : pp) {
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;

    }
    
}
