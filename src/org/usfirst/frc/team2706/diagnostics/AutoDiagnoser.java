package org.usfirst.frc.team2706.diagnostics;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
/**
 * Goal of this class is to test the hardware of the robot automatically
 * to make sure that everything is working right.
 * @author eAUE (Kyle Anderson)
 *
 */
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
public class AutoDiagnoser extends Command{
    
    protected static int caseNo = 0;
    protected static boolean error = false;
    protected static double timeSpot = 0;
    protected static double motorSpeed;
    protected static StraightDriveWithTime drive;
    
    public AutoDiagnoser() {
        Log.i("AutoDiagnoser", "\nDiagnostics beginning...");
    }
    
    
    public void execute() {
        
        switch (caseNo) {
            // Testing movement
            case 1:
                if (timeSpot == 0) {
                    timeSpot = Timer.getFPGATimestamp();
                    drive = new StraightDriveWithTime(0.1, 600);
                    drive.start();
                }
                
                else if ((Timer.getFPGATimestamp() - timeSpot) > 0.5) {
                    double distance = Robot.driveTrain.getDistance();
                    if (distance < 3) {
                        DriverStation.reportWarning("Cannot drive!", false);
                        error = true;
                    }
                    else
                        Log.i("AutoDiagnoser", "Driving appears to be working unless encoders " +
                                        "also don't work");
                }
                
            // Testing 
            case 2:
            case 3:
        }
    }

    @Override
    protected boolean isFinished() {
        return (caseNo > 5);
    }
    
    
    protected void interrupted() {
        end();
    }
    
}
