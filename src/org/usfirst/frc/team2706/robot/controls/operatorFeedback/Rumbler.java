package org.usfirst.frc.team2706.robot.controls.operatorFeedback;

import org.usfirst.frc.team2706.robot.LoggedCommand;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class Rumbler extends LoggedCommand {
    
    public static final int DRIVER_JOYSTICK = 1;
    public static final int OPERATOR_JOYSTICK = 2;
    public static final int BOTH_JOYSTICKS = 3;
    
    private boolean isFinished = false;
    
    /* An integer which is one DRIVER_JOYSTICK, OPERATOR_JOYSTICK or BOTH_JOYSTICKS
     * values. Represents which controller to rumble
     */ 
    int controllerToRumble;
    
    // How long to rumble
    double timeOn;
    
    // How much break between rumbles
    double timeOff;
    
    // How many times to repeat the pattern
    int repeatCount;
    
    // The intensity setting for the rumble
    double intensity;
    
    // The joysticks that will be rumbled
    Joystick driver;
    Joystick operator;
    
    // The last time in seconds of an event.
    double timePoint;
    
    // States of the pattern
    private static final int RUMBLE = 0;
    private static final int BREAK = 1;

    // Indicate what part of the pattern we're in, RUMBLE or BREAK
    int state;
    
    
    /**
     * Class to rumble the controllers of the robot with the purpose of 
     * giving haptic feedback to the drivers for important events.<p>
     */
    public Rumbler() {
        // Just a default rumble of 1 second 1 time on both joysticks.
        this(1.0, 0, 1, BOTH_JOYSTICKS, 1);
    }
    
    /**
     * Class to rumble the controllers of the robot with the purpose of 
     * giving haptic feedback to the drivers for important events.<p>
     * @param timeOn How long to rumble <p>
     * @param timeOff How long to pause between rumbles <p>
     * @param repeatCount How long to repeat the pattern. Enter a negative number for infinite, but DON'T FORGET TO CALL THE END() FUNCTION.<p>
     * @param controllerToRumble Which controller (one of
     * DRIVER_JOYSTICK, OPERATOR_JOYSTICK or BOTH_JOYSTICKS) to rumble.
     */
    public Rumbler(double timeOn, double timeOff, int repeatCount, int controllerToRumble) {
        this(timeOn, timeOff, repeatCount, controllerToRumble, 1);
    }
    
    
    /**
     * Class to rumble the controllers of the robot with the purpose of 
     * giving haptic feedback to the drivers for important events.<p>
     * @param timeOn How long to rumble <p>
     * @param timeOff How long to pause between rumbles <p>
     * @param repeatCount How long to repeat the pattern. Enter a negative number for infinite, but DON'T FORGET TO CALL THE END() FUNCTION.<p>
     * @param controllerToRumble Which controller (one of
     * @param intensity The rumble intensity setting.
     * DRIVER_JOYSTICK, OPERATOR_JOYSTICK or BOTH_JOYSTICKS) to rumble.
     */
    public Rumbler(double timeOn, double timeOff, int repeatCount, int controllerToRumble, double intensity) {
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.repeatCount = repeatCount;
        this.intensity = intensity;
        
        driver = Robot.oi.getDriverJoystick();
        operator = Robot.oi.getOperatorJoystick();
        
        this.controllerToRumble = controllerToRumble;
        
        // Add this command to the scheduler.
        start();
    }
    
    @Override
    public void initialize() {
        // Begin rumbling
        rumble(true);
    }
    
    @Override
    protected void execute() {
        // Get the time passed since last time point
        double timePassed = Timer.getFPGATimestamp() - timePoint;
        
        boolean rumbleOver = (state == RUMBLE && timePassed > timeOn);
        boolean breakOver = (state == BREAK && timePassed > timeOff);
        
        if (rumbleOver) {
            rumble(false);
            if (repeatCount >= 0) repeatCount--;
        }
        else if (breakOver) rumble(true);
    }
    
    @Override
    protected boolean isFinished() {
        return repeatCount == 0 || isFinished;
    }
    
    @Override
    /**
     * Ends the command nicely, removing it from the command group.
     */
    public void end() {
        isFinished = true;
        rumble(false);
    }
    
    /**
     * Function that toggles the state of the rumble
     * @param on True to turn on rumble, false otherwise.
     */
    private void rumble(boolean on) {
        // Set the time point, so we know when to start or stop rumbling
        timePoint = Timer.getFPGATimestamp();
        
        // Set the state
        state = on ? RUMBLE : BREAK;
        
        // If rumble is on, full power. Otherwise, no power.
        double rumbleIntensity = (on ? intensity : 0.0);
        
        // Rumble the appropriate joysticks
        if (controllerToRumble == DRIVER_JOYSTICK || controllerToRumble == BOTH_JOYSTICKS) {
            driver.setRumble(RumbleType.kRightRumble, rumbleIntensity);
            driver.setRumble(RumbleType.kLeftRumble, rumbleIntensity);   
        }
        
        if (controllerToRumble == OPERATOR_JOYSTICK || controllerToRumble == BOTH_JOYSTICKS) {        
                operator.setRumble(RumbleType.kRightRumble, rumbleIntensity);
                operator.setRumble(RumbleType.kLeftRumble, rumbleIntensity);
        }
    }
    
}
