package org.usfirst.frc.team2706.robot.commands.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterAutoLeftSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.CenterAutoRightSwitch;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftAutoLeftScale;
import org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.LeftAutoLeftSwitch;

import com.google.gson.Gson;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardAutoSelector {
    Command fallbackCommand;

    Priority[] leftPriorities = {
                    new Priority("left_left_switch", "Left Switch", true, true,
                                    new LeftAutoLeftSwitch()),
                    new Priority("left_left_scale", "Left Scale", false, true,
                                    new LeftAutoLeftScale())};
    Priority[] centerPriorities = {
                    new Priority("center_left_switch", "Left Switch", true, true,
                                    new CenterAutoLeftSwitch()),
                    new Priority("center_right_switch", "Right Switch", true, false,
                                    new CenterAutoRightSwitch())};
    Priority[] rightPriorities = {
                    new Priority("right_right_switch", "Right Switch", true, false,
                                    new LeftAutoLeftSwitch()),
                    new Priority("right_right_scale", "Right Scale", false, false,
                                    new LeftAutoLeftScale())};
    String position = "";

    public DashboardAutoSelector(Command fallbackCommand) {
        this.fallbackCommand = fallbackCommand;
    }
    String prevPosition = "";
    public void getPositionAndRespond() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        position = table.getEntry("autonomous/selected_position").getString("c");
        System.out.println("pos " + position);
        if (!position.equals(prevPosition)) {
            //prevPosition = position;
            if (position.equals("r")) {
                SmartDashboard.putString("SmartDashboard/autonomous/auto_modes",
                                new Gson().toJson(listToMap(new ArrayList<Priority>(
                                                Arrays.asList(rightPriorities)))));
            } else if (position.equals("c")) {
                System.out.println("SmartDashboard/autonomous/auto_modes");
                SmartDashboard.putString("autonomous/auto_modes",
                                new Gson().toJson(listToMap(new ArrayList<Priority>(
                                                Arrays.asList(centerPriorities)))));
            } else if (position.equals("l")) {
                SmartDashboard.putString("SmartDashboard/autonomous/auto_modes",
                                new Gson().toJson(listToMap(new ArrayList<Priority>(
                                                Arrays.asList(leftPriorities)))));
            }
        }

    }

    @SuppressWarnings("unchecked")
    public Priority[] getPriorityList() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        String priorities = table.getEntry("autonomous/selected_modes").getString("");
        System.out.println("priorities" + priorities);
        if (priorities != "") {
            ArrayList<String> priorityList =
                            new Gson().fromJson(priorities, new ArrayList<String>().getClass());
            ArrayList<Priority> objectPriorityList = new ArrayList<Priority>();
            for (String priority : priorityList) {
                if (position.equals("L")) {
                    objectPriorityList.add(findPriority(priority, new ArrayList<Priority>(
                                    Arrays.asList(leftPriorities))));
                } else if (position.equals("C")) {
                    objectPriorityList.add(findPriority(priority, new ArrayList<Priority>(
                                    Arrays.asList(centerPriorities))));
                } else if (position.equals("R")) {
                    objectPriorityList.add(findPriority(priority, new ArrayList<Priority>(
                                    Arrays.asList(rightPriorities))));
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
