package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        //this.addSequential(new SetPosition(1));
        this.addSequential(new CurveDrive(6.395, 10.33, 0, 0.65, true, 0.25, "CurveToSwitch"));
        //this.addSequential(new SetPosition(2ft));
        this.addSequential(new EjectCube(0.8),2);
    }
    
}