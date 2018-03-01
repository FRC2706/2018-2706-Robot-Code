package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartLeftScale extends CommandGroup {
    public RightStartLeftScale() {
        this.addSequential(new MoveLiftUp(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.65, 229 / 12.0, 2, 5, "FordfghjkwardToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3,"RotatdfghjkeToSwitch"));
        this.addSequential(new MoveLiftUp(),0.7);
        this.addSequential(new StraightDriveWithEncoders(0.65,195 / 12.0,1,5, "Pressxcvbnm,.Switch"));
        this.addSequential(new RotateDriveWithGyro(0.35,90,3,"RotatdfghyujikleToSwitch"));
        
        this.addSequential(new MoveLiftUp(),5);
        this.addSequential(new StraightDriveWithEncoders(0.45,37 / 12.0,1,5, "PressrtyuiopSwitch"), 2);
        this.addSequential(new EjectCube(0.6),2);
    }
}
