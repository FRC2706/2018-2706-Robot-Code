package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.controls.operatorFeedback.Rumbler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;
import org.usfirst.frc.team2706.robot.subsystems.Lift;

public class TipWarning extends BlingPattern {
    
    Lift liftSystem;
    
    public TipWarning() {
        liftSystem = Robot.lift;
        
        rgbColourCode = Bling.TURQUOISE;
        
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
    
    @Override
    public void initialize() {
        super.initialize();
        // Rumble when this begins.
        new Rumbler(0.5, 0.2, 2, Rumbler.BOTH_JOYSTICKS);
    }
}
