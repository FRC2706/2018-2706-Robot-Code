package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.OneTimeCommand;

public class SetLiftHeight extends OneTimeCommand {

    private static final double pDown = 0.5, iDown = 0, dDown = 100;
    private static final double pUp = 0.5, iUp = 0, dUp = 0;
    
    public SetLiftHeight(double height) {
        super(() -> {
            if(height < Robot.lift.getEncoderHeight()) {
                 Robot.lift.setPID(pDown, iDown, dDown);
                 Robot.lift.setHeight(height, false);
            }
            else {
                Robot.lift.setPID(pUp, iUp, dUp);
                Robot.lift.setHeight(height, false);
            }
        });
       
        
        Log.d(this, "Height: " + height);
//        SmartDashboard.putNumber("P Down", SmartDashboard.getNumber("P Down", pDown));
//        SmartDashboard.putNumber("I Down", SmartDashboard.getNumber("I Down", iDown));
//        SmartDashboard.putNumber("D Down", SmartDashboard.getNumber("D Down", dDown));
//        
//        SmartDashboard.putNumber("P Up", SmartDashboard.getNumber("P Up", pUp));
//        SmartDashboard.putNumber("I Up", SmartDashboard.getNumber("I Up", iUp));
//        SmartDashboard.putNumber("D Up", SmartDashboard.getNumber("D Up", dUp));
    }
}
