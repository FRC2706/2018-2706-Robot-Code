package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem; 


// This class is used for the intake of the cube
public class Intake extends Subsystem{ 
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
    
    public Intake() {
        //TODO put TALON number assignments in robotmap 
        
        // Talon stuff
        right_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_RIGHT);
        left_intake_motor = new WPI_TalonSRX(RobotMap.INTAKE_MOTOR_LEFT);
        left_talon_encoder = new TalonEncoder(left_intake_motor);
        right_talon_encoder = new TalonEncoder(right_intake_motor);
        
        
       // right_intake_motor.setInverted(true);
        left_intake_motor.setInverted(true);
        
        //TODO analog define robot map
          int channel = 1; 
          IR_sensor = new AnalogInput(channel);
        
    }
    // Turns the robot motors on to suck in the cube
    public void leftCube(double motorSpeed) {
        left_intake_motor.set( 0.3 * motorSpeed);
        right_intake_motor.set(motorSpeed*-1); 
    }
    
    // Turns the robot motors on to fire out the cube
    public void rightCube(double motorSpeed) {
        left_intake_motor.set(motorSpeed * -1);
        right_intake_motor.set(0.3 *motorSpeed);
    }
    // Turns the robot motors on to suck in the cube
    public void inhaleCube(double motorSpeed) {
        left_intake_motor.set(motorSpeed*-0.5);
        right_intake_motor.set(motorSpeed*-0.25); 
    }
    
    // Turns the robot motors on to fire out the cube
    public void exhaleCube (double motorSpeed) {
        left_intake_motor.set(motorSpeed);
        right_intake_motor.set(motorSpeed);
    }
    
    // Stops both motors instantly
    public void stopMotors() {
        left_intake_motor.set(0);
        right_intake_motor.set(0);
    }
    
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
    // I don't think we need to do anything here...
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    } 
    
        
}


