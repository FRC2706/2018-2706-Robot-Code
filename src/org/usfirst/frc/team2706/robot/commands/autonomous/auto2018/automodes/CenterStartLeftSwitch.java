package org.usfirst.frc.team2706.robot.commands.autonomous.auto2018.automodes;

import org.usfirst.frc.team2706.robot.commands.EjectCube;
import org.usfirst.frc.team2706.robot.commands.MoveLiftDown;
import org.usfirst.frc.team2706.robot.commands.autonomous.experimential.curvedrive.CurveDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterStartLeftSwitch extends CommandGroup {
    
    public CenterStartLeftSwitch() {
        this.addSequential(new MoveLiftDown(),0.25);
        this.addSequential(new CurveDrive(85 / 12.0, 94 / 12.0, 0, 0.65, true, 0.25, "CurveToSfawfawfwitch"));
        this.addSequential(new MoveLiftDown(),3);
        this.addSequential(new EjectCube(0.6),2);
    }
    
}