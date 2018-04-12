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

public class Lift extends Subsystem {

    private static final double DEADZONE = 3.0 / 12.0;

    private static final double[] HEIGHTS = new double[] {0.0, // Bottom of lift
                    3.0, // Switch height
                    5.0, // Scale low height
                    6.0, // Scale mid height
                    7.0 // Scale high height/top of lift
    };

    public static final double MAX_HEIGHT = 7.0;

    TalonLimit liftMotor;

    private TalonEncoder encoder;
    TalonPID liftPID;

    LimitSwitch liftDown;
    public static final double SPEED = 1.0;

    private boolean zeroedOnce = false;
    
    private static final double pDown = 0.5, iDown = 0, dDown = 160;
    private static final double pUp = 0.5, iUp = 0, dUp = 50;
    
    private boolean disabled = false;

    public Lift() {
        liftDown = new LimitSwitch(RobotMap.LIMIT_DOWN);
        liftMotor = new TalonLimit(RobotMap.MOTOR_LIFT, liftDown);
        setBrakeMode(true);
        liftMotor.setInverted(RobotMap.MOTOR_LIFT_INVERTED);
        liftMotor.configClosedloopRamp(0.2, 0);
        encoder = new TalonEncoder(liftMotor);
        liftDown.whileActive(new OneTimeCommand(this::reset));
        liftDown.whenActive(new OneTimeCommand(() -> {
            Log.d("Lift", "Hit limit switch");
            this.reset();
            this.setHeight(0, false);
        }));

        liftPID = new TalonPID(new TalonSensorGroup(liftMotor, null, encoder));
        liftPID.setError(0);
        encoder.setDistancePerPulse(RobotMap.ENCODER_LIFT_DPP);
        encoder.reset();

        liftMotor.configPeakCurrentLimit(0, 0);
        liftMotor.configPeakCurrentDuration(0, 0);
        setRegularCurrentLimit();
        liftMotor.enableCurrentLimit(false);
        
      SmartDashboard.putNumber("P Down", SmartDashboard.getNumber("P Down", pDown));
      SmartDashboard.putNumber("I Down", SmartDashboard.getNumber("I Down", iDown));
      SmartDashboard.putNumber("D Down", SmartDashboard.getNumber("D Down", dDown));
      
      SmartDashboard.putNumber("P Up", SmartDashboard.getNumber("P Up", pUp));
      SmartDashboard.putNumber("I Up", SmartDashboard.getNumber("I Up", iUp));
      SmartDashboard.putNumber("D Up", SmartDashboard.getNumber("D Up", dUp));
    }

    public TalonPID getPID() {
        return liftPID;
    }

    public void move(double liftspeed) {
        if ((liftspeed < 0 && !liftDown.get())
                        || (liftspeed > 0 && encoder.getDistance() <= MAX_HEIGHT)) {
            liftMotor.set(liftspeed);
        } else {
            stop();
        }
    }

    public void moveUp() {
        if (encoder.getDistance() <= MAX_HEIGHT) {
            liftMotor.set(SPEED);
        } else {
            stop();
        }
    }

    public void moveDown() {
        if (!liftDown.get()) {
            liftMotor.set(-SPEED);
        } else {
            stop();
        }
    }



    public void stop() {
        liftMotor.set(0.0);
    }

    public void setHeight(double d, boolean override) {
        d = Math.min(MAX_HEIGHT, d);

        if (bottomLimit()) {
            Log.d("Lift", "Limit hit, height 0");
            d = Math.max(d, 0);
        }
        else if(!override) {
            d = Math.max(d, Double.MIN_VALUE);
        }
        if(defaultCommand != null) {
            defaultCommand.setDestination(d);
        }
        else {
            Log.e("Lift", "Default command null!");
        }
        
    }

    private MoveLiftToDestination defaultCommand;

    public void resetSetpoint() {
        Log.d("Lift", "Resetting setpoint");
        // Override in case it starts negative
        setHeight(encoder.getDistance(), true);
    }

