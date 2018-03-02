package org.usfirst.frc.team2706.robot.subsystems;

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
        
        // Stop the lift from going too low or too high
        liftMotor.configForwardSoftLimitThreshold((int) (maxHeight / encoder.getDistancePerPulse()), 0);
        liftMotor.configForwardSoftLimitEnable(true, 0);
        
        liftMotor.configReverseSoftLimitThreshold(0, 0);
        liftMotor.configReverseSoftLimitEnable(true, 0);
        
        
        
        liftDown = new LimitSwitch(1);
        liftDown.whileActive(new OneTimeCommand(encoder::reset));
        encoder.setDistancePerPulse(RobotMap.ENCODER_LIFT_DPP);
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
        if(encoder.getDistance() <= maxHeight) {
            liftMotor.set(SPEED);
        }
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
        SmartDashboard.putData("Talon Command", getCurrentCommand());
    }
}