package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.MoveLiftToDestination;
import org.usfirst.frc.team2706.robot.controls.LimitSwitch;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;
import org.usfirst.frc.team2706.robot.controls.talon.TalonLimit;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.controls.talon.TalonSensorGroup;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The motor that moves the arms up and down
 */
public class Lift extends Subsystem {

    private static final double DEADZONE = 3.0 / 12.0;

    private static final double[] HEIGHTS = new double[] {
                    0.0, // Bottom of lift
                    3.0, // Switch height
                    5.0, // Scale low height
                    6.0, // Scale mid height
                    7.0 // Scale high height/top of lift
    };

    /**
     * The maximum height that the lift can raise to in feet
     */
    public static final double MAX_HEIGHT = 7.0;

    private final TalonLimit liftMotor;

    private final TalonEncoder encoder;
    private final TalonPID liftPid;

    private final LimitSwitch liftDown;
    public static final double SPEED = 1.0;

    private boolean zeroedOnce = false;

    private static final double pDown = 0.5, iDown = 0, dDown = 160;
    private static final double pUp = 0.5, iUp = 0, dUp = 50;

    private boolean disabled = false;
    private boolean climbing = false;

    /**
     * Creates and sets up all the parts of the lift
     */
    public Lift() {
        liftDown = new LimitSwitch(RobotMap.LIMIT_DOWN);
        liftMotor = new TalonLimit(RobotMap.MOTOR_LIFT, liftDown);
        setBrakeMode(true);
        liftMotor.setInverted(RobotMap.MOTOR_LIFT_INVERTED);
        liftMotor.configClosedloopRamp(0.2, 0);
        liftMotor.enableCurrentLimit(true);
        encoder = new TalonEncoder(liftMotor);
        liftDown.whileActive(new OneTimeCommand(this::reset));
        liftDown.whenActive(new OneTimeCommand(() -> {
            Log.d("Lift", "Hit limit switch");
            this.reset();
            this.setHeight(0, false);
        }));

        liftPid = new TalonPID(new TalonSensorGroup(liftMotor, null, encoder));
        liftPid.setError(0);
        encoder.setDistancePerPulse(RobotMap.ENCODER_LIFT_DPP);
        encoder.reset();

        liftMotor.configPeakCurrentLimit(0, 0);
        liftMotor.configPeakCurrentDuration(0, 0);
        setRegularCurrentLimit();
        liftMotor.enableCurrentLimit(true);

//         SmartDashboard.putNumber("P Down", SmartDashboard.getNumber("P Down", pDown));
//         SmartDashboard.putNumber("I Down", SmartDashboard.getNumber("I Down", iDown));
//         SmartDashboard.putNumber("D Down", SmartDashboard.getNumber("D Down", dDown));
//        
//         SmartDashboard.putNumber("P Up", SmartDashboard.getNumber("P Up", pUp));
//         SmartDashboard.putNumber("I Up", SmartDashboard.getNumber("I Up", iUp));
//         SmartDashboard.putNumber("D Up", SmartDashboard.getNumber("D Up", dUp));
    }

    /**
     * Gets the PID that controls the lift
     * 
     * @return The PID
     */
    public TalonPID getPID() {
        return liftPid;
    }

    /**
     * Moves the lift at a certain speed. The lift can only move if the lift is moving down but the
     * limit switch isn't pressed, or the lift is moving up but is less than the maximum height.
     * 
     * @param liftspeed The speed to move the lift at
     */
    public void move(double liftspeed) {
        if ((liftspeed < 0 && !liftDown.get())
                        || (liftspeed > 0 && encoder.getDistance() <= MAX_HEIGHT)) {
            liftMotor.set(liftspeed);
        } else {
            stop();
        }
    }

    /**
     * Moves the lift up at the max speed of the lift. The lift can only move if it is less than the
     * maximum lift height.
     */
    public void moveUp() {
        if (encoder.getDistance() <= MAX_HEIGHT) {
            liftMotor.set(SPEED);
        } else {
            stop();
        }
    }

