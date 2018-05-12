package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The climber that pulls the robot up the bar
 */
public class Climber extends Subsystem {

    private WPI_TalonSRX climber_motor;
    private AnalogInput IR_sensor;

    /**
     * Creates the climber and allocates the motor
     */
    public Climber() {
        climber_motor = new WPI_TalonSRX(RobotMap.CLIMBER_MOTOR);

        IR_sensor = new AnalogInput(RobotMap.CLIMBER_IR_SENSOR);
    }

    /**
     * Run the climb motor ensuring that it is positive
     */
    public void climb() {
        climber_motor.set(1.0);
    }

    /**
     * Stops the motor
     */
    public void stopClimberMotor() {
        climber_motor.set(0);
    }

    /**
     * Reads the climber IR sensor
     * 
     * @return
     */
    public double readIRSensor() {
        return IR_sensor.getVoltage();

    }

    protected void initDefaultCommand() {}

    public void initTestMode() {
        new WPI_TalonSRX(8).setName("Climber", "Climber Motor");
        IR_sensor.setName("Climber", "Climber IR");
    }

    /**
     * Log debug information to the console
     */
    public void debugLog() {
        Log.d("Climber", "IR " + readIRSensor());
        Log.d("Climber", "Temperature " + climber_motor.getTemperature());
        Log.d("Climber", "Current " + climber_motor.getOutputCurrent());
        Log.d("Climber", "Output " + climber_motor.getMotorOutputPercent());
    }
}
