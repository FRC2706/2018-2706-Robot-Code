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
   
    protected List<Integer> operationPeriod = new ArrayList<Integer>();
    
    /* True if the pattern has already been run at least once
     * and it was the last pattern to run.
     */
    protected boolean hasRun = false;
    
    public BlingPattern() {        
        blingSystem = Robot.blingSystem;
        driveTrain = Robot.driveTrain;
    }
    
    /**
     * Analyzes whether or not the conditions for this pattern are being met,
     * and returns a boolean based on this.
     * This should be overriden for real bling commands, as by default it returns false every time.
     * @return True if this bling pattern's operating conditions are met, false
     * otherwise.
     */
    public boolean conditionsMet() {
        return false;
    }
    
    public String getPattern() {
        return null;
    }
    
    public List<Integer> getPeriod() {
        return operationPeriod;
    }
    
    /**
     * Function that resets the bling pattern after it is changed to.
     */
    public void reset() {
        hasRun = false;
    }
}
