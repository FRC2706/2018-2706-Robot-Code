package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartLeftSwitch extends CommandGroup {
    
    public LeftStartLeftSwitch() {
        this.addParallel(new MoveLiftUp(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.8, 135 / 12.0, 3, 3, "FofToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.6,90,5,2,"RotateTofitch"));
        this.addParallel(new MoveLiftUp(),1);
        this.addSequential(new StraightDriveWithEncoders(0.8,48 / 12.0,3,1, "Prefgfitch"),1);
     
        this.addSequential(new EjectCube(0.6),0.5);
    }
    
}