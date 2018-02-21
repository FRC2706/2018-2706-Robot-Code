package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;

/**
 * 
 * @author eAUE (Kyle Anderson)
 * Pattern for showing if the cube is successfully loaded.
 */
public class IntakeSignaller extends BlingPattern {
    
    
    boolean wasCubeJustIn;
    /**
     * Pattern for showing if the cube is sucessfully loaded.
     */
    public IntakeSignaller() {
        
        // Add periods for the pattern to signal.
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);
        
        /**
         * True if the cube was in the intake last time this pattern ran.
         */
        wasCubeJustIn = false;
    }
    
    @Override
    public boolean conditionsMet() {
        boolean isCubeIn = false;
        return isCubeIn && !wasCubeJustIn;
    }
}
