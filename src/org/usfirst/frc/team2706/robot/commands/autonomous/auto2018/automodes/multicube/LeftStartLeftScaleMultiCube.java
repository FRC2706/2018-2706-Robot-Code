package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes.multicube;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.IntakeCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftScaleMultiCube extends CommandGroup {
    
    public LeftStartLeftScaleMultiCube() {
        this.addSequential(new MoveLiftDown(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.5,282 / 12.0 /*1/2robotlength*/,1,3, "startawfassdForwardToScale"));
        this.addSequential(new RotateDriveWithGyro(0.35,90.0,2, "turnRightTowardsSawfawffcale"));
        this.addSequential(new StraightDriveWithEncoders(0.5,40 / 12.0,1.0, 3, "endForawfawfawwardToScale"));
        this.addSequential(new MoveLiftDown(),6);
        this.addSequential(new EjectCube(0.6),2);
        
        this.addSequential(new MoveLiftUp(),6);
        this.addSequential(new StraightDriveWithEncoders(0.5,-40 / 12.0,1.0, 3, "endForawfawfawwardToScale"));
        this.addSequential(new RotateDriveWithGyro(0.35,60.0,2, "turnRightTowardsSawfawffcale"));
        this.addParallel(new IntakeCube(0.6),4);
        this.addSequential(new StraightDriveWithEncoders(0.5,40 / 12.0,1.0, 3, "endForawfawfawwardToScale"));
        this.addParallel(new MoveLiftUp(),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,20 / 12.0,1.0, 3, "endForawfawfawwardToScale"));
        this.addSequential(new EjectCube(0.6),2);
    }
    
}