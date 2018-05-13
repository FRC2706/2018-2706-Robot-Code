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
    
    // Networktables key constants
    private static final String SMART_DASHBOARD_KEY  = "SmartDashboard", 
                    SELECTED_POSITION_KEY = "autonomous/selected_position", 
                    SELECTED_MODES_KEY = "autonomous/selected_modes",
                    AVAILABLE_MODES_KEY = "autonomous/auto_modes";

    Priority[] leftPriorities;
    Priority[] centerPriorities;
    Priority[] rightPriorities;
    String position = "";

    /**
     * Instantiate with sending values
     * 
     */
    public DashboardAutoSelector(Priority[] leftPriorities, Priority[] centerPriorities,
                    Priority[] rightPriorities) {
        this.leftPriorities = leftPriorities;
        this.centerPriorities = centerPriorities;
        this.rightPriorities = rightPriorities;
    }

    /**
     * Creates a table listener to see when the dashboard sends a position back to the user, and
     * responds
     */
    public void getPositionAndRespond() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable(SMART_DASHBOARD_KEY);
        position = table.getEntry(SELECTED_POSITION_KEY).getString("");
        
        // If the position isn't empty, respond to the setting of the position by sending the auto modes for it.
        if (!position.equals(""))
            valueChanged(null, "", null, NetworkTableValue.makeString(position), 0);
        
        // Add the entry listener
        table.addEntryListener(SELECTED_POSITION_KEY, this, EntryListenerFlags.kUpdate);

    }

    /**
     * When auto starts, gets the priority list from NetworkTables, and returns them in a nice
     * arraylist of objects
     * 
     * @return The priority list
     */
    @SuppressWarnings("unchecked")
    public Priority[] getPriorityList() {
        Priority[] priorityAutoModes;
        
        
        NetworkTable table = NetworkTableInstance.getDefault().getTable(SMART_DASHBOARD_KEY);
        String priorities = table.getEntry(SELECTED_MODES_KEY).getString("");
        if (!priorities.equals("")) {
            ArrayList<String> priorityList =
                            new Gson().fromJson(priorities, new ArrayList<String>().getClass());
            ArrayList<Priority> objectPriorityList = new ArrayList<Priority>();
            
            // Loop around the entire list of priorities and determine 
            for (String priority : priorityList) {
                switch (position) {
                    case "l" :
                        objectPriorityList.add(findPriority(priority, leftPriorities));
                        break;
                    case "c" :
                        objectPriorityList.add(findPriority(priority, centerPriorities));
                        break;
                    case "r":
                        objectPriorityList.add(findPriority(priority, rightPriorities));
                        break;
                }
            }
            priorityAutoModes = objectPriorityList.toArray(new Priority[0]);
        }
        // Otherwise, return null.
        else priorityAutoModes = null;
        
        return priorityAutoModes;
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
            SmartDashboard.putString(AVAILABLE_MODES_KEY, new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(rightPriorities)))));
        } else if (position.equals("c")) {
            SmartDashboard.putString(AVAILABLE_MODES_KEY, new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(centerPriorities)))));
        } else if (position.equals("l")) {
            SmartDashboard.putString(AVAILABLE_MODES_KEY, new Gson().toJson(
                            listToMap(new ArrayList<Priority>(Arrays.asList(leftPriorities)))));
        }
    }

    /**
     * Converts a list of priorities to a {@code Map<String, String>}
     * 
     * @param pp The list to convert
     * @return The new Map
     */
    public LinkedHashMap<String, String> listToMap(ArrayList<Priority> pp) {
        LinkedHashMap<String, String> stringList = new LinkedHashMap<String, String>();
        for (Priority p : pp) {
            stringList.put(p.getID(), p.getName());
        }
        return stringList;
    }

    /**
     * Searches for a priority within a list of priorities
     * 
     * @param id The id of the priority to search for
     * @param pp The list of priorities to search
     * @return The priority that was found, otherwise null
     */
    public Priority findPriority(String id, Priority[] pp) {
        for (Priority p : pp) {
            if (p.getID().equals(id)) {
                return p;
            }
        }
        return null;

    }

}
