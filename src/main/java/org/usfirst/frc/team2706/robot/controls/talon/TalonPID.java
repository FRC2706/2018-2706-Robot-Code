package org.usfirst.frc.team2706.robot.controls.talon;

import org.usfirst.frc.team2706.robot.Log;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Handles driving with talon PIDs FIXME: Sometimes finished early when the error in the talons
 * isn't updated quickly enough
 */
public class TalonPID {

    /**
     * The talons that will PID
     */
    private final TalonSensorGroup[] talons;

    /**
     * The PID and feed forward values of the PID
     */
    private double P, I, D, FF;

    /**
     * The minimum error to be considered on target
     */
    private double error;

    /**
     * The location to go to
     */
    private double setpoint;

    public boolean enabled;

    /**
     * The minimum and maximum outputs
     */
    private double minOutput, maxOutput;

    /**
     * Constructs the PID with a group of talons
     * 
     * @param talons The talons to PID with
     */
    public TalonPID(TalonSensorGroup... talons) {
        this.talons = talons;
    }

    /**
     * Sets the PID of the PID
     * 
     * @param P The proportional value
     * @param I The integral value
     * @param D The derivative value
     */
    public void setPID(double P, double I, double D) {
        // Log.d("TalonPID", P + " " + I + " " + D);
        setPID(P, I, D, 0);
    }

    /**
     * Sets the PID and feed forward of the PID
     * 
     * @param P The proportional value
     * @param I The integral value
     * @param D The derivative value
     * @param FF The feed forward value
     */
    public void setPID(double P, double I, double D, double FF) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.FF = FF;
    }

    /**
     * Gets the tolerable distance from the setpoint
     * 
     * @return The desired error
     */
    public double getError() {
        return error;
    }

    /**
     * Sets the tolerable distance from the setpoint
     * 
     * @param error The desired error
     */
    public void setError(double error) {
        this.error = error;
    }

    /**
     * Get the location to go to
     * 
     * @return The location to go to
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * Sets the location to go to
     * 
     * @param setpoint The location to go to
     */
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    /**
     * The possible speed the motors can drive to
     * 
     * @param min The minimum speed
     * @param max The maximum
     */
    public void setOutputRange(double min, double max) {
        this.minOutput = min;
        this.maxOutput = max;
    }

    /**
     * Gets the minimum output
     * 
     * @return The minimum output
     */
    public double getMinOutput() {
        return minOutput;
    }

    /**
     * Gets the maximum output
     * 
     * @return The maximum output
     */
    public double getMaxOutput() {
        return maxOutput;
    }

    /**
     * Enables the PID
     */
    public void enable() {
        Log.d("TalonaPID", "Enabled");

        // Apply initial settings to each talon
        for (TalonSensorGroup talon : talons) {
            TalonSRX master = talon.getMaster();

            // Set the min and max of each motor
            master.configNominalOutputForward(0, 0);
            master.configNominalOutputReverse(0, 0);
            master.configPeakOutputForward(maxOutput, 0);
            master.configPeakOutputReverse(minOutput, 0);

            // Set the error
            master.configAllowableClosedloopError(0,
                            (int) Math.abs(error / talon.getTalonEncoder().getDistancePerPulse()),
                            0);

            // Disable safety stop
            if (talon.getSafetySetter() != null) {
                talon.getSafetySetter().accept(false);
            }

            // Set the motor to the desired position
            master.set(ControlMode.Position,
                            setpoint / talon.getTalonEncoder().getDistancePerPulse());

            // Ensure each slave is following the master
            for (TalonSRX slave : talon.getSlaves()) {
                slave.follow(talon.getMaster());
            }
        }

        enabled = true;
    }

    /**
     * Disables the PID
     */
    public void disable() {
        Log.d("TalonaPID", "Disabled");

        // Apply initial settings to each talon
        for (TalonSensorGroup talon : talons) {
            TalonSRX master = talon.getMaster();

            // Reset PID values
            master.config_kP(0, 0, 0);
            master.config_kI(0, 0, 0);
            master.config_kD(0, 0, 0);
            master.config_kF(0, 0, 0);

            // Reset the min and max of each motor
            master.configNominalOutputForward(0, 0);
            master.configNominalOutputReverse(0, 0);
            master.configPeakOutputForward(1, 0);
            master.configPeakOutputReverse(-1, 0);

            // Reset the desired error
            master.configAllowableClosedloopError(0, 0, 0);

            // Re-enable safety stop
            if (talon.getSafetySetter() != null) {
                talon.getSafetySetter().accept(true);
            }

            // Make the master stop PIDing
            master.set(ControlMode.PercentOutput, 0.0);

            // Make all followers stop following
            for (TalonSRX slave : talon.getSlaves()) {
                slave.set(ControlMode.PercentOutput, 0.0);
            }
        }

        enabled = false;
    }

    public void update() {
        if (enabled) {

            for (TalonSensorGroup talon : talons) {
                // Set the motor to the desired position
                talon.getMaster().set(ControlMode.Position,
                                setpoint / talon.getTalonEncoder().getDistancePerPulse());
            }

            for (TalonSensorGroup talon : talons) {
                // Set the motor to the desired position
                talon.getMaster().config_kP(0, P, 0);
                talon.getMaster().config_kI(0, I, 0);
                talon.getMaster().config_kD(0, D, 0);
                talon.getMaster().config_kF(0, FF, 0);
            }
        }
    }

    /**
     * Checks if the talons are all on target
     * 
     * @return Whether the talons are on target
     */
    public boolean isOnTarget() {
        // Check each talon
        for (TalonSensorGroup talon : talons) {
            // Talons aren't on target if at least one is off target
            if (Math.abs(talon.getMaster().getClosedLoopError(0)
                            * talon.getTalonEncoder().getDistancePerPulse()) > error) {
                return false;
            }
        }

        // All talons are on target
        return true;
    }
}
