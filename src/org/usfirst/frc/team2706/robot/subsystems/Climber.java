package org.usfirst.frc.team2706.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

// This class is used for usage of the climber 
public class Climber extends Subsystem {
    
    // Defines the climber motor
    private WPI_TalonSRX climber_motor;
    
    public Climber() {
        // TODO Add TALON number assignments in robotmap 
        int CLIMBER_MOTOR = 8; //Unknown exact number at the moment, Value clashes with lift motor (Used for testing) 
        climber_motor = new WPI_TalonSRX(CLIMBER_MOTOR);
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

    /**
     * This is needed by the Subsystem superclass...
     */
    protected void initDefaultCommand() {
    }

}
