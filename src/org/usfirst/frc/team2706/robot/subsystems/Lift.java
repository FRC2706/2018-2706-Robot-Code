package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.MoveLiftToDestination;
import org.usfirst.frc.team2706.robot.controls.LimitSwitch;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;
import org.usfirst.frc.team2706.robot.controls.talon.TalonEncoder;
import org.usfirst.frc.team2706.robot.controls.talon.TalonPID;
import org.usfirst.frc.team2706.robot.controls.talon.TalonSensorGroup;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift extends Subsystem{

    private final double maxHeight = 7.0;
    
    WPI_TalonSRX liftMotor = new WPI_TalonSRX(RobotMap.MOTOR_LIFT);
    
    TalonEncoder encoder = new TalonEncoder(liftMotor);
    
    TalonPID liftPID = new TalonPID(new TalonSensorGroup(liftMotor, null, encoder));
    
    LimitSwitch liftDown;
    public static final double SPEED = 0.6;
    
    public Lift() {
        liftMotor.setNeutralMode(NeutralMode.Brake);
        liftMotor.setInverted(RobotMap.MOTOR_LIFT_INVERTED);
        liftMotor.setSensorPhase(true);
        
        liftDown = new LimitSwitch(1);
        liftDown.whileActive(new OneTimeCommand(this::reset));
        encoder.setDistancePerPulse(RobotMap.ENCODER_LIFT_DPP);
        encoder.reset();
    }

    public TalonPID getPID () {
        return liftPID;
    }
    
    public void move(double liftspeed) {
        if((liftspeed < 0 && !liftDown.get()) || (liftspeed > 0 && encoder.getDistance() <= maxHeight)) {
            liftMotor.set(liftspeed);
        }
        else {
            liftMotor.set(0);
        }
    }
    
    public void moveUp () {
        Log.d(this, liftMotor.getControlMode() + " " + liftPID.enabled);
        
        if(encoder.getDistance() <= maxHeight)
            liftMotor.set(SPEED);
    } 
    
    public void moveDown () {
        if(!liftDown.get())
            liftMotor.set(-SPEED);
        }
       
    
    
    public void stop () {
        liftMotor.set(0.0);
    } 
    
    public void setHeight(double d) {
        if(d >= 0 && d <= maxHeight) {
            defaultCommand.setDestination(d);
        }
    }
    
    private MoveLiftToDestination defaultCommand;
    
    /**
     * When no other command is running use PID to hold position
     */
    public void initDefaultCommand() {
        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);
        
        Log.d("Lift Command", defaultCommand);
    }

    public double getEncoderHeight() {
        return encoder.getDistance();
    }
    
    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new MoveLiftToDestination(0);
        }
        return defaultCommand;
    }
    public void log() {
        SmartDashboard.putNumber("Lift Distance", encoder.getDistance());
    }
    
    public void reset() {
        encoder.reset();
    }
}