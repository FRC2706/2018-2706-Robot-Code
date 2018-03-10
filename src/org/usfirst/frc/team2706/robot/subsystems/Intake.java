package org.usfirst.frc.team2706.robot.subsystems;

import java.awt.geom.Arc2D.Double;

import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem; 


// This class is used for the intake of the cube
public class Intake extends Subsystem { 
    boolean cubeIn = false; 
    
    private static final double CUBE_CAPTURED = 0.5;
    
    // Objects for inhaling and exhaling the cube
    private WPI_TalonSRX right_intake_motor;
    private WPI_TalonSRX left_intake_motor;
    @SuppressWarnings("unused")
    private TalonEncoder left_talon_encoder;
    @SuppressWarnings("unused")
    private TalonEncoder right_talon_encoder;
    private AnalogInput IR_sensor;
    private double m_leftPower;
    private double m_rightPower;
    
    public Intake(double leftPower, double rightPower) {
        // Talon definition stuff
        right_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_RIGHT);
        left_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_LEFT);
        left_talon_encoder = new TalonEncoder(left_intake_motor);
        right_talon_encoder = new TalonEncoder(right_intake_motor);
        m_rightPower = rightPower;
        m_leftPower = leftPower;
           
        // right_intake_motor.setInverted(true);
        left_intake_motor.setInverted(true);
        
        // IR sensor
        IR_sensor = new AnalogInput(RobotMap.INTAKE_IR_SENSOR);
    }
    
    // Turns the robot motors on to suck in the cube on the left
    public void leftCube(double motorSpeed) {
        left_intake_motor.set(m_leftPower * motorSpeed);
        right_intake_motor.set(m_rightPower * -motorSpeed); 
    }
    
    // Turns the robot motors on to suck in the cube on the right
    public void rightCube(double motorSpeed) {
        left_intake_motor.set(m_leftPower * -motorSpeed);
        right_intake_motor.set(m_rightPower * motorSpeed);
    }
    
    // Turns the robot motors on to suck in the cube normally 
    public void inhaleCube(double motorSpeed) {
        left_intake_motor.set(-motorSpeed * m_leftPower);
        right_intake_motor.set(-motorSpeed * m_rightPower); 
    }
    
    // Turns the robot motors on to shoot out the cube
    public void exhaleCube (double motorSpeed) {
        left_intake_motor.set(motorSpeed * m_leftPower);
        right_intake_motor.set(motorSpeed * m_rightPower);
    }
    
    // Stops both motors instantly
    public void stopMotors() {
        left_intake_motor.set(0);
        right_intake_motor.set(0);
    }
    
    // Reads the intake IR sensor 
    public double readIRSensor() {
        return IR_sensor.getVoltage();
    }
    
    /**
     * Uses the IR sensor to detect whether the robot has a cube
     * 
     * @return Whether the robot has a cube or not
     */
    public boolean cubeCaptured() {
       if (IR_sensor.getVoltage() >= CUBE_CAPTURED) {
            return true;
       }
        return false;
    }

    @Override
    /** I honestly don't think we need to do anything here... It's required by 
     * the superclass. 
     */
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    } 
    
        
}


