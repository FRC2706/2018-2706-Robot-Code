package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Robot;
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

    
   private static final double DEADZONE = 3.0/12.0;
    
    private static final double[] HEIGHTS = new double[] {
        Double.MIN_VALUE, // Bottom of lift
        3.0, // Switch height
        5.0, // Scale low height
        6.0, // Scale mid height
        7.0  // Scale high height/top of lift
    };
    
    public static final double MAX_HEIGHT = 7.0;
    
    WPI_TalonSRX liftMotor = new WPI_TalonSRX(RobotMap.MOTOR_LIFT);
    
    TalonEncoder encoder = new TalonEncoder(liftMotor);
    
    TalonPID liftPID = new TalonPID(new TalonSensorGroup(liftMotor, null, encoder));
    
    LimitSwitch liftDown;
    public static final double SPEED = 1.0;
    
    public Lift() {
        liftMotor.setNeutralMode(NeutralMode.Brake);
        liftMotor.setInverted(RobotMap.MOTOR_LIFT_INVERTED);
        
        liftDown = new LimitSwitch(1);
        liftDown.whileActive(new OneTimeCommand(encoder::reset));
        encoder.setDistancePerPulse(RobotMap.ENCODER_LIFT_DPP);
    }

    public TalonPID getPID () {
        return liftPID;
    }
    
    public void move(double liftspeed) {
        if((liftspeed < 0 && !liftDown.get()) || (liftspeed > 0 && encoder.getDistance() <= MAX_HEIGHT)) {
            liftMotor.set(liftspeed);
        }
        else {
            stop();
        }
    }
    
    public void moveUp () {
        if(encoder.getDistance() <= MAX_HEIGHT) {
            liftMotor.set(SPEED);
        }
        else {
            stop();
        }
    } 
    
    public void moveDown () {
        if(!liftDown.get()) {
            liftMotor.set(-SPEED);
        }
        else {
            stop();
        }
    }
       
    
    
    public void stop () {
        liftMotor.set(0.0);
    } 
    
    public void setHeight(double d) {
        defaultCommand.setDestination(Math.max(0, Math.min(MAX_HEIGHT, d)));
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
        SmartDashboard.putString("Talon Command", getCurrentCommand().getName());
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
        
        if(newLevel != -1) {
            Robot.lift.setHeight(HEIGHTS[newLevel]);
        }
    }
    
    private int findNewLevel(boolean up) {
        double height = Robot.lift.getEncoderHeight();
        
        for(int i = 0; i < HEIGHTS.length; i++) {
            double heightLevel = HEIGHTS[i];
            if(height > heightLevel - DEADZONE && height < heightLevel + DEADZONE) {
                if(up && i + 1 < HEIGHTS.length) {
                    return i+1;
                }
                else if(i - 1 >= 0) {
                    return i-1;
                }
                else {
                    return -1;
                }
            }
        }
        
        if(HEIGHTS.length > 1){
            for(int i = 0; i < HEIGHTS.length - 1; i++) {
                double bottomLevel = HEIGHTS[i];
                double topLevel = HEIGHTS[i+1];
                
                if(height > bottomLevel && height < topLevel) {
                    if(up) {
                        return i+1;
                    }
                    else {
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
}