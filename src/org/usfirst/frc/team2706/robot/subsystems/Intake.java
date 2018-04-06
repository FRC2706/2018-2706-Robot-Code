package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 


// This class is used for the intake of the cube
public class Intake extends Subsystem { 
    boolean cubeIn = false; 
    
    private static final double CUBE_CAPTURED = 1.5;
    
    // Objects for inhaling and exhaling the cube
    private WPI_TalonSRX right_intake_motor;
    private WPI_TalonSRX left_intake_motor;
    @SuppressWarnings("unused")
    private TalonEncoder left_talon_encoder;
    @SuppressWarnings("unused")
    private TalonEncoder right_talon_encoder;
    private AnalogInput IR_sensor;
    private double m_leftIntakeMaxPower;
    private double m_rightIntakeMaxPower;
    private double m_ejectMaxPower;
    
    public Intake(double leftIntakePower, double rightIntakePower, double ejectPower) {
        // Talon definition stuff
        right_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_RIGHT);
        left_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_LEFT);
        left_talon_encoder = new TalonEncoder(left_intake_motor);
        right_talon_encoder = new TalonEncoder(right_intake_motor);
        m_rightIntakeMaxPower = rightIntakePower;
        m_leftIntakeMaxPower = leftIntakePower;
        m_ejectMaxPower = ejectPower;
           
        // right_intake_motor.setInverted(true);
        left_intake_motor.setInverted(true);
        
        // IR sensor
        IR_sensor = new AnalogInput(RobotMap.INTAKE_IR_SENSOR);
        
        left_intake_motor.setNeutralMode(NeutralMode.Brake);
        right_intake_motor.setNeutralMode(NeutralMode.Brake);
    }
    
    public void log() {
        SmartDashboard.putNumber("Intake IR", readIRSensor());
        SmartDashboard.putBoolean("CubeIn", cubeCaptured());
    }
    
    // Turns the robot motors on to suck in the cube on the left
    public void leftCube(double motorSpeed) {
        left_intake_motor.set(m_leftIntakeMaxPower * motorSpeed);
        right_intake_motor.set(m_rightIntakeMaxPower * -motorSpeed); 
    }
    
    // Turns the robot motors on to suck in the cube on the right
    public void rightCube(double motorSpeed) {
        left_intake_motor.set(m_leftIntakeMaxPower * -motorSpeed);
        right_intake_motor.set(m_rightIntakeMaxPower * motorSpeed);
    }
    
    // Turns the robot motors on to suck in the cube normally 
    public void inhaleCube(double motorSpeed) {
        right_intake_motor.set(-motorSpeed * m_leftIntakeMaxPower);
        left_intake_motor.set(-motorSpeed * m_rightIntakeMaxPower); 
    }
    
    // Turns the robot motors on to suck in the cube normally 
    public void inhaleCubeStatic(double motorSpeed) {
        left_intake_motor.set(-motorSpeed * 0.75);
        right_intake_motor.set(-motorSpeed * 0.75); 
    }
    
    public void inhaleCubeCustom(double left, double right) {
        left_intake_motor.set(-left);
        right_intake_motor.set(-right);
    }
    // Turns the robot motors on to shoot out the cube
    public void exhaleCube (double motorSpeed) {
        left_intake_motor.set(motorSpeed * m_ejectMaxPower);
        right_intake_motor.set(motorSpeed * m_ejectMaxPower);
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
    
    public void initTestMode() {
        new WPI_TalonSRX(6).setName("Intake","Intake Motor Left");
        new WPI_TalonSRX(7).setName("Intake","Intake Motor Right");
        IR_sensor.setName("Intake","Intake IR");
    }
        
}


