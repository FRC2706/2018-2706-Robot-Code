package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * @author eAUE (Kyle Anderson)
 * Pattern for showing if the cube is successfully loaded.
 */
public class IntakeSignaller extends BlingPattern {
    
    
    boolean wasCubeJustIn;
    double timePoint = 0;
    private Intake intakeSubsystem; 
    /**
     * Pattern for showing if the cube is successfully loaded.
     */
    public IntakeSignaller() {
        
        // Add periods for the pattern to signal.
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);
        
        // Set Colour to green
        rgbColourCode[0] = 0;
        rgbColourCode[1] = 255;
        rgbColourCode[2] = 0;


        command = Bling.SOLID;
        repeatCount = 0;
        wait_ms = 0;
        
        /**
         * True if the cube was in the intake last time this pattern ran.
         */
        wasCubeJustIn = false;
        
        intakeSubsystem = Robot.intake;
    }
    
    /**
     * Set it to true when beginning to display the pattern.
     */
    boolean patternBeingDisplayed = false;
    
    @Override
    public boolean conditionsMet() {
        // Determine if the cube is currently in the robot.
        boolean isCubeIn = intakeSubsystem.cubeCaptured();

        // True if the cube was not in before but it is now in.
        boolean cubeStateSwitched = isCubeIn && !wasCubeJustIn;
        
        wasCubeJustIn = isCubeIn; // Reset the status of this variable.
        
        // If the cube state was switched, begin timing the pattern.
        if (cubeStateSwitched) timePoint = Timer.getFPGATimestamp(); 
        // Determine the time passed since the last run.
        double timePassed = Timer.getFPGATimestamp() - timePoint;
        
        /* Update the patternBeingDisplayed boolean as needed
         * If the cubeStateWasSwitched, we make it true no mater what.
         * If not, if we were last displaying a pattern and are under 3 seconds in of 
         * doing so, continue to display the pattern so long as we also still have the cube.
         */
        patternBeingDisplayed = cubeStateSwitched || (patternBeingDisplayed && timePassed < 3 && isCubeIn);
        
        
        // Display if the timePassed is under 3 seconds. 
        return patternBeingDisplayed;
    }
    
    @Override
    public void runCommand() {
        // Rumble the joystick when the pattern is run.
        new Rumbler(0.8, 0.2, 1, Rumbler.OPERATOR_JOYSTICK);
        hasRun = true;
    }
}
