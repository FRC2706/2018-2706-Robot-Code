package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScale extends CommandGroup {
    
    public RightStartRightScale() {
        this.addParallel(new SetLiftHeightBlocking(5.25,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(0.8,18.795 /*1/2robotlength*/,1,1, this + ".startForwardToScale"));
        
        //this.addSequential(new StraightDriveWithEncoders(0.6,8.795 /*1/2robotlength*/,2,3, this + ".startForwardToScale"));
        this.addSequential(new RotateDriveWithGyro(0.5,-36,2, this + ".turnRightTowardsScale"),2);
       // this.addSequential(new MoveLiftUp(), 3.5);
        
        this.addSequential(new StraightDriveWithEncoders(0.55,3.2,1.0, 3, this + ".endForwardToScale"),2);
      
        this.addSequential(new EjectCube(0.6),0.5);
    }
    
}