    /**
     * Moves the lift down at the max speed of the lift. The lift can only move if the limit switch
     * is not pressed.
     */
    public void moveDown() {
        if (!liftDown.get()) {
            liftMotor.set(-SPEED);
        } else {
            stop();
        }
    }

    /**
     * Stops the lift motor
     */
    public void stop() {
        liftMotor.set(0.0);
    }

    /**
     * Sets the height of the lift
     * 
     * @param d The height of the lift
     * @param override Whether to ignore some of the safety limits
     */
    public void setHeight(double d, boolean override) {
        // Ensure that the lift height is less than the maximum height
        d = Math.min(MAX_HEIGHT, d);

        // If the limit switch is hit, the lift cannot go below 0
        if (bottomLimit()) {
            Log.d("Lift", "Limit hit, height 0");
            d = Math.max(d, 0);
        }
        // When not overriding the height cannot go below MIN_VALUE, which is effectively 0
        else if (!override) {
            d = Math.max(d, Double.MIN_VALUE);
        }

        // Ensure that there will be no nullpointers
        if (defaultCommand != null) {
            // Set the lift height to the safe height
            defaultCommand.setDestination(d);
        } else {
            Log.e("Lift", "Default command null!");
        }

    }

    private MoveLiftToDestination defaultCommand;

    /**
     * Set the height of the lift to the current height
     */
    public void resetSetpoint() {
        Log.d("Lift", "Resetting setpoint");
        // Override in case it starts negative
        setHeight(encoder.getDistance(), true);
    }

