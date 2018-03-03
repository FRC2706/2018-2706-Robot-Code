package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;
import org.usfirst.frc.team2706.robot.controls.talon.TalonLimit;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.controls.talon.TalonSensorGroup;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift extends Subsystem{

    TalonLimit liftMotor;
    
    TalonEncoder encoder;
    
    TalonPID liftPID;
    
    DigitalInput liftDown;
    double speed = 1.0;
    
    public Lift() {
        liftDown = new DigitalInput(1);
        liftMotor = new TalonLimit(RobotMap.MOTOR_LIFT, liftDown);
        liftMotor.setNeutralMode(NeutralMode.Brake);
        encoder = new TalonEncoder(liftMotor);
        encoder.setDistancePerPulse(1);
        liftPID = new TalonPID(new TalonSensorGroup(liftMotor,liftMotor::setSafetyEnabled, encoder));
        encoder.reset();
    }

    public TalonPID getPID () {
        return liftPID;
    }
    
    public void move(double liftspeed) {
        liftMotor.set(liftspeed);
    }
    
    public void moveUp () {
        liftMotor.set(speed);
    } 
    
    public void moveDown () {
            liftMotor.set(-1*speed);
        }
       
    
    
    public void stop () {
        liftMotor.set(0.0);
    } 
    
    public void checkHeight () {
        System.out.println("Encoder value:" + encoder.getDistance());   
        double height = encoder.getDistance();
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    }  
    
    public void log() {
        SmartDashboard.putNumber("Lift Distance", encoder.getDistance());
    }
}