package org.usfirst.frc.team2706.robot.commands.bling.patterns;

import org.usfirst.frc.team2706.robot.commands.bling.BlingController;
import org.usfirst.frc.team2706.robot.subsystems.Bling;

public class FunDuringAuto extends BlingPattern {

    public FunDuringAuto() {
        // Do a rainbow command
        command = Bling.RAINBOW;
        
        // Only operate during the autonomous period
        operationPeriod.add(BlingController.AUTONOMOUS_PERIOD);
        
        rgbColourCode[0] = 255;
        rgbColourCode[1] = 255;
        rgbColourCode[2] = 0;
        
        LED_BRIGHTNESS = 255;
    }
    
    @Override
    public boolean conditionsMet() {
        return true;
    }

}
