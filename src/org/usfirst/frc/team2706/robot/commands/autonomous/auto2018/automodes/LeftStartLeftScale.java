package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftScale extends CommandGroup {
    
    public LeftStartLeftScale() {
    //    this.addSequential(new SetLiftHeight(0.2));
        this.addSequential(new StraightDriveWithEncoders(0.8,282 / 12.0 /*1/2robotlength*/,3,3, "startawfassdForwardToScale"));
        this.addSequential(new RotateDriveWithGyro(0.4,90.0,2, "turnRightTowardsSawfawffcale"));
        this.addSequential(new MoveLiftUp(), 5);
       // this.addSequential(new StraightDriveWithEncoders(0.4,40 / 12.0,1.0, 3, "endForawfawfawwardToScale"));
       
        this.addSequential(new EjectCube(0.6),2);
    }
    
}