package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterAutoRightSwitch extends CommandGroup {
    
    public CenterAutoRightSwitch() {
        this.addSequential(new StraightDriveWithEncoders(0.8,24.0 / 12.0,1.0,3, "forwardFromStart"));
        this.addSequential(new RotateDriveWithGyro(0.5,31.17,2, "turnRightTowardsSwitch"));
        this.addSequential(new StraightDriveWithEncoders(0.8, 116.75 / 12.0,1.0, 3, "moveRightTowardsSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.5, -31.17, 2,"turnTowardsSwitch"));
        this.addSequential(new StraightDriveWithEncoders(0.8, 32.0 / 12.0, 1.0, 3, "forwardToSwitch"));
    }
    
}