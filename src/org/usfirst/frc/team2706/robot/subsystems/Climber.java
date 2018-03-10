package org.usfirst.frc.team2706.robot.subsystems;
import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

// This class is used for usage of the climber 
public class Climber extends Subsystem {
    
    // Defines stuff
    private WPI_TalonSRX climber_motor;
    private AnalogInput IR_sensor;
    
    public Climber() {
        climber_motor = new WPI_TalonSRX(RobotMap.CLIMBER_MOTOR);
        
        IR_sensor = new AnalogInput(RobotMap.CLIMBER_IR_SENSOR);
    }
    
    // Tells the robot to start climbing
    public void climb() {
        double power = 0.5;
        climber_motor.set(java.lang.Math.abs(power)); //Speed may change later
    }
    
    // Stops the motor
    public void stopClimberMotor() {
        climber_motor.set(0);
    }
    
    // Reads the climber IR sensor 
    public double readIRSensor() {
        return IR_sensor.getVoltage();
        
    }

    /**
     * This is needed by the Subsystem superclass...
     */
    protected void initDefaultCommand() {
    }

}
