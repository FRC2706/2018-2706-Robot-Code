package org.usfirst.frc.team2706.robot.commands.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Communicates with the dashboard to send priorities and get selections, eventually getting one
 * autonomous mode chosen after using FMS. Falls back on two autonomous selector switches this year
 *
 */
public class DashboardAutoSelector implements TableEntryListener {

    Priority[] leftPriorities;
    Priority[] centerPriorities;
    Priority[] rightPriorities;
    String position = "";

    /**
     * Instantiate with sending values
     * 
     */
    public DashboardAutoSelector(Priority[] leftPriorities, Priority[] centerPriorities,Priority[] rightPriorities) {
        this.leftPriorities = leftPriorities;
        this.centerPriorities = centerPriorities;
        this.rightPriorities = rightPriorities;
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
