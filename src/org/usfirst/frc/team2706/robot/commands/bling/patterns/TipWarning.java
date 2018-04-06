package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

public class TipWarning extends BlingPattern {
    
    Lift liftSystem;
    Rumbler rumbler;
    
    public TipWarning() {
        liftSystem = Robot.lift;
        
        rgbColourCode[0] = 0;
        rgbColourCode[1] = 255;
        rgbColourCode[2] = 255;
        
        // Set the command that the bling strip will perform.
        command = Bling.BLINK;
        // Set the delay
        wait_ms = 100;
        
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);
    }

    @Override
    public boolean conditionsMet() {
        // If the arm height is above half the maximum height, begin warning about tipping 
        return (liftSystem.getEncoderHeight() > (Lift.MAX_HEIGHT / 2));
    }
    
    public void runCommand() {
//        rumbler = new Rumbler(10000, 0, -1, Rumbler.BOTH_JOYSTICKS);
    }
    
    @Override
    public void reset() {
        // Run the base command
        super.reset();
        
        // End the rumbler when this pattern is reset.
//        rumbler.end();
    }

}
