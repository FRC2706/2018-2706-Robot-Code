package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;

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
    //private AnalogInput IR_sensor;
    
    public Intake() {
        //TODO put TALON number assignments in robotmap 
        //RobotMap.INTAKE_MOTOR_RIGHT
        //RobotMap.INTAKE_MOTOR_LEFT
        int INTAKE_MOTOR_RIGHT = 6;
        int INTAKE_MOTOR_LEFT = 7;
        right_intake_motor = new WPI_TalonSRX(INTAKE_MOTOR_RIGHT);
        left_intake_motor = new WPI_TalonSRX(INTAKE_MOTOR_LEFT);
        
        //TODO analog define robot map
      //  int channel = 0; 
      //  IR_sensor = new AnalogInput(channel);
        
    }
    // Turns the robot motors on to suck in the cube
    public void inhaleCube() {
        left_intake_motor.set(-1.0);
        right_intake_motor.set(1.0);
    }
    
    // Turns the robot motors on to fire out the cube
    public void exhaleCube() {
        left_intake_motor.set(1.0);
        right_intake_motor.set(-1.0);
    }
    
    // Stops both motors
    public void stopMotors() {
        left_intake_motor.set(0);
        right_intake_motor.set(0);
    }
    
    /**
     * Uses the IR sensor to detect whether the robot has a cube
     * 
     * @return Whether the robot has a cube or not
     */
    public boolean cubeCaptured() {
       // if (IR_sensor.getVoltage() >= CUBE_CAPTURED) {
       //     return true;
       // }
        return false;
    }

    @Override
    // I don't think we need to do anything here...
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    }
    
        
}