    /**
     * When no other command is running use PID to hold position
     */
    public void initDefaultCommand() {
        if (disabled) {
            return;
        }

        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);
    }

    /**
     * Gets the height of the lift
     * 
     * @return The height of the lift
     */
    public double getEncoderHeight() {
        return encoder.getDistance();
    }

    @Override
    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new MoveLiftToDestination();
        }
        return defaultCommand;
    }

    /**
     * Log data to SmartDashboard
     */
    public void log() {
        SmartDashboard.putNumber("Lift Distance", encoder.getDistance());
        SmartDashboard.putNumber("Talon Speed", encoder.getRate());
        SmartDashboard.putString("Talon Command",
                        getCurrentCommand() != null ? getCurrentCommand().getName() : "None");
        SmartDashboard.putNumber("Lift Current", liftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Lift Temperature", liftMotor.getTemperature());
    }

    /**
     * Log debug information to the console
     */
    public void debugLog() {
        Log.d("Lift", "Position " + encoder.getDistance());
        Log.d("Lift", "Temperature " + liftMotor.getTemperature());
        Log.d("Lift", "Current " + liftMotor.getOutputCurrent());
        Log.d("Lift", "Output " + liftMotor.getMotorOutputPercent());
    }

    /**
     * Get whether the bottom limit switch has been pressed
     * 
     * @return Whether the switch has been pressed
     */
    public boolean bottomLimit() {
        return liftDown.get();
    }

    /**
     * Move the height of the lift to the next level up
     */
    public void levelUp() {
        move(true);
    }

    /**
     * Move the height of the lift to the next level down
     */
    public void levelDown() {
        move(false);
    }

    /**
     * Move the height of the lift up or down a level
     */
    private void move(boolean up) {
        // Find the next level
        int newLevel = findNewLevel(up);

        // Set the height to the new level
        if (newLevel != -1) {
            Robot.lift.setHeight(HEIGHTS[newLevel], false);
        }
    }

    /**
     * Finds the new level that the lift should move to
     * 
     * @param up Whether the level should move up or down
     * @return The new level
     */
    private int findNewLevel(boolean up) {
        // Get the height of the lift
        double height = Robot.lift.getEncoderHeight();

        for (int i = 0; i < HEIGHTS.length; i++) {
            double heightLevel = HEIGHTS[i];

            // Check whether the height is close enough to the height level
            if (height > heightLevel - DEADZONE && height < heightLevel + DEADZONE) {
                // Move the level up or down if possible
                if (up && i + 1 < HEIGHTS.length) {
                    return i + 1;
                } else if (i - 1 >= 0) {
                    return i - 1;
                }
                // Next index out of range
                else {
                    return -1;
                }
            }
        }

        if (HEIGHTS.length > 1) {
            for (int i = 0; i < HEIGHTS.length - 1; i++) {
                double bottomLevel = HEIGHTS[i];
                double topLevel = HEIGHTS[i + 1];

                // Check if the lift is between two levels
                if (height > bottomLevel && height < topLevel) {
                    // Choose the upper level if moving up
                    if (up) {
                        return i + 1;
                    }
                    // Choose the lower level if moving down
                    else {
                        return i;
                    }
                }
            }
        }

        // Return invalid level
        return -1;
    }

    /**
     * Reset the lift height, should be down very carefully to avoid an out of control lift
     */
    public void reset() {
        Log.d("Lift", "Resetting");
        encoder.reset();
        zeroedOnce = true;

        if (liftPid.getSetpoint() < 0) {
            Log.d("Lift", "Setting height to zero");
            setHeight(0, false);
        }
    }

    /**
     * Check whether the lift has hit the limit switch at least once
     * 
     * @return Whether the limit swtich has been hit once
     */
    public boolean zeroedOnce() {
        return zeroedOnce;
    }

    /**
     * Use a current limit that shouldn't normally be hit for normal lift operations
     */
    public void setRegularCurrentLimit() {
        Log.i("Lift", "Current limit to 60");
        liftMotor.configContinuousCurrentLimit(60, 0);
    }

    /**
     * Use a lower current limit for when there is a change that the lift could hit the bottom or
     * the top
     */
    public void setUnsafeCurrentLimit() {
        Log.d("Lift", "Current limit to 10");
        liftMotor.configContinuousCurrentLimit(10, 0);
    }

    /**
     * Sets the PID parameters to use on the lift
     * 
     * @param P The P value
     * @param I The I value
     * @param D The D value
     */
    public void setPID(double P, double I, double D) {
        liftPid.setPID(P, I, D);
    }

    public void useUpPID() {
        Log.d("Lift", "Going up");

        liftPid.setPID(pUp, iUp, dUp);
//         liftPid.setPID(SmartDashboard.getNumber("P Up", pUp), SmartDashboard.getNumber("I Up", iUp), SmartDashboard.getNumber("D Up", dUp));
    }

    public void useDownPID() {
        Log.d("Lift", "Going down");

        Robot.lift.setPID(pDown, iDown, dDown);
//      liftPid.setPID(SmartDashboard.getNumber("P Down", pDown), SmartDashboard.getNumber("I Down", iDown), SmartDashboard.getNumber("D Down", dDown));
    }

    public void initTestMode() {

        new WPI_TalonSRX(5).setName("Lift", "Lift Motor");
        liftDown.setName("Lift", "Limit Switch Down");
    }

    /**
     * Sets whether to use brake mode on the lift
     * 
     * @param brakeMode Whether to use brake mode
     */
    public void setBrakeMode(boolean brakeMode) {
        if (brakeMode) {
            liftMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            liftMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

    /**
     * Enable the lift, and allow the PID to control it
     * 
     * @param override Whether to ignore that climbing has begun and re-enable the motor
     */
    public void enable(boolean override) {
        // The lift can only be re-enabled after climbing when specified
        if(override) {
            // Disable lift re-enable lockout
            climbing = false;
        }
        
        // Only re-enable lift if there is no disable lockout
        if(!climbing) {
            // Lift is no longer disabled
            disabled = false;
        }
    }

    /**
     * Stops the motor and ensures it can't be restarted
     * 
     * @param climb Whether the motor is disabled for climb and must be overridden to re-enable
     */
    public void disableMotor(boolean climb) {
        this.getDefaultCommand().cancel();
        liftMotor.stopMotor();
        disabled = true;
        climbing = climbing || climb;
    }

    /**
     * Get whether the lift is disabled
     * 
     * @return Whether the lift is disabled
     */
    public boolean disabled() {
        return disabled;
    }

    /**
     * Whether to keep motor disabled, unless told climb is finished
     * 
     * @return
     */
    public boolean climbing() {
        return climbing;
    }
}
