package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
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
        // Set brightness to more than 0
        
        /**
         * True if the cube was in the intake last time this pattern ran.
         */
        wasCubeJustIn = false;
        
        intakeSubsystem = Robot.intake;
    }
    
    @Override
    public boolean conditionsMet() {
        boolean isCubeIn = intakeSubsystem.cubeCaptured(); // TODO get actual value here.
        if (isCubeIn && !wasCubeJustIn) timePoint = Timer.getFPGATimestamp(); 
        double timePassed = Timer.getFPGATimestamp() - timePoint;
        
        System.out.println("IntakeSignaller: timepassed : " + timePassed + " isCubeIn : " + isCubeIn); // TODO remove debug print
        
        // Display if the timePassed is under 3 seconds. 
        return (isCubeIn && !wasCubeJustIn) || timePassed <= 3;
    }
    
    @Override
    public void runCommand() {
        // Rumble the joystick when the pattern is run.
        if (!hasRun) new Rumbler(0.8, 0.2, 3, Rumbler.OPERATOR_JOYSTICK);
        hasRun = true;
    }
}
