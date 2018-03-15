package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.InitLift;
import org.usfirst.frc.team2706.robot.commands.SetLiftHeightBlocking;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightSwitch extends CommandGroup {
    
    public RightStartRightSwitch() {
        this.addParallel(new InitLift());
        this.addSequential(new StraightDriveWithEncoders(0.8, 135 / 12.0, 3, 3, this + ".forwardToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.6,-90,5,2, this + ".rotateToSwitch"));
        this.addParallel(new SetLiftHeightBlocking(2.5,2,0.2),2);
        this.addSequential(new StraightDriveWithEncoders(0.5,48 / 12.0,3,1, this + ".driveToSwitch"),1);
     
        this.addSequential(new EjectCube(0.8),0.8);
    }
    
}