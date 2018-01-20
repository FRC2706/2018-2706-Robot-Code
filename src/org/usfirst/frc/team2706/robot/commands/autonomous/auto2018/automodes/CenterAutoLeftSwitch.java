package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterAutoLeftSwitch extends CommandGroup {
    
    public CenterAutoLeftSwitch() {
        this.addSequential(new StraightDriveWithEncoders(0.8,24.0 / 12.0,1.0,3));
        this.addSequential(new RotateDriveWithGyro(0.5,-37.5,2));
        this.addSequential(new StraightDriveWithEncoders(0.8, 126.05 / 12.0,1.0, 3));
        this.addSequential(new RotateDriveWithGyro(0.5, 37.5, 2));
        this.addSequential(new StraightDriveWithEncoders(0.8, 32.0 / 12.0, 1.0, 3));
    }
    
}
