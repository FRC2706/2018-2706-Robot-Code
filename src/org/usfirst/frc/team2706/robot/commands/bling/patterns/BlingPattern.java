package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;

/**
 * The template for Bling Patterns. Shouldn't be instantiated.
 * @author eAUE (Kyle Anderson)
 *
 */
public abstract class BlingPattern {
    
    protected Bling blingSystem;
    protected DriveTrain driveTrain;
    
    protected int[] rgbColourCode = new int[3];
    
    protected int repeatCount = 10000;
    protected int wait_ms = 50;
    protected int LED_BRIGHTNESS = 255;
    String command = Bling.COLOUR_WIPE;
   
    protected List<Integer> operationPeriod = new ArrayList<Integer>();
    
    /* 
     * True if the pattern has already been run at least once
     * and it was the last pattern to run.
     */
    protected boolean hasRun = false;
    
    public BlingPattern() {        
        blingSystem = Robot.blingSystem;
        driveTrain = Robot.driveTrain;
        
        
        rgbColourCode[0] = 255;
        rgbColourCode[1] = 0;
        rgbColourCode[2] = 0;
    }
    
    /**
     * Analyzes whether or not the conditions for this pattern are being met,
     * and returns a boolean based on this.
     * This should be overriden for real bling commands, as by default it returns false every time.
     * @return True if this bling pattern's operating conditions are met, false
     * otherwise.
     */
    public abstract boolean conditionsMet();
    
    /**
     * Gets an integer array representation of the RGB colour code for the pattern
     * @return The RGB array representation of the pattern's colour
     */
    public int[] getRGB() {
        return rgbColourCode;
    }
    
    /**
     * Gets the repeat counnt for the pattern
     * @return The repeat count (number of times to repeat) of the pattern
     */
    public int getRepeatCount() {
        return repeatCount;
    }
    
    /**
     * 
     * @return The brightness (from 0 to 255) of the LED strip when displaying this pattern
     */
    public int getBrightness() {
        return LED_BRIGHTNESS;
    }
    /**
     * Gets the string command type for the pattern. Should be one of the Bling subsystem's constants
     * @return
     */
    public String getCommand() {
        return command;
    }
    
    /**
     * Get the delay time between pattern segments
     * @return The delay time between pattern segments in miliseconds
     */
    public int getWaitMS() {
        return wait_ms;
    }
    
    /**
     * Gets the commands operation periods, which declare when the pattern is supposed to operate.
     * @return A list of BlingController periods when this pattern is supposed to run, one of Teleop, Autonomous or Climb or a combination
     */
    public List<Integer> getPeriod() {
        return operationPeriod;
    }
    
    /**
     * If the command would like to run something while it is being displayed,
     * it should be run here.
     */
    public void runCommand() {
        
    }
    
    /**
     * Function that resets the bling pattern after it is changed to.
     */
    public void reset() {
        hasRun = false;
    }
}
