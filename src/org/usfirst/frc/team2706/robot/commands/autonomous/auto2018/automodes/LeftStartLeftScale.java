package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlockingAfterTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.AutoConstants;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDriveTwoSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Starts in the left position.
 * 
 * Curve drives to the left scale, places the cube, and drives towards the next cube
 */
public class LeftStartLeftScale extends CommandGroup {
    
    public LeftStartLeftScale() {
        // Move the lift up, then start driving and stop quickly to drop intake
        this.addParallel(new SetLiftHeightBlocking(1,2,0.2));
        this.addSequential(new StraightDriveWithEncoders(1.0,0.3,0.1,1, this + ".stutterStart"),0.5);
        this.addSequential(new StraightDriveWithEncoders(0.0,0.25,0.1,1, this + ".stutterStop"),0.15);
        
        // Raise the lift and drive to the scale
        this.addParallel(new SetLiftHeightBlocking(AutoConstants.SCALE_HEIGHT, 5, 0.2));
        this.addSequential(new CurveDriveTwoSpeed(2.5,20,25,0.95,0.9,0.7,10, 14,false, this + ".curveToScale"));
        
        // Place the cube in the scale
        this.addSequential(new EjectCube(0.8),0.5);
        
        // Back off of scale
        this.addSequential(new StraightDriveWithEncoders(AutoConstants.SPEED_CONTROLLED, -2,1,2,"aff"));
        
        // Lower lift and turn towards switch
        this.addParallel(new SetLiftHeightBlockingAfterTime(Double.MIN_VALUE,5,0.1, 10),5);
        this.addSequential(new RotateDriveWithGyro(0.45, -25, 2, 3, this + ".turnRightTowardsSwitch"),3);
        
        // Drive towards cube
        this.addSequential(new StraightDriveWithEncoders(0.75,-5,1.0,1,"a"));
    }
    
}