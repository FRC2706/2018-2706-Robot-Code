package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        this.addSequential(new StraightDriveWithEncoders(0.8,24.0 / 12.0,1.0,3, "forwardFromStart"));
        this.addSequential(new RotateDriveWithGyro(0.5,-37.5,2, "turnLeftTowardsSwitch"));
        this.addSequential(new StraightDriveWithEncoders(0.8, 126.05 / 12.0,1.0, 3, "moveLeftTowardsSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.5, 37.5, 2,"turnTowardsSwitch"));
        this.addSequential(new StraightDriveWithEncoders(0.8, 32.0 / 12.0, 1.0, 3, "forwardToSwitch"));
    }
    
}