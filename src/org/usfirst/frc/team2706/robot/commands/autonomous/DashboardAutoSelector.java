package org.usfirst.frc.team2706.robot.commands.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterAutoLeftSwitch;

import com.google.gson.Gson;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardAutoSelector {
    Command fallbackCommand;

    Priority[] leftPriorities = {new Priority("left_switch", "Left Position", true, true,
                    new CenterAutoLeftSwitch())};
    Priority[] centerPriorities  = {new Priority("left_switch", "Center Position", true, true,
                    new CenterAutoLeftSwitch())};
    Priority[] rightPriorities  = {new Priority("left_switch", "Right Position", true, true,
                    new CenterAutoLeftSwitch())};
    String position = "";

    public DashboardAutoSelector(Command fallbackCommand) {
        this.fallbackCommand = fallbackCommand;
    }

    public void getPositionAndRespond() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        position = table.getEntry("autonomous/selected_position").getString("");
        System.out.println("position" + "," + position);
        if (position != "") {
            if (position.equals("R")) {
                SmartDashboard.putString("autonomous/auto_modes",
                                new Gson().toJson(((UnselectedPriorityList) new ArrayList<Priority>(
                                                Arrays.asList(rightPriorities))).listToMap()));
            } else if (position.equals("C")) {
                SmartDashboard.putString("autonomous/auto_modes",
                                new Gson().toJson(((UnselectedPriorityList) new ArrayList<Priority>(
                                                Arrays.asList(centerPriorities))).listToMap()));
            } else if (position.equals("L")) {
                SmartDashboard.putString("autonomous/auto_modes",
                                new Gson().toJson(((UnselectedPriorityList) new ArrayList<Priority>(
                                                Arrays.asList(leftPriorities))).listToMap()));
            }
        }

    }

    @SuppressWarnings("unchecked")
    public Priority[] getPriorityList() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        String priorities = table.getEntry("autonomous/selected_modes").getString("");
        System.out.println("priorities" + priorities);
        if (priorities != "") {
            ArrayList<String> priorityList = new Gson().fromJson(priorities, new ArrayList<String>().getClass());
            ArrayList<Priority> objectPriorityList = new ArrayList<Priority>();
            for (String priority : priorityList) {
                if(position.equals("L")) {
                    objectPriorityList.add(((UnselectedPriorityList) new ArrayList<Priority>(Arrays.asList(leftPriorities))).findPriority(priority));
                }
                else if(position.equals("C")) {
                    objectPriorityList.add(((UnselectedPriorityList) new ArrayList<Priority>(Arrays.asList(centerPriorities))).findPriority(priority));
                }
                else if(position.equals("R")) {
                    objectPriorityList.add(((UnselectedPriorityList) new ArrayList<Priority>(Arrays.asList(rightPriorities))).findPriority(priority));
                }
            }
        }
        return null;
    }

    public Command chooseCommandFromPriorityList(Priority[] priorityList) {
        for (Priority priority : priorityList) {
            if (priority.getPossible())
                return priority.getCommand();
        }
        return fallbackCommand;
    }

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
         * @param id
         * @param isSwitch
         * @param isLeft
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
         * The priority will always work on first try.
         * 
         * @param id
         */
        public Priority(String id, String name, Command linkedCommand) {
            this.id = id;
            this.name = name;
            this.linkedCommand = linkedCommand;
            guarenteedPriority = true;
        }

        public boolean getPossible() {
            if (guarenteedPriority)
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
    class UnselectedPriorityList extends ArrayList<Priority> {
        private static final long serialVersionUID = 1L;

        public LinkedHashMap<String, String> listToMap() {
            LinkedHashMap<String, String> stringList = new LinkedHashMap<String, String>();
            for (Priority p : this) {
                stringList.put(p.id, p.name);
            }
            return stringList;
        }

        public Priority findPriority(String id) {
            for (Priority p : this) {
                if (p.id.equals(id)) {
                    return p;
                }
            }
            return null;
        }
    }
}
