package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftScaleMultiCube extends CommandGroup {
    
    public LeftStartLeftScaleMultiCube() {
       // this.addParallel(new SetLiftHeight(0.1));
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.7,18.795 /*1/2robotlength*/,1,3, this + ".startForwardToScale"));
        this.addParallel(new SetLiftHeightBlocking(5.25,2,0.2),5);
        this.addSequential(new RotateDriveWithGyro(0.6,36,2, this + ".turnRightTowardsScale"),2);
       // this.addSequential(new MoveLiftUp(), 3.5);
        
        this.addSequential(new StraightDriveWithEncoders(0.55,3,1.0, 3, this + ".endForwardToScale"),2);
      
        this.addSequential(new EjectCube(0.6),0.5);
        
        this.addParallel(new SetLiftHeightBlocking(0,2,0.2),5);
        this.addSequential(new StraightDriveWithEncoders(0.55,-2.48,1.0, 3, this + ".endForwardToScale"),2);
        
        this.addSequential(new RotateDriveWithGyro(0.6,125,2, this + ".turnRightTowardsScale"));
        this.addParallel(new IntakeCube(0.6),4);
        this.addSequential(new StraightDriveWithEncoders(0.6,5.32,1.0, 3, this + ".endForwardToScale"),4);
        this.addParallel(new SetLiftHeightBlocking(2.5,2,0.1),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"),2);
        this.addSequential(new EjectCube(0.6),2);
    }
    
}