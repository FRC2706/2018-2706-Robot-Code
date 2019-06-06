/**
 * WPI Compliant motor controller class.
 * WPILIB's object model requires many interfaces to be implemented to use
 * the various features.
 * This includes...
 * - Software PID loops running in the robot controller
 * - LiveWindow/Test mode features
 * - Motor Safety (auto-turn off of motor if Set stops getting called)
 * - Single Parameter set that assumes a simple motor controller.
 */
package org.usfirst.frc.team2706.robot.controls.talon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.WPI_MotorSafetyImplem;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * An extended {@code WPI_TalonSRX} that can use current mode when setting output
 */
public class EWPI_TalonSRX extends TalonSRX implements SpeedController, Sendable {

    private String _description;
    private double _speed;
    private boolean useVoltage;
    private double maxCurrent;

    /**
     * The default motor safety timeout IF calling application
     * enables the feature.
     */
    public static final double kDefaultSafetyExpiration = 0.1;

    /**
     * Late-constructed motor safety, which ensures feature is off unless calling
     * applications explicitly enables it.
     */
    private WPI_MotorSafetyImplem _motorSafety = null;
    private final Object _lockMotorSaf = new Object();
    private double _motSafeExpiration = kDefaultSafetyExpiration;

    /**
     * Constructor for motor controller
     * @param deviceNumber device ID of motor controller
     */
    public EWPI_TalonSRX(int deviceNumber) {
        super(deviceNumber);
        HAL.report(FRCNetComm.tResourceType.kResourceType_CTRE_future2, deviceNumber + 1);
        _description = "Talon SRX " + deviceNumber;

        this.useVoltage = true;
        this.maxCurrent = Double.MAX_VALUE;

        LiveWindow.add(this);
        setName("Talon SRX ", deviceNumber);
    }

    public void setUseVoltage(boolean useVoltage) {
        this.useVoltage = useVoltage;
    }

    public boolean getUseVoltage() {
        return useVoltage;
    }

    // ------ set/get routines for WPILIB interfaces ------//
    @Override
    public void set(double speed) {
        _speed = speed;

        if(useVoltage) {
            set(ControlMode.PercentOutput, _speed);
        }
        else {
            set(ControlMode.Current, speed * maxCurrent);
        }
        _motorSafety.feed();
    }

    /**
     * Special write for PID, same functionality as calling set
     * @param output Output to send to motor
     */
    @Override
    public void pidWrite(double output) {
        set(output);
    }

    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    @Override
    public double get() {
        return _speed;
    }

    // ---------Intercept CTRE calls for motor safety ---------//
    /**
     * Sets the appropriate output on the talon, depending on the mode.
     * @param mode The output mode to apply.
     * In PercentOutput, the output is between -1.0 and 1.0, with 0.0 as stopped.
     * In Current mode, output value is in amperes.
     * In Velocity mode, output value is in position change / 100ms.
     * In Position mode, output value is in encoder ticks or an analog value,
     *   depending on the sensor.
     * In Follower mode, the output value is the integer device ID of the talon to
     * duplicate.
     *
     * @param value The setpoint value, as described above.
     *
     *
     *	Standard Driving Example:
     *	_talonLeft.set(ControlMode.PercentOutput, leftJoy);
     *	_talonRght.set(ControlMode.PercentOutput, rghtJoy);
     */
    public void set(ControlMode mode, double value) {
        /* intercept the advanced Set and feed motor-safety */
        super.set(mode, value);
        feed();
    }

    /**
     * @deprecated use 4 parameter set
     * @param mode Sets the appropriate output on the talon, depending on the mode.
     * @param demand0 The output value to apply.
     * 	such as advanced feed forward and/or auxiliary close-looping in firmware.
     * In PercentOutput, the output is between -1.0 and 1.0, with 0.0 as stopped.
     * In Current mode, output value is in amperes.
     * In Velocity mode, output value is in position change / 100ms.
     * In Position mode, output value is in encoder ticks or an analog value,
     *   depending on the sensor. See
     * In Follower mode, the output value is the integer device ID of the talon to
     * duplicate.
     *
     * @param demand1 Supplemental value.  This will also be control mode specific for future features.
     */
    @Deprecated
    public void set(ControlMode mode, double demand0, double demand1) {
        /* intercept the advanced Set and feed motor-safety */
        super.set(mode, demand0, demand1);
        feed();
    }

