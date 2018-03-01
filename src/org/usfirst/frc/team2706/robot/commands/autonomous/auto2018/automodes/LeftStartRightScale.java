package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftUp;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.core.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftStartRightScale extends CommandGroup{

    public LeftStartRightScale() {
        this.addSequential(new MoveLiftUp(),0.25);
        this.addSequential(new StraightDriveWithEncoders(0.5, 232 / 12.0, 1, 5, "awfawfFdghorwardToSwitch"));
        this.addSequential(new RotateDriveWithGyro(0.35,90,3,"RotateToSwitchawf"));
        this.addSequential(new StraightDriveWithEncoders(0.5,148 / 12.0,1,5, "PressSwifawtch"));
        this.addSequential(new RotateDriveWithGyro(0.35,-90,3,"RotateToSwitcfh"));
        this.addSequential(new StraightDriveWithEncoders(0.5,37 / 12.0,1,5, "PressSwfawawfawfawfitch"));
        this.addSequential(new MoveLiftUp(),6);
        this.addSequential(new EjectCube(0.6),2);
    }
}
