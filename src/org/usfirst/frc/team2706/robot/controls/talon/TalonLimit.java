package org.usfirst.frc.team2706.robot.controls.talon;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

public class TalonLimit extends WPI_TalonSRX {

    public DigitalInput liftDown;
    public TalonLimit(int deviceNumber, DigitalInput liftDown) {
        super(deviceNumber);
        this.liftDown = liftDown;
      
        // TODO Auto-generated constructor stub
    }
    @Override
    public void set(double value) {
        System.out.println("LIFT RUNNING" + "," + value);
        if(liftDown.get() || value < 0) {
        super.set(value);
        } else {
            super.set(0);
        }
    }

}