    /**
     * @param mode Sets the appropriate output on the talon, depending on the mode.
     * @param demand0 The output value to apply.
     * 	such as advanced feed forward and/or auxiliary close-looping in firmware.
     * In PercentOutput, the output is between -1.0 and 1.0, with 0.0 as stopped.
     * In Current mode, output value is in amperes.
     * In Velocity mode, output value is in position change / 100ms.
     * In Position mode, output value is in encoder ticks or an analog value,
     *   depending on the sensor. See
     * In Follower mode, the output value is the integer device ID of the talon to
     * duplicate.
     *
     * @param demand1Type The demand type for demand1.
     * Neutral: Ignore demand1 and apply no change to the demand0 output.
     * AuxPID: Use demand1 to set the target for the auxiliary PID 1.
     * ArbitraryFeedForward: Use demand1 as an arbitrary additive value to the
     *	 demand0 output.  In PercentOutput the demand0 output is the motor output,
     *   and in closed-loop modes the demand0 output is the output of PID0.
     * @param demand1 Supplmental output value.  Units match the set mode.
     *
     *
     *  Arcade Drive Example:
     *		_talonLeft.set(ControlMode.PercentOutput, joyForward, DemandType.ArbitraryFeedForward, +joyTurn);
     *		_talonRght.set(ControlMode.PercentOutput, joyForward, DemandType.ArbitraryFeedForward, -joyTurn);
     *
     *	Drive Straight Example:
     *	Note: Selected Sensor Configuration is necessary for both PID0 and PID1.
     *		_talonLeft.follow(_talonRght, FollwerType.AuxOutput1);
     *		_talonRght.set(ControlMode.PercentOutput, joyForward, DemandType.AuxPID, desiredRobotHeading);
     *
     *	Drive Straight to a Distance Example:
     *	Note: Other configurations (sensor selection, PID gains, etc.) need to be set.
     *		_talonLeft.follow(_talonRght, FollwerType.AuxOutput1);
     *		_talonRght.set(ControlMode.MotionMagic, targetDistance, DemandType.AuxPID, desiredRobotHeading);
     */
    public void set(ControlMode mode, double demand0, DemandType demand1Type, double demand1){
        /* intercept the advanced Set and feed motor-safety */
        super.set(mode, demand0, demand1Type, demand1);
        feed();
    }

    // ----------------------- Invert routines -------------------//
    /**
     * Common interface for inverting direction of a speed controller.
     *
     * @param isInverted The state of inversion, true is inverted.
     */
    @Override
    public void setInverted(boolean isInverted) {
        super.setInverted(isInverted);
    }

    /**
     * Common interface for returning the inversion state of a speed controller.
     *
     * @return The state of inversion, true is inverted.
     */
    @Override
    public boolean getInverted() {
        return super.getInverted();
    }

    // ----------------------- turn-motor-off routines-------------------//
    /**
     * Common interface for disabling a motor.
     */
    @Override
    public void disable() {
        neutralOutput();
    }

    /**
     * Common interface to stop the motor until Set is called again.
     */
    @Override
    public void stopMotor() {
        neutralOutput();
    }

    // ---- essentially a copy of SendableBase -------//
    private String m_name = "";
    private String m_subsystem = "Ungrouped";

    /**
     * Free the resources used by this object.
     */
    public void free() {
        LiveWindow.remove(this);
    }

    /**
     * @return name of object
     */
    @Override
    public final synchronized String getName() {
        return m_name;
    }

    /**
     * Sets the name of the object
     *
     * @param name
     * 			  name of object
     */
    @Override
    public final synchronized void setName(String name) {
        m_name = name;
    }

    /**
     * Sets the name of the sensor with a channel number.
     *
     * @param moduleType
     *            A string that defines the module name in the label for the
     *            value
     * @param channel
     *            The channel number the device is plugged into
     */
    protected final void setName(String moduleType, int channel) {
        setName(moduleType + "[" + channel + "]");
    }

    /**
     * Sets the name of the sensor with a module and channel number.
     *
     * @param moduleType
     *            A string that defines the module name in the label for the
     *            value
     * @param moduleNumber
     *            The number of the particular module type
     * @param channel
     *            The channel number the device is plugged into (usually PWM)
     */
    protected final void setName(String moduleType, int moduleNumber, int channel) {
        setName(moduleType + "[" + moduleNumber + "," + channel + "]");
    }

