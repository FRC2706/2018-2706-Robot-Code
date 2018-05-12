package org.usfirst.frc.team2706.robot.controls;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Log;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A trigger that activates when the limit switch is pressed
 */
public class LimitSwitch extends Trigger {

    private final Supplier<Boolean> input;
    
    /**
     * Creates a Talon limit switch
     * 
     * @param talon The Talon with the limit switch
     * @param forward Whether the limit switch is on forward or reverse
     */
    public LimitSwitch(TalonSRX talon, boolean forward) {
        if(forward) {
            input = talon.getSensorCollection()::isFwdLimitSwitchClosed;
        }
        else {
            input = talon.getSensorCollection()::isRevLimitSwitchClosed;
        }
    }
    
    /**
     * Creates a DIO limit switch
     * 
     * @param channel The channel of the DIO
     */
    public LimitSwitch(int channel) {
        final DigitalInput dio;
        try {
            // Try to create the limit switch
            dio = new DigitalInput(channel);
        }
        catch(RuntimeException e) {
            Log.e("Limit Switch", "Could not create DIO limit switch", e);
            
            // If the limit switch does not exist, keep the switch unpressed
            input = () -> false;
            return;
        }
        
        // Assume that the switch is open when pressed
        input = () -> !dio.get();
        
    }
    
    @Override
    public boolean get() {
        return input.get();
    }

}