    /**
     * When no other command is running use PID to hold position
     */
    public void initDefaultCommand() {
        if(disabled) {
            return;
        }
        
        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);
    }

    public double getEncoderHeight() {
        return encoder.getDistance();
    }

    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new MoveLiftToDestination();
        }
        return defaultCommand;
    }

    public void log() {
        SmartDashboard.putNumber("Lift Distance", encoder.getDistance());
        SmartDashboard.putNumber("Talon Speed", encoder.getRate());
        SmartDashboard.putString("Talon Command",
                        getCurrentCommand() != null ? getCurrentCommand().getName() : "None");
        SmartDashboard.putNumber("Lift Current", liftMotor.getOutputCurrent());
    }

    public void debugLog() {
        Log.d("Lift", "Position " + encoder.getDistance());
        Log.d("Lift", "Temperature " + liftMotor.getTemperature());
        Log.d("Lift", "Current " + liftMotor.getOutputCurrent());
        Log.d("Lift", "Output " + liftMotor.getMotorOutputPercent());
    }
    
    public boolean bottomLimit() {
        return liftDown.get();
    }

    public void levelUp() {
        move(true);
    }

    public void levelDown() {
        move(false);
    }

    private void move(boolean up) {
        int newLevel = findNewLevel(up);

        if (newLevel != -1) {
            Robot.lift.setHeight(HEIGHTS[newLevel], false);
        }
    }

    private int findNewLevel(boolean up) {
        double height = Robot.lift.getEncoderHeight();

        for (int i = 0; i < HEIGHTS.length; i++) {
            double heightLevel = HEIGHTS[i];
            if (height > heightLevel - DEADZONE && height < heightLevel + DEADZONE) {
                if (up && i + 1 < HEIGHTS.length) {
                    return i + 1;
                } else if (i - 1 >= 0) {
                    return i - 1;
                } else {
                    return -1;
                }
            }
        }

        if (HEIGHTS.length > 1) {
            for (int i = 0; i < HEIGHTS.length - 1; i++) {
                double bottomLevel = HEIGHTS[i];
                double topLevel = HEIGHTS[i + 1];

                if (height > bottomLevel && height < topLevel) {
                    if (up) {
                        return i + 1;
                    } else {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public void reset() {
        Log.d("Lift", "Resetting");
        encoder.reset();
        zeroedOnce = true;

        if (liftPID.getSetpoint() < 0) {
            Log.d("Lift", "Setting height to zero");
            setHeight(0, false);
        }
    }

    public boolean zeroedOnce() {
        return zeroedOnce;
    }

    public void setRegularCurrentLimit() {
        Log.d("Lift", "Current limit to 37");
        liftMotor.configContinuousCurrentLimit(37, 0);
    }

    public void setUnsafeCurrentLimit() {
        Log.d("Lift", "Current limit to 10");
        liftMotor.configContinuousCurrentLimit(10, 0);
    }
    
    public void setPID(double P, double I, double D) {
        liftPID.setPID(P, I, D);
        liftPID.setPID(SmartDashboard.getNumber("P Down", pDown), SmartDashboard.getNumber("I Down", iDown), SmartDashboard.getNumber("D Down", dDown));
    }
    
    public void useUpPID() {
        Log.d("Lift", "Going up");
        
        liftPID.setPID(pUp, iUp, dUp);
        liftPID.setPID(SmartDashboard.getNumber("P Up", pUp), SmartDashboard.getNumber("I Up", iUp), SmartDashboard.getNumber("D Up", dUp));
    }
    
    public void useDownPID() {
        Log.d("Lift", "Going down");
        
        Robot.lift.setPID(pDown, iDown, dDown);
        Robot.lift.setPID(pUp, iUp, dUp);
    }
    
    public void initTestMode() {
        
       new WPI_TalonSRX(5).setName("Lift","Lift Motor");
       liftDown.setName("Lift","Limit Switch Down");
    }
    
    public void setBrakeMode(boolean brakeMode) {
        if(brakeMode) {
            liftMotor.setNeutralMode(NeutralMode.Brake);
        }
        else {
            liftMotor.setNeutralMode(NeutralMode.Coast);
        }
    }
    
    public void enable() {
        Log.i("Lift", "Enabled");
        disabled = false;
    }
    
    public void disableMotor() {
        Log.i("Lift", "Disabled Motor");
        
        liftMotor.stopMotor();
        disabled = true;
    }
    
    public boolean disabled() {
        return disabled;
    }
}