    /**
     * @return subsystem name of this object
     */
    @Override
    public final synchronized String getSubsystem() {
        return m_subsystem;
    }

    /**
     * Sets the subsystem name of this object
     *
     * @param subsystem
     */
    @Override
    public final synchronized void setSubsystem(String subsystem) {
        m_subsystem = subsystem;
    }

    /**
     * Add a child component.
     *
     * @param child
     *            child component
     */
    protected final void addChild(Object child) {
        LiveWindow.addChild(this, child);
    }

    /**
     * Initialize sendable
     * @param builder Base sendable to build on
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Speed Controller");
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
    }

    /**
     * @return description of controller
     */
    public String getDescription() {
        return _description;
    }

    /* ----- Motor Safety ----- */
    /** caller must lock appropriately */
    private WPI_MotorSafetyImplem GetMotorSafety() {
        if (_motorSafety == null) {
            /* newly created MS object */
            _motorSafety = new WPI_MotorSafetyImplem(this, getDescription());
            /* apply the expiration timeout */
            _motorSafety.setExpiration(_motSafeExpiration);
        }
        return _motorSafety;
    }
    /**
     * Feed the motor safety object.
     *
     * <p>Resets the timer on this object that is used to do the timeouts.
     */
    public void feed() {
        synchronized (_lockMotorSaf) {
            if (_motorSafety == null) {
                /* do nothing, MS features were never enabled */
            } else {
                GetMotorSafety().feed();
            }
        }
    }

    /**
     * Set the expiration time for the corresponding motor safety object.
     *
     * @param expirationTime The timeout value in seconds.
     */
    public void setExpiration(double expirationTime) {
        synchronized (_lockMotorSaf) {
            /* save the value for if/when we do create the MS object */
            _motSafeExpiration = expirationTime;
            /* apply it only if MS object exists */
            if (_motorSafety == null) {
                /* do nothing, MS features were never enabled */
            } else {
                /* this call will trigger creating a registered MS object */
                GetMotorSafety().setExpiration(_motSafeExpiration);
            }
        }
    }

    /**
     * Retrieve the timeout value for the corresponding motor safety object.
     *
     * @return the timeout value in seconds.
     */
    public double getExpiration() {
        synchronized (_lockMotorSaf) {
            /* return the intended expiration time */
            return _motSafeExpiration;
        }
    }

    /**
     * Determine of the motor is still operating or has timed out.
     *
     * @return a true value if the motor is still operating normally and hasn't timed out.
     */
    public boolean isAlive() {
        synchronized (_lockMotorSaf) {
            if (_motorSafety == null) {
                /* MC is alive - MS features were never enabled to neutral the MC. */
                return true;
            } else {
                return GetMotorSafety().isAlive();
            }
        }
    }

    /**
     * Enable/disable motor safety for this device.
     *
     * <p>Turn on and off the motor safety option for this PWM object.
     *
     * @param enabled True if motor safety is enforced for this object
     */
    public void setSafetyEnabled(boolean enabled) {
        synchronized (_lockMotorSaf) {
            if (_motorSafety == null && !enabled) {
				/* Caller wants to disable MS,
					but MS features were nevere enabled,
					so it doesn't need to be disabled. */
            } else {
                /* MS will be created if it does not exist */
                GetMotorSafety().setSafetyEnabled(enabled);
            }
        }
    }

    /**
     * Return the state of the motor safety enabled flag.
     *
     * <p>Return if the motor safety is currently enabled for this device.
     *
     * @return True if motor safety is enforced for this device
     */
    public boolean isSafetyEnabled() {
        synchronized (_lockMotorSaf) {
            if (_motorSafety == null) {
                /* MS features were never enabled. */
                return false;
            } else {
                return GetMotorSafety().isSafetyEnabled();
            }
        }
    }

    private void timeoutFunc() {
        DriverStation ds = DriverStation.getInstance();
        if (ds.isDisabled() || ds.isTest()) {
            return;
        }

        DriverStation.reportError(getDescription() + "... Output not updated often enough.", false);

        stopMotor();
    }
}
