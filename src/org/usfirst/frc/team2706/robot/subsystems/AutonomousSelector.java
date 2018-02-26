package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.autonomous.Priority;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Controls the 12 switch dial on the robot to select an autonomous mode. The autonomous modes on
 * each of the switches are defined in Robot.java
 */
public class AutonomousSelector extends SensorBase implements Sendable {

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

    public void setCommands(Priority[][]... commands) {
        for (int i = 0; i < NUM_INDICES; i++) {
            for(int j = 0; j < NUM_INDICES; j++) {
             
                if (i < commands.length && j < commands[i].length) {
                   // System.out.println("aaaaaaaaaaa" + commands[i][j]);
                    this.commands[i][j] = commands[i][j];
                } else {
                   // this.commands[i][j] = null;
                }
            }
        }
    }

    /**
     * Gets the currently selected command
     * 
     * @return The selected command
     */
    public Priority[] getSelected() {
        int idx1 = getVoltageAsIndex(selector1);
        int idx2 = getVoltageAsIndex(selector2);
      //  System.out.println(idx1 + "," + idx2);
        if(commands[idx1] == null) {
            idx1 = 0;
        }
        else if (commands[idx1][idx2] == null) {
            idx1 = 2;
            idx2 = 0;
        }
        for(Priority[][] p : commands) {
            for(Priority[] pp : p) {
                for(Priority ppp : pp) {
                    System.out.println("AWFAQWAF" + ppp.linkedCommand.getName());
                }
            }
        }
        System.out.println("bbbbbbbbb" + commands[idx1][idx2]);
        return commands[idx1][idx2];
    }

    public int getVoltageAsIndex(AnalogInput selector) {
        if (isFree) {
            return -1;
        }

        for (int i = 0; i < voltages.length; i++) {
            double voltage = selector.getAverageVoltage();
            if (voltage >= voltages[i].min && voltage < voltages[i].max) {
                return i;
            }
        }

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
        if(selector2 != null && !isFree) {
            selector2.free();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Selector Switch");
        builder.addDoubleProperty("Voltage1", selector1::getAverageVoltage, null);
        builder.addDoubleProperty("Voltage2", selector2::getAverageVoltage, null);
        builder.addDoubleProperty("Index1", ()->this.getVoltageAsIndex(selector1), null);
        builder.addDoubleProperty("Index2", ()->this.getVoltageAsIndex(selector2), null);
    }

    private static class Range {

        public final double min, max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}
