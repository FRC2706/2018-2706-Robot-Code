package org.usfirst.frc.team2706.robot.controls.talon;

import org.usfirst.frc.team2706.robot.controls.LimitSwitch;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonLimit extends WPI_TalonSRX {

    public LimitSwitch liftDown;
    public TalonLimit(int deviceNumber, LimitSwitch liftDown) {
        super(deviceNumber);
        this.liftDown = liftDown;
      
        // TODO Auto-generated constructor stub
    }
    @Override
    public void set(double value) {
        if(!liftDown.get() || value > 0) {
        super.set(value);
        } else {
            super.set(0);
        }
    }

}
