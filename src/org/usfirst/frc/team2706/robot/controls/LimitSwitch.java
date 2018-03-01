package org.usfirst.frc.team2706.robot.controls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class LimitSwitch extends Trigger {

    private final DigitalInput input;
    
    public LimitSwitch(int channel) {
        input = new DigitalInput(channel);
    }
    
    @Override
    public boolean get() {
        return !input.get();
    }

}
