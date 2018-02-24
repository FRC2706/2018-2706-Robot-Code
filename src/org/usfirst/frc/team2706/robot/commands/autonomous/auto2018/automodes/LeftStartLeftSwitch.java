package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftSwitch extends CommandGroup {
    
    public LeftStartLeftSwitch() {
        this.addSequential(new MoveLiftDown(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.5, 135 / 12.0, 1, 5, "FofToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.35,90,2,"RotateTofitch"));
        this.addSequential(new MoveLiftDown(),3);
        this.addSequential(new StraightDriveWithEncoders(0.5,25 / 12.0,1,5, "Prefgfitch"),3);
     
        this.addSequential(new EjectCube(0.6),2);
    }
    
}