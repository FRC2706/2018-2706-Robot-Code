package org.usfirst.frc.team2706.robot.controls.talon;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Holds information of a talon, its encoder, and talons following it
 */
public class TalonSensorGroup {

    /**
     * The main talon attached to the encoder
     */
    private final TalonSRX master;

    /**
     * The encoder of the talon
     */
    private final TalonEncoder talonEncoder;

    /**
     * The following talons
     */
    private final TalonSRX[] slaves;

    /**
     * The function to disable safety on the motors
     */
    private final Consumer<Boolean> safetySetter;

    /**
     * Creates a talon sensor group
     * 
     * @param master The talon attached to the sensor
     * @param talonEncoder The encoder of the master talon
     * @param slaves The following talons
     */
    public TalonSensorGroup(TalonSRX master, TalonEncoder talonEncoder, TalonSRX... slaves) {
        this(master, (it) -> {
        }, talonEncoder, slaves);
    }

    /**
     * Creates a talon sensor group
     * 
     * @param master The talon attached to the sensor
     * @param safetySetter The function to disable safety on the motors
     * @param talonEncoder The encoder of the master talon
     * @param slaves The following talons
     */
    public TalonSensorGroup(TalonSRX master, Consumer<Boolean> safetySetter,
                    TalonEncoder talonEncoder, TalonSRX... slaves) {
        this.master = master;
        this.safetySetter = safetySetter;
        this.talonEncoder = talonEncoder;
        this.slaves = slaves;
    }

    /**
     * The master encoder attached to the encoder
     * 
     * @return The master talon
     */
    public TalonSRX getMaster() {
        return master;
    }

    /**
     * Get the encoder attached to the master talon
     * 
     * @return The encoder
     */
    public TalonEncoder getTalonEncoder() {
        return talonEncoder;
    }

    /**
     * The talons that follow the output of the master talon
     * 
     * @return The following talons
     */
    public TalonSRX[] getSlaves() {
        return slaves;
    }

    /**
     * Gets the function to disable safety on the motors
     * 
     * @return The safety setter
     */
    public Consumer<Boolean> getSafetySetter() {
        return safetySetter;
    }
}
