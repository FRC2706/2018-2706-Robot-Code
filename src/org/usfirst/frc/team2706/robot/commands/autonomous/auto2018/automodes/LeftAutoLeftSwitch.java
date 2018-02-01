package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAutoLeftSwitch extends CommandGroup {
    
    public LeftAutoLeftSwitch() {
        this.addSequential(new StraightDriveWithEncoders(0.8,168.0 / 12.0 - (17.0 / 12.0 /*1/2robotlength*/),1.0,3, "startForwardToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.5,-90.0,2, "turnLeftTowardsSwitch"));
        this.addSequential(new StraightDriveWithEncoders(0.8, 65.56 / 12.0 - (17.0 / 12.0),1.0, 3, "endForwardToSwitch"));
    }
    
}