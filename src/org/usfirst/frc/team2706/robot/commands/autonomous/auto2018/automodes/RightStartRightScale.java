package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightScale extends CommandGroup {
    
    public RightStartRightScale() {
        this.addSequential(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.5,280 / 12.0/*1/2robotlength*/,1,3, this + ".startForwardToScale"));
        this.addSequential(new RotateDriveWithGyro(0.4,-45.0,2, this + ".turnLeftTowardScale"));
        this.addSequential(new MoveLiftUp(),5.5);
        this.addSequential(new StraightDriveWithEncoders(0.5,35 / 12.0,1.0, 3, this + ".endForwardScale"), 2);
       
        this.addSequential(new EjectCube(0.6),2);
    }
    
}