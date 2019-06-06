package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.autonomous.Priority;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Controls the 12 switch dial on the robot to select an autonomous mode. The autonomous modes on
 * each of the switches are defined in Robot.java
 */
public class AutonomousSelector extends SendableBase implements Sendable {

    /**
     * The number of different positions on the selector switch (including not connencted)
     */
    public static final int NUM_INDICES = 13;

    private static final Range[] voltages = {   new Range(0, 2.5), new Range(2.5, 2.75),
                    new Range(2.75, 3.1), new Range(3.1, 3.5), new Range(3.5, 3.75),
                    new Range(3.75, 3.95), new Range(3.95, 4.1), new Range(4.1, 4.2),
                    new Range(4.2, 4.3), new Range(4.3, 4.4), new Range(4.4, 4.5),
                    new Range(4.5, 4.6), new Range(4.6, 5)};

    private final Priority[][][] commands;
    public final AnalogInput selector1;
    public final AnalogInput selector2;

    private boolean isFree = true;

    /**
     * Creates AutoSelector with a list of commands to bind to each input
     * 
     * @param commands The commands to bind. The zeroth is default, one is the first notch
     */
    public AutonomousSelector(Priority[][]... commands) {
        this.commands = new Priority[NUM_INDICES][NUM_INDICES][8];
        this.selector1 = new AnalogInput(RobotMap.SELECTOR1_CHANNEL);
        this.selector2 = new AnalogInput(RobotMap.SELECTOR2_CHANNEL);

        isFree = false;
        setCommands(commands);
    }

    /**
     * Sets the priorities that the selectors have
     * 
     * @param commands The priorities that the selectors have
     */
    public void setCommands(Priority[][]... commands) {
        // Use the first selector as the first dimension index of the array
        for (int i = 0; i < NUM_INDICES; i++) {

            // Use the second selector as the second dimension index of the array
            for (int j = 0; j < NUM_INDICES; j++) {

                // Add command if it it was provided
                if (i < commands.length && j < commands[i].length) {
                    this.commands[i][j] = commands[i][j];
                } else {
                    this.commands[i][j] = null;
                }
            }
        }
    }

    /**
     * Gets the currently selected priority list
     * 
     * @return The selected priority list
     */
    public Priority[] getSelected() {
        // Get the index of the selectors
        int idx1 = getVoltageAsIndex(selector1);
        int idx2 = getVoltageAsIndex(selector2);

        // If the first dimension of the array is null default back to the 0th dimension
        if (commands[idx1] == null) {
            idx1 = 0;
        }
        // If the second dimension of the array is null default back to drive forward
        else if (commands[idx1][idx2] == null) {
            idx1 = 2;
            idx2 = 0;
        }
        // Ensure that the second index is not going to be less than 0
        if (idx2 == 0) {
            idx2 = 1;
        }

        // Return the selected priority list
        return commands[idx1][idx2 - 1];
    }

    /**
     * Gets the index of a selector switch
     * 
     * @param selector The selector switch to get the index of
     * @return The index of the selector
     */
    public int getVoltageAsIndex(AnalogInput selector) {
        // Don't try to get hardware data if the hardware was freed
        if (isFree) {
            return -1;
        }

        // Check each voltage range
        for (int i = 0; i < voltages.length; i++) {
            // Get the current voltage
            double voltage = selector.getAverageVoltage();

            // Check if the voltage is within the current voltage range
            if (voltage >= voltages[i].min && voltage < voltages[i].max) {
                // The selector is within this range
                return i;
            }
        }

        // Default to index of 0
        return 0;
    }

    /**
     * Delete (free) the analog switch used for the autonomous selector.
     */
    @Override
    public void free() {
        if (selector1 != null && !isFree) {
            selector1.free();
        }
        if (selector2 != null && !isFree) {
            selector2.free();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Selector Switch");
        builder.addDoubleProperty("Voltage1", selector1::getAverageVoltage, null);
        builder.addDoubleProperty("Voltage2", selector2::getAverageVoltage, null);
        builder.addDoubleProperty("Index1", () -> this.getVoltageAsIndex(selector1), null);
        builder.addDoubleProperty("Index2", () -> this.getVoltageAsIndex(selector2), null);
    }

    private static class Range {

        public final double min, max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Log data to SmartDashboard
     */
    public void log() {
        SmartDashboard.putNumber("selector1", getVoltageAsIndex(selector1));
        SmartDashboard.putNumber("selector2", getVoltageAsIndex(selector2));
    }

    /**
     * Log debug information to the console
     */
    public void debugLog() {
        Log.d("AutonomousSelector", "Selected " + getVoltageAsIndex(selector1) + " "
                        + getVoltageAsIndex(selector2));
    }
}
