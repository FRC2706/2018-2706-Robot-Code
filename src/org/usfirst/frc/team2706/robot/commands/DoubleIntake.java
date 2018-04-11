package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

public class DoubleIntake extends LoggedCommand {

    private Intake inhale;
    /**
     * Allows us to use the methods in 'Intake'
     */
    public DoubleIntake() {
        inhale = Robot.intake;
        this.requires(Robot.intake);
       
    }
    
    /**
     * I don't believe initialization is required 
     */
    public void initialize() {
        currentTime = 0;
        intake = true;
        startTime = System.currentTimeMillis();
    }
    long startTime;
    long currentTime;
    boolean intake = true;
    /**
     * Turns the motors on to suck in the cube
     */
    public void execute() {
        currentTime += System.currentTimeMillis() - startTime;
        if(currentTime <= 275 + (intake ? 0 : 0)) {
            if(intake) {
                inhale.leftCube(0.7); 
            }
            else {
                inhale.rightCube(0.7); 
            } 
        } else {
            intake = !intake;
            currentTime = 0;
            startTime = System.currentTimeMillis();
        }
           
    }
    
    /**
     * Sets both Intake motors to 0, stopping them
     */
    public void end() {
        inhale.stopMotors();
    }
    

    @Override
    /**
     * Used to detect whether the motors should stop
     */
    protected boolean isFinished() {
       // if (inhale.cubeCaptured() == true) {
     //       return true;
     //   }
    //    else {
            return false;
    //    }
        
    }

}
