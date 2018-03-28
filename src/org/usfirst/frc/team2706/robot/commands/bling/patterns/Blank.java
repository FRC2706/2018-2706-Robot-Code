package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.subsystems.Bling;

public class Blank extends BlingPattern {
    public Blank() {
        
        // Add this command to all period
        operationPeriod.add(BlingController.AUTONOMOUS_PERIOD);
        operationPeriod.add(BlingController.CLIMBING_PERIOD);
        operationPeriod.add(BlingController.TELEOP_WITHOUT_CLIMB);
        
        LED_BRIGHTNESS = 0;
        command = Bling.CLEAR;
    }
    
    /**
     * Determines whether or not the conditions for this command are met.
     * @return Always true because this is the blank command
     */
    @Override
    public boolean conditionsMet() {
        return true;
    }
}
